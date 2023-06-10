package cn.schff.dyvlog.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* @Author：眭传洪
* @Create：2023/5/14 16:31
* @Meet: 2022/02/25
* @Start: 2022/6/24
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowDTO {

    private String vlogerId;

    private String nickname;

    private String face;

    private boolean isFollowed = true;

}
