package cn.schff.dyvlog.controller;

import cn.schff.dyvlog.common.util.Result;
import cn.schff.dyvlog.service.MessageService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Author：眭传洪
 * @Create：2023/6/10 20:36
 * @Meet: 2022/02/25
 * @Start: 2022/6/24
 */
@RestController
@RequestMapping("/msg")
@Api(tags = "系统消息模块")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping("/list")
    public Result getMsgList(@RequestParam("userId") String userId,
                             @RequestParam("page") Long page,
                             @RequestParam("pageSize") int pageSize) {
        return messageService.getMsgList(userId, page, pageSize);
    }

}
