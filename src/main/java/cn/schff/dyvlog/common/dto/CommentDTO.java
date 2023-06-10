package cn.schff.dyvlog.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author：眭传洪
 * @Create：2023/5/21 21:31
 * @Meet: 2022/02/25
 * @Start: 2022/6/24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {

    // 评论id
    private String commentId;

    // 评论用户的id
    private String commentUserId;

    // 评论用户头像
    private String commentUserFace;

    // 评论用户昵称
    private String commentUserNickname;

    // 回复用户的昵称
    private String replyedUserNickname;

    // 评论时间
    private LocalDateTime createTime;

    // 评论内容
    private String content;

    // 点赞个数
    private int likeCounts;

    // 视频发布者Id
    private String vlogerId;

    // 父级评论的id
    private String fatherCommentId;

    // 视频id
    private String vlogId;

    // 是否点赞
    private int isLike = 0;

}
