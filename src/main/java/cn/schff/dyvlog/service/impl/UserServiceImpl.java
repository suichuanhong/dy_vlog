package cn.schff.dyvlog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.schff.dyvlog.common.exception.ParameterException;
import cn.schff.dyvlog.config.MinioConfig;
import cn.schff.dyvlog.common.dto.LoginDTO;
import cn.schff.dyvlog.common.dto.UserDTO;
import cn.schff.dyvlog.mapper.UserMapper;
import cn.schff.dyvlog.pojo.User;
import cn.schff.dyvlog.service.UserService;
import cn.schff.dyvlog.common.util.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static cn.schff.dyvlog.common.util.RedisConstant.*;
import static cn.schff.dyvlog.common.util.SystemConstant.*;

/**
 * @Author：眭传洪
 * @Create：2023/4/29 21:54
 * @Meet: 2022/02/25
 * @Start: 2022/6/24
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private MsgSendUtil msgSendUtil;

    @Autowired
    private IdUtil idUtil;

    @Resource
    private MinioConfig minioConfig;

    @Override
    public Result sendCode(String mobile) {
        if (StrUtil.isBlank(mobile)) {
            throw new ParameterException("phone参数值异常");
        }
        String key = LOGIN_CODE_KEY + mobile;
        // 生成随机验证码
        String code = RandomUtil.randomNumbers(CODE_LENGTH);
        // 保存到redis
        stringRedisTemplate.opsForValue().set(key, code, CODE_TIMEOUT, TimeUnit.MINUTES);
        // 发送验证码
        log.debug("手机号：{}, 验证码：{}", mobile, code);
        // 调用腾讯云发送短信
//        msgSendUtil.sendMsg(mobile, code);
        return Result.success();
    }

    @Override
    public Result login(LoginDTO loginDTO, BindingResult result) {
        // 参数异常直接返回
        if (result.hasErrors()) {
            Map<String, String> errors = getErrors(result);
            return Result.fail(errors);
        }
        String mobile = loginDTO.getMobile();
        String code = loginDTO.getCode();
        String cacheCode = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY + mobile);
        // 验证码错误直接返回
        if (!code.equals(cacheCode)) {
            return Result.fail("验证码错误");
        }
        // 如果不存在用户则创建用户并保存
        User u = query().eq("mobile", mobile).one();
        if (u == null) {
            u = createUser(mobile);
            save(u);
        }
        // 将token保存到redis并返回用户信息
        String userToken = UUID.randomUUID().toString(true);
        stringRedisTemplate.opsForValue().set(LOGIN_USER_KEY + u.getId(), userToken);
        UserDTO userDTO = BeanUtil.copyProperties(u, UserDTO.class);
        userDTO.setUserToken(userToken);
        return Result.success(userDTO);
    }

    @Override
    public Result logout(String userId) {
        // 退出登录直接删除redis中的token即可
        stringRedisTemplate.delete(LOGIN_USER_KEY + userId);
        return Result.success();
    }

    @Override
    public Result queryById(String userId) {
        User user = getById(userId);
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        // 关注数量
        Long followCount = stringRedisTemplate.opsForSet().size(USERINFO_QUERY_FOLLOW_KEY + userId);
        // 粉丝数
        Long fanCount = stringRedisTemplate.opsForSet().size(USERINFO_QUERY_FAN_KEY + userId);
        // 视频点赞
        String vlogLikeCountStr = stringRedisTemplate.opsForValue().get(USER_VLOG_LIKE_COUNT_KEY + userId);
        // 评论点赞
        String commentLikeCountStr = stringRedisTemplate.opsForValue().get(USER_COMMENT_LIKE_COUNT_KEY + userId);
        // 设置数据
        userDTO.setMyFollowsCounts(followCount);
        userDTO.setMyFansCounts(fanCount);
        long vlogLikeCount = 0L;
        long commentLikeCount = 0L;
        if (StrUtil.isNotBlank(vlogLikeCountStr)) {
            assert vlogLikeCountStr != null;
            vlogLikeCount = Long.parseLong(vlogLikeCountStr);
        }
        if (StrUtil.isNotBlank(commentLikeCountStr)) {
            assert commentLikeCountStr != null;
            commentLikeCount = Long.parseLong(commentLikeCountStr);
        }
        userDTO.setTotalLikeMeCounts(vlogLikeCount + commentLikeCount);
        return Result.success(userDTO);
    }

    @Override
    public Result modifyImage(String userId, Integer type, MultipartFile file) {
        if (!Objects.equals(type, BACK_IMG_TYPE) && !Objects.equals(type, FACE_IMAGE_TYPE)) {
            return Result.fail("文件上传失败");
        }
        InputStream inputStream = null;
        try {
            String fileName = file.getOriginalFilename();
            inputStream = file.getInputStream();
            // 上传文件
            MinIOUtils.uploadFile(minioConfig.getBucketName(), fileName, inputStream);
            String imgUrl = minioConfig.getEndPoint() + "/" + minioConfig.getBucketName() + "/" + fileName;
            // 更新数据库地址
            update().set(type.equals(BACK_IMG_TYPE) ? "bg_img" : "face", imgUrl).eq("id", userId).update();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return queryById(userId);
    }

    private Map<String, String> getErrors(BindingResult result) {
        Map<String, String> map = new HashMap<>();
        // 获取所有的异常信息
        List<FieldError> errors = result.getFieldErrors();
        for (FieldError field : errors) {
            String key = field.getField();
            String value = field.getDefaultMessage();
            map.put(key, value);
        }
        return map;
    }

    private User createUser(String mobile) {
        User user = new User();
        user.setId(idUtil.getId("user").toString());
        user.setMobile(mobile);
        user.setNickname(NICK_NAME + mobile);
        user.setImoocNum(NICK_NAME + mobile);
        user.setFace(DEFAULT_FACE);
        user.setSex(2);
        user.setBirthday(DEFAULT_DATE);
        user.setCountry("中国");
        user.setDescription("这个人很懒，什么都没留下");
        user.setCanImoocNumBeUpdated(1);
        user.setCreatedTime(LocalDateTime.now());
        user.setUpdatedTime(LocalDateTime.now());
        return user;
    }
}