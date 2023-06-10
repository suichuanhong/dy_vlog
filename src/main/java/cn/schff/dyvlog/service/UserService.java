package cn.schff.dyvlog.service;

import cn.schff.dyvlog.common.dto.LoginDTO;
import cn.schff.dyvlog.pojo.User;
import cn.schff.dyvlog.common.util.Result;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author：眭传洪
 * @Create：2023/4/29 21:53
 * @Meet: 2022/02/25
 * @Start: 2022/6/24
 */
public interface UserService extends IService<User> {
    Result sendCode(String mobile);

    Result login(LoginDTO loginDTO, BindingResult result);

    Result logout(String userId);

    Result queryById(String userId);

    Result modifyImage(String userId, Integer type, MultipartFile file);
}
