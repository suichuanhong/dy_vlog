package cn.schff.dyvlog.service.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.schff.dyvlog.common.dto.FanDTO;
import cn.schff.dyvlog.common.dto.FollowDTO;
import cn.schff.dyvlog.common.util.IdUtil;
import cn.schff.dyvlog.common.util.Result;
import cn.schff.dyvlog.mapper.FanMapper;
import cn.schff.dyvlog.pojo.Fan;
import cn.schff.dyvlog.service.FanService;
import cn.schff.dyvlog.service.MessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.Collections;
import java.util.List;

import static cn.schff.dyvlog.common.util.RedisConstant.USERINFO_QUERY_FAN_KEY;
import static cn.schff.dyvlog.common.util.RedisConstant.USERINFO_QUERY_FOLLOW_KEY;
import static cn.schff.dyvlog.common.util.SystemConstant.*;

/**
 * @Author：眭传洪
 * @Create：2023/4/29 22:22
 * @Meet: 2022/02/25
 * @Start: 2022/6/24
 */
@Service
@Slf4j
public class FanServiceImpl extends ServiceImpl<FanMapper, Fan> implements FanService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private IdUtil idUtil;

    @Autowired
    private MessageService messageService;

    @Transactional
    @Override
    public Result followUser(String myId, String vlogerId) {
        // 我的关注列表key
        String myFollowKey = USERINFO_QUERY_FOLLOW_KEY + myId;
        // 对方关注列表key
        String vlogerFollowKey = USERINFO_QUERY_FOLLOW_KEY + vlogerId;
        // 对方粉丝列表key
        String vlogerFansKey = USERINFO_QUERY_FAN_KEY + vlogerId;
        // 检查是否关注对方
        Boolean isFollow = stringRedisTemplate.opsForSet().isMember(myFollowKey, vlogerId);
        if (BooleanUtil.isTrue(isFollow)) {
            return Result.fail("已经关注该用户");
        }
        // 将对方保存到我的关注列表
        stringRedisTemplate.opsForSet().add(myFollowKey, vlogerId);
        // 将我保存到他的粉丝列表
        stringRedisTemplate.opsForSet().add(vlogerFansKey, myId);
        // 保存到数据库
        Fan fan = new Fan();
        fan.setFanId(idUtil.getId("fan").toString());
        fan.setFanId(myId);
        fan.setVlogerId(vlogerId);
        // 将关注信息保存到mongodb
        messageService.save(myId, vlogerId, null, FOLLOW_TYPE);
        // 查询自己的id是否在对方的set中 因为我已经关注他了 所以他只要关注了我 那么我们就是朋友 需要设置为1
        Boolean isExists = stringRedisTemplate.opsForSet().isMember(vlogerFollowKey, myId);
        fan.setIsFanFriendOfMine(BooleanUtil.isTrue(isExists) ? IS_FRIEND : NOT_FRIEND);
        save(fan);
        return Result.success();
    }

    @Override
    public Result queryDoIFollowVloger(String myId, String vlogerId) {
        String key = USERINFO_QUERY_FOLLOW_KEY + myId;
        Boolean isFollow = stringRedisTemplate.opsForSet().isMember(key, vlogerId);
        return Result.success(BooleanUtil.isTrue(isFollow));
    }

    @Transactional
    @Override
    public Result cancelFollow(String myId, String vlogerId) {
        // 数据库删除信息
        Integer count = baseMapper.deleteByMyIdAndVlogerId(myId, vlogerId);
        if (count == 0) {
            return Result.fail("取消关注失败");
        }
        // redis删除关注信息
        String followKey = USERINFO_QUERY_FOLLOW_KEY + myId;
        stringRedisTemplate.opsForSet().remove(followKey, vlogerId);
        // redis删除粉丝信息
        String fansKey = USERINFO_QUERY_FAN_KEY + vlogerId;
        stringRedisTemplate.opsForSet().remove(fansKey, myId);
        return Result.success();
    }

    @Override
    public Result getMyFollows(String myId, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<FollowDTO> follows = baseMapper.getFollowUsers(myId);
        if (follows == null) {
            return Result.success(Collections.emptyList());
        }
        PageInfo<FollowDTO> followUsers = new PageInfo<>(follows);
        return Result.success(followUsers);
    }

    @Override
    public Result getMyFans(String myId, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<FanDTO> fans = baseMapper.getFans(myId);
        if (fans == null) {
            return Result.success(Collections.emptyList());
        }
        // 设置互关
        fans.forEach(fan -> {
            String fanId = fan.getFanId();
            Long count = query().eq("vloger_id", fanId).eq("fan_id", myId).count();
            fan.setFriend(count > 0);
        });
        PageInfo<FanDTO> followUsers = new PageInfo<>(fans);
        return Result.success(followUsers);
    }
}
