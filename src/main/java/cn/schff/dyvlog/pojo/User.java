package cn.schff.dyvlog.pojo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 昵称，媒体号
     */
    private String nickname;

    /**
     * 慕课号，类似头条号，抖音号，公众号，唯一标识，需要限制修改次数，比如终生1次，每年1次，每半年1次等，可以用于付费修改。
     */
    private String imoocNum;

    /**
     * 头像
     */
    private String face;

    /**
     * 性别 1:男  0:女  2:保密
     */
    private Integer sex;

    /**
     * 生日
     */
    private LocalDate birthday;

    /**
     * 国家
     */
    private String country;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区县
     */
    private String district;

    /**
     * 简介
     */
    private String description;

    /**
     * 个人介绍的背景图
     */
    private String bgImg;

    /**
     * 慕课号能否被修改，1：默认，可以修改；0，无法修改
     */
    private Integer canImoocNumBeUpdated;

    /**
     * 创建时间 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 更新时间 更新时间
     */
    private LocalDateTime updatedTime;


}
