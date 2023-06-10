package cn.schff.dyvlog.controller.interceptor;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static cn.schff.dyvlog.common.util.RedisConstant.LOGIN_USER_KEY;

/**
 * @Author：眭传洪
 * @Create：2023/4/30 22:43
 * @Meet: 2022/02/25
 * @Start: 2022/6/24
 */
public class LoginInterceptor implements HandlerInterceptor {

    private final StringRedisTemplate stringRedisTemplate;

    public LoginInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId = request.getHeader("headerUserId");
        System.out.println("userId = " + userId);
        String userToken = request.getHeader("headerUserToken");
        System.out.println("userToken = " + userToken);
        if (userId == null || userToken == null) {
            return false;
        }
        String key = LOGIN_USER_KEY + userId;
        String token = stringRedisTemplate.opsForValue().get(key);
        if(token == null) {
            return false;
        }
        return userToken.equals(token);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
