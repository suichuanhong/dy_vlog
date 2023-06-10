package cn.schff.dyvlog.service.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.schff.dyvlog.common.dto.VlogDTO;
import cn.schff.dyvlog.common.exception.UpdateFailException;
import cn.schff.dyvlog.mapper.MyLikedVlogMapper;
import cn.schff.dyvlog.mapper.VlogMapper;
import cn.schff.dyvlog.pojo.MyLikedVlog;
import cn.schff.dyvlog.pojo.User;
import cn.schff.dyvlog.pojo.Vlog;
import cn.schff.dyvlog.service.MessageService;
import cn.schff.dyvlog.service.MyLikedVlogService;
import cn.schff.dyvlog.service.UserService;
import cn.schff.dyvlog.service.VlogService;
import cn.schff.dyvlog.common.util.IdUtil;
import cn.schff.dyvlog.common.util.Result;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

import static cn.schff.dyvlog.common.util.RedisConstant.*;
import static cn.schff.dyvlog.common.util.SystemConstant.*;

/**
 * @Author：眭传洪
 * @Create：2023/4/29 22:24
 * @Meet: 2022/02/25
 * @Start: 2022/6/24
 */
@Service
public class VlogServiceImpl extends ServiceImpl<VlogMapper, Vlog> implements VlogService {

    @Autowired
    private IdUtil idUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private MyLikedVlogService myLikedVlogService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private MyLikedVlogMapper myLikedVlogMapper;

    @Autowired
    private MessageService messageService;

    @Override
    public Result publish(Vlog vlog) {
        if (vlog == null) {
            return Result.fail("视频信息保存失败");
        }
        User user = userService.getById(vlog.getVlogerId());
        if (user == null) {
            return Result.fail("用户不存在");
        }
        vlog.setId(idUtil.getId("vlog").toString());
        vlog.setIsPrivate(DEFAULT_VLOG_STATUS);
        vlog.setLikeCounts(INIT_LIKE_COUNT);
        vlog.setCommentsCounts(VLOG_COMMENTS);
        vlog.setCreatedTime(LocalDateTime.now());
        vlog.setUpdatedTime(LocalDateTime.now());
        save(vlog);
        return Result.success();
    }

    @Override
    public Result getVlogList(String userId, String search, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<VlogDTO> vlogs = baseMapper.queryVlogList(search);
        // 已登录处理后再返回
        if (StrUtil.isNotBlank(userId)) {
            // 设置是否关注和点赞
            vlogs.forEach(vlog -> setFollowAndLike(vlog, userId));
        }
        // 未登录直接返回
        PageInfo<VlogDTO> pageInfo = new PageInfo<>(vlogs);
        return Result.success(pageInfo);
    }

    @Override
    public Result getVlogDetail(String userId, String vlogId) {
        VlogDTO vlogDTO = baseMapper.queryVlogDetailById(vlogId);
        if (vlogDTO == null) {
            return Result.success(Collections.emptyList());
        }
        setFollowAndLike(vlogDTO, userId);
        return Result.success(vlogDTO);
    }

    private void setFollowAndLike(VlogDTO vlog, String userId) {
        String followKey = USERINFO_QUERY_FOLLOW_KEY + userId;
        // 设置是否关注
        Boolean isFollow = stringRedisTemplate.opsForSet().isMember(followKey, vlog.getVlogerId());
        vlog.setDoIFollowVloger(BooleanUtil.isTrue(isFollow));
        // 设置本人是否点赞
        String likeKey = VLOG_LIKE + vlog.getVlogId();
        Boolean isLike = stringRedisTemplate.opsForSet().isMember(likeKey, userId);
        vlog.setDoILikeThisVlog(BooleanUtil.isTrue(isLike));
    }

    @Transactional
    @Override
    public Result changeVlogToPublicOrPrivate(String userId, String vlogId, boolean isPublic) {
        // 根据用户id和视频id更新isPrivate字段即可
        boolean isSuccess = update()
                .set("is_private", isPublic ? 0 : 1)
                .eq("id", vlogId)
                .eq("vloger_id", userId)
                .update();
        if (!isSuccess) {
            throw new UpdateFailException("更新视频状态失败");
        }
        return Result.success();
    }

    @Override
    public Result getMyVlogList(String userId, int page, int pageSize, boolean isPublic) {
        PageHelper.startPage(page, pageSize);
        List<Vlog> vlogs = query()
                .eq("vloger_id", userId)
                .eq("is_private", isPublic ? 0 : 1)
                .list();
        if (vlogs == null) {
            return Result.success(Collections.emptyList());
        }
        PageInfo<Vlog> pageInfo = new PageInfo<>(vlogs);
        return Result.success(pageInfo);
    }

