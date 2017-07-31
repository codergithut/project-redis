package src.test.redis;

import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/7/31
 * @description
 */
public class RedisTest {
    @Test
    public void redisClientTest() {
        //连接本地的 Redis 服务
        Jedis jedis = new Jedis("192.168.50.210");
        System.out.println("连接成功");
        //查看服务是否运行
        System.out.println("服务正在运行: "+jedis.ping());
    }

    @Test
    public void redisStringJavaTest() {
        //连接本地的 Redis 服务
        Jedis jedis = new Jedis("192.168.50.210");
        System.out.println("连接成功");
        //设置 redis 字符串数据
        jedis.set("runoobkey", "www.runoob.com");
        // 获取存储的数据并输出
        System.out.println("redis 存储的字符串为: "+ jedis.get("runoobkey"));

    }

    @Test
    public void redisListJavaTest() {
        //连接本地的 Redis 服务
        Jedis jedis = new Jedis("192.168.50.210");
        System.out.println("连接成功");
        //存储数据到列表中
        jedis.lpush("site-list", "Runoob");
        jedis.lpush("site-list", "Google");
        jedis.lpush("site-list", "Taobao");

        jedis.publish("site-list", "helllo");


        // 获取存储的数据并输出
        List<String> list = jedis.lrange("site-list", 0 ,2);
        for(int i=0; i<list.size(); i++) {
            System.out.println("列表项为: "+list.get(i));
        }

        JedisPubSub jedisPubSub = new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                System.out.println("sdfsdfasdfasdfasdf:" + message);
                super.onMessage(channel, message);
            }
        };

        Jedis jedis1 = new Jedis("192.168.50.210");

        jedis1.subscribe(jedisPubSub, "chat");

    }

    @Test
    public void redisKeyJavaTest() {
        //连接本地的 Redis 服务
        Jedis jedis = new Jedis("192.168.50.210");
        System.out.println("连接成功");

        // 获取数据并输出
        Set<String> keys = jedis.keys("*");
        Iterator<String> it=keys.iterator() ;
        while(it.hasNext()){
            String key = it.next();
            System.out.println(key);
        }
    }
}
