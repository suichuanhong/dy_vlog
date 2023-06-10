package cn.schff.dyvlog.common.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Author：眭传洪
 * @Create：2023/5/1 16:23
 * @Meet: 2022/02/25
 * @Start: 2022/6/24
 */
@Data
public class UserDTO{
    private static final long serialVersionUID = 1L;

    private String id;

    private String mobile;

    private String nickname;

    private String imoocNum;

    private String face;

    private Integer sex;

    private LocalDate birthday;

    private String country;

    private String province;

    private String city;

    private String district;

    private String description;

    private String bgImg;

    private Integer canImoocNumBeUpdated;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;

    private String userToken;

    // 我的关注总数
    private Long myFollowsCounts;

    // 我的粉丝总数
    private Long myFansCounts;

    // 我的获赞总数
    private Long totalLikeMeCounts;
}
