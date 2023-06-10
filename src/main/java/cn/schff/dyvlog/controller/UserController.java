package cn.schff.dyvlog.controller;

import cn.schff.dyvlog.common.dto.LoginDTO;
import cn.schff.dyvlog.service.UserService;
import cn.schff.dyvlog.common.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

/**
 * @Author：眭传洪
 * @Create：2023/4/29 22:26
 * @Meet: 2022/02/25
 * @Start: 2022/6/24
 */
@Api(tags = "用户模块")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation("生成并发送验证码")
    @PostMapping("/code")
    public Result sendCode(@RequestParam("mobile") String mobile) {
        return userService.sendCode(mobile);
    }

    @ApiOperation("登录功能")
    @PostMapping("/login")
    public Result login(@Valid @RequestBody LoginDTO loginDTO, BindingResult result) {
        return userService.login(loginDTO, result);
    }

    @ApiOperation("退出登录")
    @PostMapping("/logout")
    public Result logout(@RequestParam("userId") String userId) {
        return userService.logout(userId);
    }

    @ApiOperation("用户查询")
    @GetMapping("/query")
    public Result queryById(@RequestParam("userId") String userId) {
        return userService.queryById(userId);
    }

    @ApiOperation("更新背景图或者头像")
    @PostMapping("/modifyImage")
    public Result modifyImage(@RequestParam("userId") String userId, @RequestParam("type") Integer type, MultipartFile file) {
        return userService.modifyImage(userId, type, file);
    }
}
