package redis;

import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/7/31
 * @description
 */
public class TestPublish {
    @Test
    public void testPublish() throws Exception{
        Jedis jedis = new Jedis("192.168.50.210");
        jedis.publish("redisChatTest", "我是天才");
        Thread.sleep(5000);
        jedis.publish("redisChatTest", "我牛逼");
        Thread.sleep(5000);
        jedis.publish("redisChatTest", "哈哈");
    }
}
