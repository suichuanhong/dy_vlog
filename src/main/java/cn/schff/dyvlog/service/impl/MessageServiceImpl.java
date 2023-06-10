package cn.schff.dyvlog.service.impl;

import cn.schff.dyvlog.common.dto.MessageDTO;
import cn.schff.dyvlog.common.util.IdUtil;
import cn.schff.dyvlog.common.util.Result;
import cn.schff.dyvlog.pojo.MessageMO;
import cn.schff.dyvlog.pojo.User;
import cn.schff.dyvlog.service.MessageService;
import cn.schff.dyvlog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static cn.schff.dyvlog.common.util.RedisConstant.USERINFO_QUERY_FAN_KEY;
import static cn.schff.dyvlog.common.util.RedisConstant.USERINFO_QUERY_FOLLOW_KEY;

/**
 * @Author：眭传洪
 * @Create：2023/6/5 15:36
 * @Meet: 2022/02/25
 * @Start: 2022/6/24
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Resource
    private MongoTemplate mongoTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private IdUtil idUtil;

    @Override
    public void save(String fromUserId, String toUserId, Map<String, Object> content, Integer type) {
        MessageMO messageMO = new MessageMO();
        messageMO.setId(idUtil.getId("system_message"));
        messageMO.setFromUserId(fromUserId);
        User user = userService.getById(fromUserId);
        messageMO.setFromUserNickName(user.getNickname());
        messageMO.setFromUserFace(user.getFace());
        messageMO.setToUserId(toUserId);
        messageMO.setContent(content);
        messageMO.setType(type);
        messageMO.setCreate_time(LocalDateTime.now());
        mongoTemplate.save(messageMO);
    }

    @Override
    public Result getMsgList(String userId, Long page, int pageSize) {
        Query query = new Query();
        // 添加查询条件以及分页
        query.addCriteria(Criteria.where("toUserId").is(userId));
        query.with(Sort.by(Sort.Order.desc("create_time"))).skip((page - 1) * pageSize).limit(pageSize);
        // 查询mongodb
        List<MessageMO> messageMOS = mongoTemplate.find(query, MessageMO.class);
        // 设置互关
        messageMOS.forEach(messageMO -> {
            if (messageMO.getContent() == null) {
                Map<String, Object> contentMap = new HashMap<>();
                messageMO.setContent(contentMap);
                String toUserId = messageMO.getToUserId();
                String myFollowKey = USERINFO_QUERY_FOLLOW_KEY + toUserId;
                String myFanswKey = USERINFO_QUERY_FAN_KEY + toUserId;
                Set<String> commonFollowIds = stringRedisTemplate.opsForSet().intersect(myFanswKey, myFollowKey);
                contentMap.put("isFriend", commonFollowIds != null && commonFollowIds.contains(messageMO.getFromUserId()));
            }
        });
        return Result.success(new MessageDTO(messageMOS, messageMOS.size()));
    }
}
