package cn.schff.dyvlog.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @Author：眭传洪
 * @Create：2023/6/5 15:31
 * @Meet: 2022/02/25
 * @Start: 2022/6/24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document(collection = "system_message")
public class MessageMO {

    private Long id;

    @Field("fromUserId")
    private String fromUserId;

    @Field("fromUserNickName")
    private String fromUserNickName;

    @Field("fromUserFace")
    private String fromUserFace;

    @Field("toUserId")
    private String toUserId;

    @Field("content")
    private Map<String, Object> content;

    @Field("type")
    private Integer type;

    @Field("create_time")
    private LocalDateTime create_time;

}
