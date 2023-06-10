package cn.schff.dyvlog.mapper;

import cn.schff.dyvlog.common.dto.VlogDTO;
import cn.schff.dyvlog.pojo.Vlog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @Author：眭传洪
 * @Create：2023/4/29 21:59
 * @Meet: 2022/02/25
 * @Start: 2022/6/24
 */
@Mapper
public interface VlogMapper extends BaseMapper<Vlog> {

    List<VlogDTO> queryVlogList(@Param("search") String search);

    VlogDTO queryVlogDetailById(@Param("vlogId") String vlogId);

    List<Vlog> queryMyLikeList(@Param("userId") String userId);

    List<VlogDTO> queryListByIds(@Param("ids") Set<String> ids);
}
