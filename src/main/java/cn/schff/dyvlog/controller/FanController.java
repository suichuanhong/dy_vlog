package cn.schff.dyvlog.controller;

import cn.schff.dyvlog.common.util.Result;
import cn.schff.dyvlog.service.FanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author：眭传洪
 * @Create：2023/4/29 22:25
 * @Meet: 2022/02/25
 * @Start: 2022/6/24
 */
@Api(tags = "用户关注模块")
@RestController
@RequestMapping("/fan")
public class FanController {

    @Autowired
    private FanService fanService;

    @ApiOperation("关注用户")
    @PostMapping("/follow")
    public Result followUser(@RequestParam("myId") String myId, @RequestParam("vlogerId") String vlogerId) {
        return fanService.followUser(myId, vlogerId);
    }

    @ApiOperation("查询用户是否关注用户")
    @GetMapping("/queryDoIFollowVloger")
    public Result queryDoIFollowVloger(@RequestParam("myId") String myId, @RequestParam("vlogerId") String vlogerId) {
        return fanService.queryDoIFollowVloger(myId, vlogerId);
    }

    @ApiOperation("取消关注用户")
    @PostMapping("/cancel")
    public Result cancelFollow(@RequestParam("myId") String myId, @RequestParam("vlogerId") String vlogerId) {
       return fanService.cancelFollow(myId, vlogerId);
    }

    @ApiOperation("查询我的关注列表")
    @GetMapping("/queryMyFollows")
    public Result queryMyFollows(@RequestParam("myId") String myId,
                                 @RequestParam("page") int page,
                                 @RequestParam("pageSize") int pageSize) {
        return fanService.getMyFollows(myId, page, pageSize);
    }

    @ApiOperation("查询我的粉丝列表")
    @GetMapping("/queryMyFans")
    public Result queryMyFans(@RequestParam("myId") String myId,
                              @RequestParam("page") int page,
                              @RequestParam("pageSize") int pageSize) {
        return fanService.getMyFans(myId, page, pageSize);
    }

}
