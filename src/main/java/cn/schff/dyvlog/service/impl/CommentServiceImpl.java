package cn.schff.dyvlog.service.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.schff.dyvlog.common.dto.CommentDTO;
import cn.schff.dyvlog.common.exception.ParameterException;
import cn.schff.dyvlog.common.exception.RepeatOperException;
import cn.schff.dyvlog.common.exception.UpdateFailException;
import cn.schff.dyvlog.common.util.IdUtil;
import cn.schff.dyvlog.common.util.Result;
import cn.schff.dyvlog.mapper.CommentMapper;
import cn.schff.dyvlog.pojo.Comment;
import cn.schff.dyvlog.pojo.Vlog;
import cn.schff.dyvlog.service.CommentService;
import cn.schff.dyvlog.service.MessageService;
import cn.schff.dyvlog.service.VlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.schff.dyvlog.common.util.RedisConstant.*;
import static cn.schff.dyvlog.common.util.SystemConstant.*;

/**
 * @Author：眭传洪
 * @Create：2023/4/29 22:21
 * @Meet: 2022/02/25
 * @Start: 2022/6/24
 */
@Slf4j
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private MessageService messageService;

    @Autowired
    private VlogService vlogService;

    @Autowired
    private IdUtil idUtil;

    @Transactional
    @Override
    public Result publishComment(Comment comment) {
        // 创建评论保存数据库
        comment.setId(idUtil.getId("comment").toString());
        comment.setCreateTime(LocalDateTime.now());
        comment.setLikeCounts(INIT_LIKE_COUNT);
        save(comment);
        // 将评论个数自增
        String key = COMMENT_COUNTS_KEY + comment.getVlogId();
        stringRedisTemplate.opsForValue().increment(key);
        // 将评论信息保存到mongodb
        Vlog vlog = vlogService.getById(comment.getVlogId());
        Map<String, Object> contentMap = new HashMap<>();
        contentMap.put("vlogId", vlog.getId());
        contentMap.put("vlogCover", vlog.getCover());
        contentMap.put("commentId", comment.getId());
        contentMap.put("commentContent", comment.getContent());
        // 如果是回复需要单独设置type和toUserId
        Integer type = COMMENT_VLOG_TYPE;
        String toUserId = vlog.getVlogerId();
        if (StrUtil.isNotBlank(comment.getFatherCommentId()) && !comment.getFatherCommentId().equals("0")) {
            type = ANSWER_COMMENT_TYPE;
            toUserId = getById(comment.getFatherCommentId()).getCommentUserId();
        }
        messageService.save(comment.getCommentUserId(), toUserId, contentMap, type);
        return Result.success(comment);
    }

    @Override
    public Result getVlogCommentCounts(String vlogId) {
        if (StrUtil.isBlank(vlogId)) {
            throw new ParameterException("vlogId参数值异常");
        }
        int data = 0;
        String key = COMMENT_COUNTS_KEY + vlogId;
        String commentCountsStr = stringRedisTemplate.opsForValue().get(key);
        if (commentCountsStr != null && StrUtil.isNotBlank(commentCountsStr)) {
            data = Integer.parseInt(commentCountsStr);
        }
        return Result.success(data);
    }

    @Override
    public Result getVlogCommentList(String vlogId, String userId, int page, int pageSize) {
        if (StrUtil.isBlank(vlogId)) {
            throw new ParameterException("vlogId参数值异常");
        }
        PageHelper.startPage(page, pageSize);
        List<CommentDTO> comments = baseMapper.queryCommentsByVlogId(vlogId);
        if (comments == null) {
            return Result.success(Collections.emptyList());
        }
        // 设置是否点赞
        comments.forEach(commentDTO -> {
            String commentId = commentDTO.getCommentId();
            String commentLikeUserKey = COMMENT_LIKE_USER_KEY + commentId;
            Boolean isLike = stringRedisTemplate.opsForSet().isMember(commentLikeUserKey, userId);
            commentDTO.setIsLike(BooleanUtil.isTrue(isLike) ? 1 : 0);
        });
        PageInfo<CommentDTO> pageInfo = new PageInfo<>(comments);
        return Result.success(pageInfo);
    }

    @Transactional
    @Override
    public Result deleteComment(String commentUserId, String commentId, String vlogId) {
        if (StrUtil.isBlank(vlogId)) {
            throw new ParameterException("vlogId参数值异常");
        }
        if (StrUtil.isBlank(commentId)) {
            throw new ParameterException("commentId参数值异常");
        }
        if (StrUtil.isBlank(commentUserId)) {
            throw new ParameterException("commentUserIdd参数值异常");
        }
        // 数据库删除记录
        boolean isSuccess = baseMapper.removeByCommentIdAndCommentUserId(commentId, commentUserId);
        if (!isSuccess) {
            throw new UpdateFailException("删除失败");
        }
        // 删除redis中存储评论点赞量的key
        String key = COMMENT_COUNTS_KEY + vlogId;
        stringRedisTemplate.delete(key);
        // redis中删除存储点赞用户的key
        String commentLikeUsersKey = COMMENT_LIKE_USER_KEY + commentId;
        stringRedisTemplate.delete(commentLikeUsersKey);
        return Result.success();
    }

    /**
     * 处理评论点赞
     * @param userId    用户id
     * @param commentId 评论id
     * @return 返回点赞是否成功的code
     */
    @Transactional
    @Override
    public Result likeComment(String userId, String commentId) {
        // 检查是否已点赞
        String commentLikeUserKey = COMMENT_LIKE_USER_KEY + commentId;
        if (BooleanUtil.isTrue(stringRedisTemplate.opsForSet().isMember(commentLikeUserKey, userId))) {
            throw new RepeatOperException("重复点赞该评论");
        }
        // 更新数据库，like_counts字段自增
        Comment comment = getById(commentId);
        String commentUserId = comment.getCommentUserId();
        update().set("like_counts", comment.getLikeCounts() + 1)
                .eq("id", commentId)
                .update();
        // 根据comment查出commentUserId自增redis
        String commentUserLIkeIcrKey = USER_COMMENT_LIKE_COUNT_KEY + commentUserId;
        stringRedisTemplate.opsForValue().increment(commentUserLIkeIcrKey, 1);
        // 将userId添加到redis的集合中
        stringRedisTemplate.opsForSet().add(commentLikeUserKey, userId);
        // 将点赞信息保存到mongodb
        Vlog vlog = vlogService.getById(comment.getVlogId());
        Map<String, Object> contentMap = new HashMap<>();
        contentMap.put("vlogId", vlog.getId());
        contentMap.put("vlogCover", vlog.getCover());
        contentMap.put("commentId", commentId);
        messageService.save(userId, commentUserId, contentMap, LIKE_COMMENT_TYPE);
        return Result.success();
    }

    @Transactional
    @Override
    public Result unLikeComment(String userId, String commentId) {
        // 更新数据库，like_counts字段自减
        Comment comment = getById(commentId);
        String commentUserId = comment.getCommentUserId();
        update().set("like_counts", comment.getLikeCounts() - 1)
                .eq("id", commentId)
                .update();
        // 根据comment查出commentUserId自减redis
        String commentUserLikeIcrKey = USER_COMMENT_LIKE_COUNT_KEY + commentUserId;
        stringRedisTemplate.opsForValue().increment(commentUserLikeIcrKey, -1);
        // 将userId从redis中移除
        String commentLikeUserKey = COMMENT_LIKE_USER_KEY + commentId;
        stringRedisTemplate.opsForSet().remove(commentLikeUserKey, userId);
        return Result.success();
    }
}
