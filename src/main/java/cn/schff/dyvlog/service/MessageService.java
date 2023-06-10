package cn.schff.dyvlog.service;

import cn.schff.dyvlog.common.util.Result;

import java.util.Map;

/**
 * @Author：眭传洪
 * @Create：2023/6/5 15:35
 * @Meet: 2022/02/25
 * @Start: 2022/6/24
 */
public interface MessageService {

    void save(String fromUserId, String toUserId, Map<String, Object> content, Integer type);

    Result getMsgList(String userId, Long page, int pageSize);
}
