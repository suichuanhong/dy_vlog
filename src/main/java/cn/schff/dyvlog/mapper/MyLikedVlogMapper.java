package cn.schff.dyvlog.mapper;

import cn.schff.dyvlog.pojo.MyLikedVlog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author：眭传洪
 * @Create：2023/4/29 21:57
 * @Meet: 2022/02/25
 * @Start: 2022/6/24
 */
@Mapper
public interface MyLikedVlogMapper extends BaseMapper<MyLikedVlog> {

    @Delete("delete from my_liked_vlog where user_id = #{userId} and vlog_id = #{vlogId}")
    boolean deleteByUserIdAndVlogId(@Param("userId") String userId, @Param("vlogId") String vlogId);

}
