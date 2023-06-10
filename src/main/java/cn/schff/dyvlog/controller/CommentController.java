package cn.schff.dyvlog.controller;

import cn.schff.dyvlog.common.dto.CommentDTO;
import cn.schff.dyvlog.common.util.Result;
import cn.schff.dyvlog.pojo.Comment;
import cn.schff.dyvlog.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.RegEx;

/**
 * @Author：眭传洪
 * @Create：2023/4/29 22:26
 * @Meet: 2022/02/25
 * @Start: 2022/6/24
 */
@Api(tags = "评论模块")
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ApiOperation("添加评论")
    @PostMapping("/create")
    public Result publishComment(@RequestBody Comment comment) {
        return commentService.publishComment(comment);
    }

    @ApiOperation("查询视频的评论总数")
    @GetMapping("/counts")
    public Result getVlogCommentCounts(@RequestParam("vlogId") String vlogId) {
        return commentService.getVlogCommentCounts(vlogId);
    }

    @ApiOperation("视频评论列表查询")
    @GetMapping("/list")
    public Result getVlogCommentList(@RequestParam("vlogId") String vlogId,
                                     @RequestParam("userId") String userId,
                                     @RequestParam("page") int page,
                                     @RequestParam("pageSize") int pageSize) {
        return commentService.getVlogCommentList(vlogId, userId, page, pageSize);
    }

    @ApiOperation("删除评论")
    @DeleteMapping("/delete")
    public Result deleteComment(@RequestParam("commentUserId") String commentUserId,
                                @RequestParam("commentId") String commentId,
                                @RequestParam("vlogId") String vlogId) {
        return commentService.deleteComment(commentUserId, commentId, vlogId);
    }

    @ApiOperation("评论点赞")
    @PostMapping("/like")
    public Result likeComment(@RequestParam("userId") String userId,
                              @RequestParam("commentId") String commentId) {
        return commentService.likeComment(userId, commentId);
    }

    @ApiOperation("取消评论点赞")
    @PostMapping("/unlike")
    public Result unLikeComment(@RequestParam("userId") String userId,
                                @RequestParam("commentId") String commentId) {
        return commentService.unLikeComment(userId, commentId);
    }

}
