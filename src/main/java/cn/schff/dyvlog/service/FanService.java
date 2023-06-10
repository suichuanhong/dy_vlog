package cn.schff.dyvlog.service;

import cn.schff.dyvlog.common.util.Result;
import cn.schff.dyvlog.pojo.Fan;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author：眭传洪
 * @Create：2023/4/29 21:53
 * @Meet: 2022/02/25
 * @Start: 2022/6/24
 */
public interface FanService extends IService<Fan> {

    Result followUser(String myId, String vlogerId);

    Result queryDoIFollowVloger(String myId, String vlogerId);

    Result cancelFollow(String myId, String vlogerId);

    Result getMyFollows(String myId, int page, int pageSize);

    Result getMyFans(String myId, int page, int pageSize);
}
