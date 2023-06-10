package cn.schff.dyvlog.service;

import cn.schff.dyvlog.common.util.Result;
import cn.schff.dyvlog.pojo.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author：眭传洪
 * @Create：2023/4/29 21:53
 * @Meet: 2022/02/25
 * @Start: 2022/6/24
 */
public interface CommentService extends IService<Comment> {

    Result publishComment(Comment commentDTO);

    Result getVlogCommentCounts(String vlogId);

    Result getVlogCommentList(String vlogId, String userId, int page, int pageSize);

    Result deleteComment(String commentUserId, String commentId, String vlogId);

    Result likeComment(String userId, String commentId);

    Result unLikeComment(String userId, String commentId);
}
