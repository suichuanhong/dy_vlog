package cn.schff.dyvlog.pojo;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("my_liked_vlog")
public class MyLikedVlog implements Serializable {

    private static final long serialVersionUID = 1L;

      private String id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 喜欢的短视频id
     */
    private String vlogId;


}
