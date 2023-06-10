package cn.schff.dyvlog.mapper;

import cn.schff.dyvlog.common.dto.CommentDTO;
import cn.schff.dyvlog.pojo.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author：眭传洪
 * @Create：2023/4/29 21:56
 * @Meet: 2022/02/25
 * @Start: 2022/6/24
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    List<CommentDTO> queryCommentsByVlogId(@Param("vlogId") String vlogId);

    @Delete("delete from comment where id = #{commentId} and comment_user_id = #{commentUserId}")
    boolean removeByCommentIdAndCommentUserId(@Param("commentId") String commentId, @Param("commentUserId") String commentUserId);

}
