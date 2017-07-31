package src.test.redis;

import com.test.spring.sub.RedisMsgPubSubListener;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/7/31
 * @description
 */
public class TestSubscribe {
    @Test
    public void testSubscribe() throws Exception{
        Jedis jedis = new Jedis("192.168.50.210");
        RedisMsgPubSubListener listener = new RedisMsgPubSubListener();
        jedis.subscribe(listener, "redisChatTest");
        //other code
    }
}