    @Transactional
    @Override
    public Result vlogLike(String userId, String vlogerId, String vlogId) {
        // 视频保存key
        String vlogSaveKey = USER_VLOG_LIKE_KEY + userId;
        // 如果重复点赞直接返回失败
        Boolean isLike = stringRedisTemplate.opsForSet().isMember(vlogSaveKey, userId);
        if (BooleanUtil.isTrue(isLike)) {
            return Result.fail("重复点赞");
        }
        // 视频点赞key
        String vlogLikeKey = VLOG_LIKE + vlogId;
        // 将视频id存到用户点赞视频key中
        stringRedisTemplate.opsForSet().add(vlogSaveKey, vlogId);
        // 将点赞者id存入到视频key中
        stringRedisTemplate.opsForSet().add(vlogLikeKey, userId);
        // 被点赞者点赞数自增
        String likeUserKey = USER_VLOG_LIKE_COUNT_KEY + vlogerId;
        stringRedisTemplate.opsForValue().increment(likeUserKey);
        // 点赞信息存入数据库
        MyLikedVlog myLikedVlog = new MyLikedVlog();
        myLikedVlog.setId(idUtil.getId("likeVlog").toString());
        myLikedVlog.setUserId(userId);
        myLikedVlog.setVlogId(vlogId);
        boolean isSuccess = myLikedVlogService.save(myLikedVlog);
        if (!isSuccess) {
            return Result.fail("视频点赞失败");
        }
        // 获取视频点赞量
        Long vlogLikeCount = stringRedisTemplate.opsForSet().size(vlogLikeKey);
        // 更新视频点赞个数
        isSuccess = update().set("like_counts", vlogLikeCount)
                .eq("vloger_id", vlogerId)
                .eq("id", vlogId)
                .update();
        if (!isSuccess) {
            return Result.fail("视频点赞失败");
        }
        // 将点赞消息存到mongodb
        Map<String, Object> mapContent = new HashMap<>();
        mapContent.put("vlogId", vlogId);
        mapContent.put("vlogCover", getById(vlogId).getCover());
        messageService.save(userId, vlogerId, mapContent, LIKE_VLOG_TYPE);
        return Result.success();
    }

    @Transactional
    @Override
    public Result vlogUnLike(String userId, String vlogerId, String vlogId) {
        // 先获取视频key点赞量然后移除当前用户
        String vlogLikeKey = VLOG_LIKE + vlogId;
        Long likeCount = stringRedisTemplate.opsForSet().size(vlogLikeKey);
        assert likeCount != null;
        if (likeCount.equals(ZERO)) {
            return Result.fail("取消视频点赞失败");
        }
        // 用户点赞key移除当前视频
        String userLikeKey = USER_VLOG_LIKE_KEY + userId;
        stringRedisTemplate.opsForSet().remove(userLikeKey, vlogId);
        stringRedisTemplate.opsForSet().remove(vlogLikeKey, userId);
        // 被取消用户点赞量-1
        String likeUserKey = USER_VLOG_LIKE_COUNT_KEY + vlogerId;
        stringRedisTemplate.opsForValue().increment(likeUserKey, -1);
        // 视频表点赞减1
        boolean isSuccess = update().set("like_counts", likeCount - 1)
                .eq("vloger_id", vlogerId)
                .eq("id", vlogId)
                .update();
        if (!isSuccess) {
            return Result.fail("取消视频点赞失败");
        }
        // 点赞表删除信息
        isSuccess = myLikedVlogMapper.deleteByUserIdAndVlogId(userId, vlogId);
        // 点赞信息存入数据库
        if (!isSuccess) {
            return Result.fail("取消视频点赞失败");
        }
        return Result.success();
    }

    @Override
    public Result getVlogTotalLikeCounts(String vlogId) {
        String vlogLikeKey = VLOG_LIKE + vlogId;
        Long likeCount = stringRedisTemplate.opsForSet().size(vlogLikeKey);
        return Result.success(likeCount);
    }

    @Override
    public Result getMyLikedList(String userId, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<Vlog> vlogs = baseMapper.queryMyLikeList(userId);
        if (vlogs == null) {
            return Result.success(Collections.emptyList());
        }
        PageInfo<Vlog> pageInfo = new PageInfo<>(vlogs);
        return Result.success(pageInfo);
    }

    @Override
    public Result getFollowList(String myId, int page, int pageSize) {
        // 通过myId查询redis中关注的人的id然后查询数据库返回
        String followKey = USERINFO_QUERY_FOLLOW_KEY + myId;
        Set<String> followIds = stringRedisTemplate.opsForSet().members(followKey);
        return getListByIds(followIds, myId, page, pageSize);
    }

    @Override
    public Result getFriendList(String myId, int page, int pageSize) {
        // 通过redis中myId的关注和粉丝求出共同关注获得id查询数据库返回结果
        String followKey = USERINFO_QUERY_FOLLOW_KEY + myId;
        String fanKey = USERINFO_QUERY_FAN_KEY + myId;
        Set<String> friendIds = stringRedisTemplate.opsForSet().intersect(followKey, fanKey);
        return getListByIds(friendIds, myId, page, pageSize);
    }

    /**
     * 根据id获取视频列表
     *
     * @param ids      视频id
     * @param myId     请求者id
     * @param page     页码
     * @param pageSize 一页的长度
     * @return 返回包含页码信息的json数据
     */
    private Result getListByIds(Set<String> ids, String myId, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<VlogDTO> vlogDTOS = baseMapper.queryListByIds(ids);
        if (vlogDTOS == null) {
            return Result.success(Collections.emptyList());
        }
        vlogDTOS.forEach(vlog -> setFollowAndLike(vlog, myId));
        PageInfo<VlogDTO> pageInfo = new PageInfo<>(vlogDTOS);
        return Result.success(pageInfo);
    }

}
