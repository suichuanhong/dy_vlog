package cn.schff.dyvlog.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @Author：眭传洪
 * @Create：2023/5/1 12:30
 * @Meet: 2022/02/25
 * @Start: 2022/6/24
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginDTO {

    @NotBlank(message = "手机号为空")
    @Length(min = 11, max = 11, message = "手机号格式不正确")
    private String mobile;

    @NotBlank(message = "验证码为空")
    private String code;

}
