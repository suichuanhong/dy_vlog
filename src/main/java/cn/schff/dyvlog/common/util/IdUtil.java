package cn.schff.dyvlog.common.util;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static cn.schff.dyvlog.common.util.RedisConstant.ID_INCR_KEY;

/**
 * @Author：眭传洪
 * @Create：2023/4/30 22:59
 * @Meet: 2022/02/25
 * @Start: 2022/6/24
 */
@Component
public class IdUtil {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public Long getId(String prefix) {
        long current = System.currentTimeMillis() / 1000;
        String dateTime = DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDateTime.now());
        long start = 1672531200L;
        return (current - start) << 32 | stringRedisTemplate.opsForValue().increment(ID_INCR_KEY + prefix + ":" + dateTime);
    }

}
