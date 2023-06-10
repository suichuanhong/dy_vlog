package cn.schff.dyvlog.mapper;

import cn.schff.dyvlog.common.dto.FanDTO;
import cn.schff.dyvlog.common.dto.FollowDTO;
import cn.schff.dyvlog.pojo.Fan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author：眭传洪
 * @Create：2023/4/29 21:57
 * @Meet: 2022/02/25
 * @Start: 2022/6/24
 */
@Mapper
public interface FanMapper extends BaseMapper<Fan> {

    @Delete("delete from fan where fan_id = #{myId} and vloger_id = #{vlogerId}")
    Integer deleteByMyIdAndVlogerId(@Param("myId") String myId, @Param("vlogerId") String vlogerId);

    List<FollowDTO> getFollowUsers(@Param("myId") String myId);

    List<FanDTO> getFans(@Param("myId") String myId);

}
