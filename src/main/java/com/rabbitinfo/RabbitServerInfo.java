package com.rabbitinfo;

import com.alibaba.fastjson.JSON;
import com.common.model.RabbitmqQueueInfo;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

import static org.springframework.boot.Banner.Mode.LOG;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/3
 * @description
 */
public class RabbitServerInfo {
    /**
     * 查看ip为127.0.0.1 的vost为/ 队列名称为100010001000 信息
     * http://127.0.0.1:15672/api/queues/%2F/100010001000
     * @param args
     */

    public static void main(String[] args) {

        String info = HttpClientUtils.getConnect("http://10.1.1.153:15672/api/queues/%2F/100010001000", "tianjian", "tianjian");

        RabbitmqQueueInfo rabbitmqQueueInfo = JSON.parseObject(info, RabbitmqQueueInfo.class);

        System.out.println(rabbitmqQueueInfo.getName());


    }

    public static class HttpClientUtils {


        private static final String AUTHENKEY = "Authorization";

        private static final String BASICKEY = "Basic ";

        public static  String  getConnect(String url,String username,String password) {

            CloseableHttpResponse response = null;

            CloseableHttpClient client = HttpClients.createDefault();

            HttpGet httpGet = new HttpGet(url);
            Base64 token = new Base64();
            String authenticationEncoding = token.encodeAsString(new String(username + ":" + password).getBytes());

            httpGet.setHeader(AUTHENKEY, BASICKEY + authenticationEncoding);

            String responseContent = "";
            try {
                response = client.execute(httpGet);

                HttpEntity entity = response.getEntity();

                responseContent = EntityUtils.toString(entity, "UTF-8");

            } catch (IOException e) {
            } finally {
                if (response != null) {
                    try {
                        response.close();
                    } catch (IOException e) {
                    }
                }

                if (client != null) {
                    try {
                        client.close();
                    } catch (IOException e) {
                    }
                }
            }

            return responseContent;
        }


    }

}
