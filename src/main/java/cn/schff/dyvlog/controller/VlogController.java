package cn.schff.dyvlog.controller;

import cn.schff.dyvlog.pojo.Vlog;
import cn.schff.dyvlog.service.VlogService;
import cn.schff.dyvlog.common.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static cn.schff.dyvlog.common.util.SystemConstant.IS_PUBLIC;

/**
 * @Author：眭传洪
 * @Create：2023/4/29 22:25
 * @Meet: 2022/02/25
 * @Start: 2022/6/24
 */
@Api(tags = "短视频模块")
@RestController
@RequestMapping("/vlog")
public class VlogController {

    @Autowired
    private VlogService vlogService;

    @ApiOperation("视频信息入库")
    @PostMapping("/publish")
    public Result publishVlog(@RequestBody Vlog vlog) {
        return vlogService.publish(vlog);
    }

    @ApiOperation("分页视频列表")
    @GetMapping("/vlogList")
    public Result getVlogList(@RequestParam(value = "userId", defaultValue = "") String userId,
                              @RequestParam(value = "search", required = false) String search,
                              @RequestParam("page") Integer page,
                              @RequestParam("pageSize") Integer pageSize) {
        return vlogService.getVlogList(userId, search, page, pageSize);
    }

    @ApiOperation("视频详情查询")
    @GetMapping("/detail")
    public Result getVlogDetail(@RequestParam(value = "userId", defaultValue = "") String userId,
                                @RequestParam("vlogId") String vlogId) {
        return vlogService.getVlogDetail(userId, vlogId);
    }

    @ApiOperation("视频修改为public")
    @PostMapping("/changeToPublic")
    public Result changeToPublic(@RequestParam("userId") String userId, @RequestParam("vlogId") String vlogId) {
        return vlogService.changeVlogToPublicOrPrivate(userId, vlogId, IS_PUBLIC);
    }

    @ApiOperation("视频修改为private")
    @PostMapping("/changeToPrivate")
    public Result changeToPrivate(@RequestParam("userId") String userId, @RequestParam("vlogId") String vlogId) {
        return vlogService.changeVlogToPublicOrPrivate(userId, vlogId, !IS_PUBLIC);
    }

    @ApiOperation("查询我的public视频列表")
    @GetMapping("/myPublicList")
    public Result getPublicVlogList(@RequestParam("userId") String userId,
                                    @RequestParam("page") int page,
                                    @RequestParam("pageSize") int pageSize) {
        return vlogService.getMyVlogList(userId, page, pageSize, IS_PUBLIC);
    }

    @ApiOperation("查询我的private视频列表")
    @GetMapping("/myPrivateList")
    public Result getPrivateVlogList(@RequestParam("userId") String userId,
                                     @RequestParam("page") int page,
                                     @RequestParam("pageSize") int pageSize) {
        return vlogService.getMyVlogList(userId, page, pageSize, !IS_PUBLIC);
    }

    @ApiOperation("查询我的点赞视频")
    @GetMapping("/myLikedList")
    public Result getMyLikedList(@RequestParam("userId") String userId,
                                 @RequestParam("page") int page,
                                 @RequestParam("pageSize") int pageSize) {
        return vlogService.getMyLikedList(userId, page, pageSize);
    }

    @ApiOperation("点赞视频")
    @PostMapping("/like")
    public Result vlogLike(@RequestParam("userId") String userId,
                           @RequestParam("vlogerId") String vlogerId,
                           @RequestParam("vlogId") String vlogId) {
        return vlogService.vlogLike(userId, vlogerId, vlogId);
    }

    @ApiOperation("取消点赞视频")
    @PostMapping("/unlike")
    public Result vlogUnLike(@RequestParam("userId") String userId,
                             @RequestParam("vlogerId") String vlogerId,
                             @RequestParam("vlogId") String vlogId) {
        return vlogService.vlogUnLike(userId, vlogerId, vlogId);
    }

    @ApiOperation("查询视频点赞量")
    @PostMapping("/totalLikedCounts")
    public Result totalLikedCounts(@RequestParam("vlogId") String vlogId) {
        return vlogService.getVlogTotalLikeCounts(vlogId);
    }

    @ApiOperation("查询我的关注视频列表")
    @GetMapping("/followList")
    public Result getFollowList(@RequestParam("myId") String myId,
                                @RequestParam("page") int page,
                                @RequestParam("pageSize") int pageSize) {
        return vlogService.getFollowList(myId, page, pageSize);
    }

    @ApiOperation("查询朋友发布的视频列表")
    @GetMapping("/friendList")
    public Result getFriendList(@RequestParam("myId") String myId,
                                @RequestParam("page") int page,
                                @RequestParam("pageSize") int pageSize) {
        return vlogService.getFriendList(myId, page, pageSize);
    }

}
