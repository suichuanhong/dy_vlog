package cn.schff.dyvlog.service;

import cn.schff.dyvlog.pojo.Vlog;
import cn.schff.dyvlog.common.util.Result;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author：眭传洪
 * @Create：2023/4/29 21:53
 * @Meet: 2022/02/25
 * @Start: 2022/6/24
 */
public interface VlogService extends IService<Vlog> {

    Result publish(Vlog vlogDTO);

    Result getVlogList(String userId, String search, Integer page, Integer pageSize);

    Result getVlogDetail(String userId, String vlogId);

    Result changeVlogToPublicOrPrivate(String userId, String vlogId, boolean isPublic);

    Result getMyVlogList(String userId, int page, int pageSize, boolean isPublic);

    Result vlogLike(String userId, String vlogerId, String vlogId);

    Result vlogUnLike(String userId, String vlogerId, String vlogId);

    Result getVlogTotalLikeCounts(String vlogId);

    Result getMyLikedList(String userId, int page, int pageSize);

    Result getFollowList(String myId, int page, int pageSize);

    Result getFriendList(String myId, int page, int pageSize);

}
