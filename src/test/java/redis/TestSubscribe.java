package redis;

import com.client.file.listen.RedisMsgJedisPubSub;
import org.junit.Test;
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
        RedisMsgJedisPubSub listener = new RedisMsgJedisPubSub();
        jedis.subscribe(listener, "site-list");
        //other code
    }
}