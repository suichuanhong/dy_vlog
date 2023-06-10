package cn.schff.dyvlog.common.dto;

import cn.schff.dyvlog.pojo.MessageMO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author：眭传洪
 * @Create：2023/6/10 21:21
 * @Meet: 2022/02/25
 * @Start: 2022/6/24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {

    private List<MessageMO> msgList;

    private int length;

}
