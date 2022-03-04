package cn.tzq0301.general;

import cn.tzq0301.general.config.RedisConfig;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ReactiveRedisTemplate;

/**
 * @author tzq0301
 * @version 1.0
 */
@SpringBootTest
public class TestApplication {
    @Autowired
    ReactiveRedisTemplate<String, Object> redisTemplate;

    @Test
    @Disabled
    void test() {
        redisTemplate.opsForSet().add(RedisConfig.ADDRESSES_KEY, "二基楼", "白石桥", "天府大道", "太古里", "春熙路", "339", "西园六舍").subscribe();
    }
}
