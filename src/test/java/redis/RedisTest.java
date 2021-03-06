package redis;


import com.common.util.FileUtil;
import com.common.util.XmlUtil;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.util.*;

/**
 * 数据均为key + file
 * key为数字签名
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/7/31
 * @description
 */
public class RedisTest {

    private Map<String, String> fileMessages = new HashMap<String, String>();

    @Before
    public void init() throws Exception {

        //todo 定时获取文件

        String xmlContent1 = FileUtil.getFileString(new File("E:\\wechart\\project-redis\\src\\main\\resources\\Biz320481161020001228.xml"));
        fileMessages.put(XmlUtil.getTextByXpath("/Message/Head/DigitalSign", xmlContent1), xmlContent1);

        String xmlContent3 = FileUtil.getFileString(new File("E:\\wechart\\project-redis\\src\\main\\resources\\Biz320684170518003619.xml"));
        fileMessages.put(XmlUtil.getTextByXpath("/Message/Head/DigitalSign", xmlContent3), xmlContent3);

    }

//    @Test
//    public void redisClientTest() {
//        //连接本地的 Redis 服务
//        Jedis jedis = new Jedis("192.168.50.210");
//        System.out.println("连接成功");
//        //查看服务是否运行
//        System.out.println("服务正在运行: "+jedis.ping());
//    }

    @Test
    public void redisStringJavaTest() {
        //连接本地的 Redis 服务
        Jedis jedis = new Jedis("192.168.50.210");
        System.out.println("连接成功");
        for(Map.Entry<String,String> entry : fileMessages.entrySet()){
            jedis.set(entry.getKey(), entry.getValue());
            System.out.println("redis 存储的字符串为: "+ jedis.get(entry.getKey()));
        }

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

        //jedis.publish("site-list", "helllo");

        jedis.pubsubChannels("site-list");


        // 获取存储的数据并输出
        List<String> list = jedis.lrange("site-list", 0 ,2);
        for(int i=0; i<list.size(); i++) {
            System.out.println("列表项为: "+list.get(i));
        }


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
