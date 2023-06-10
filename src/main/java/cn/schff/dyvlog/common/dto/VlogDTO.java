package cn.schff.dyvlog.common.dto;

import lombok.Data;

/**
 * @Author：眭传洪
 * @Create：2023/5/7 15:20
 * @Meet: 2022/02/25
 * @Start: 2022/6/24
 */
@Data
public class VlogDTO {

    private String vlogId;

    private String vlogerId;

    private String vlogerFace;

    private String vlogerName;

    private String content;

    private String url;

    private String cover;

    private Integer width;

    private Integer height;

    private Integer likeCounts;

    private Integer commentsCounts;

    private Integer isPrivate;

    private boolean isPlay = false;

    private boolean doIFollowVloger = false;

    private boolean doILikeThisVlog = false;

}
