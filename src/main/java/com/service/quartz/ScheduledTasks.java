package com.service.quartz;

import com.service.model.DocumentInfo;
import com.service.model.ResponseMessage;
import com.util.FileUtil;
import com.util.JaxbUtil;
import com.util.RSASignature;
import com.util.XmlUtil;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.*;


/**
 * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
 * @version 1.0, 2017/2/13
 * @description 定时器启动到指定文件夹下获取数据并上传到消费接受中心
 */
@Component
@Configurable
public class ScheduledTasks{

    Jedis jedis;

    static String PUBLIC_KEY="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDT9TsOfGqhv+3TxeU/jNwskOlXPF35Gffm4oGjUyC2ajplEiRZ+57x5wYXtqh1B3ulnSQY1PLO/Pw8i0jg1uwQoO0izGk31CrIloFpKAcve5JfGx0XBK1cffsGpFvjzL/gvRZHHvqGOH1BOyST8EcOrDnuT3Y3FtYbGdeDlH6AGQIDAQAB";


    /**
     * 所有文件都存储
     */
    private final static String SAVE_FILES = "file-save";

    /**
     * 报文信息
     */
    private final static String FILES_INFO = "file-info-hash";


    private final static String RESPONSE_MESSAGE = "response-message-hash";

    /**
     * 所有xsd文件
     */
    private final static String XSD_FILES = "file-xsd";

    /**
     * 成功报文
     */
    private final static String SUCCESS_FILES = "success-key-set";

    /**
     * 失败报文存放
     */
    private final static String ERROR_FILES = "error-key-set";


    /**
     * 需要确认的消息标记
     */
    private final static String WAITE_KEY = "wait-key-set";

    private static boolean xsd = false;


    public void reportCurrentByCron() throws Exception {
        getAllFiles("E:\\wechart\\project-redis\\src\\main\\resources\\xml");

    }

    private void initXSDResource() throws UnsupportedEncodingException {
        jedis = new Jedis("192.168.50.210");
        List<File> files = FileUtil.getFiles("C:\\gtmap_server\\xsd");

        Map<String,String> xsdInfos = new HashMap<String,String>();

        for(File file : files) {
            String name = file.getName().split("\\.")[0];
            String content = getFileByByte(file);
            xsdInfos.put(name, content);
        }

        jedis.hmset(XSD_FILES, xsdInfos);

        xsd = true;
        jedis.close();
    }


    private List<File> getAllFiles (String path) throws Exception {
        jedis = new Jedis("192.168.50.210");
        List<File> files = FileUtil.getFiles(path);

        //连接本地的 Redis 服务
        Map<String,String> docInfos = new HashMap<String, String>();

        for(File file : files) {
            String content = getFileByByte(file);
            String sign = XmlUtil.getTextByXpath("/Message/Head/DigitalSign", content);
            String bizId = XmlUtil.getTextByXpath("/Message/Head/BizMsgID", content);
            DocumentInfo docInfo = new DocumentInfo();
            docInfo.setFileName(file.getName());
            docInfo.setQueueName("33333");
            docInfo.setSign(sign);
            docInfo.setBizMsgId(bizId);
            docInfos.put(sign, docInfo.toString());
            Set<String> sets = jedis.hkeys(FILES_INFO);

            if (sets.contains(sign)) {
                //todo 报文重复处理，这个比较特殊无需进行备份操作
                //continue;
            } else {
                //docInfos.put(sign,content);
            }

            /**
             * 如何各项验证通过
             */
            if(checkDocByClient(content, docInfo)) {
                jedis.sadd(WAITE_KEY, sign);
                jedis.publish("sendMessage", content);
            } else {
                jedis.sadd(ERROR_FILES, sign);
                jedis.publish("errorMessage", content);
            }


            /**
             * 将文件发到备份文件库中
             */
            jedis.sadd(SAVE_FILES, content);

            jedis.hmset(FILES_INFO, docInfos);

            jedis.close();
        }

        return files;
    }

    private boolean checkDocByClient(String content, DocumentInfo docInfo) throws Exception {

        /**
         * xsd  验证
         */
        String retype = XmlUtil.getTextByXpath("/Message/Head/RecType", content);

        String xsdString = jedis.hmget(XSD_FILES, new String[]{retype}).get(0);

        XmlUtil.XmlValidateResult result = XmlUtil.checkXmlByXsd(content, xsdString);

        if(!xsd) {
            initXSDResource();
        }

        if(!result.isValidated()) {
            saveErrorMessage(getResPonseMessage(docInfo, "XSD"), docInfo.getFileName());
        }


        /**
         * 数字签名验证
         */

        boolean checkRSA = RSASignature.CheckXmlByPulKey(content, PUBLIC_KEY);

        if(!checkRSA) {
            saveErrorMessage(getResPonseMessage(docInfo, "RSA"), docInfo.getFileName());
        }



        return false;
    }

    private String getFileByByte(File file) throws UnsupportedEncodingException {
        byte[] fileBytes = FileUtil.getBytes(file);
        return new String(fileBytes, "UTF-8");
    }

    private ResponseMessage getResPonseMessage(DocumentInfo docInfo, String type) {
        ResponseMessage responseMessage = new ResponseMessage();

        responseMessage.setBizMsgId(docInfo.getBizMsgId());

        responseMessage.setRecType(docInfo.getRecType());

        if("RSA".equals(type)) {

            responseMessage.setResponseCode("00001");

            responseMessage.setSuccessFlag("false");

            responseMessage.setResponseInfo("数字签名验证失败!");
        }

        if("XSD".equals(type)) {

            responseMessage.setResponseCode("00002");

            responseMessage.setSuccessFlag("false");

            responseMessage.setResponseInfo("XSD验证失败!");
        }

        return responseMessage;
    }


    private void saveErrorMessage(ResponseMessage responseMessage, String fileName) throws UnsupportedEncodingException {

        Map<String,String> data = new HashMap<String,String>();

        String resp =  new String(JaxbUtil.convertToXml(responseMessage).getBytes("UTF-8"),"UTF-8");

        data.put(fileName, resp);

        jedis.hmset(RESPONSE_MESSAGE, data);

    }


}

