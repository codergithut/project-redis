package com.client.file.quartz;

import com.client.send.service.SaveFinalMessage;
import com.common.model.DocumentInfo;
import com.common.model.ResponseMessage;
import com.common.util.FileUtil;
import com.common.util.RSASignature;
import com.common.util.XmlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class ScheduledGetFileTasks {

    @Autowired
    @Qualifier("write")
    Jedis jedis;

    @Autowired
    SaveFinalMessage saveFinalMessage;

    static String PUBLIC_KEY="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDT9TsOfGqhv+3TxeU/jNwskOlXPF35Gffm4oGjUyC2ajplEiRZ+57x5wYXtqh1B3ulnSQY1PLO/Pw8i0jg1uwQoO0izGk31CrIloFpKAcve5JfGx0XBK1cffsGpFvjzL/gvRZHHvqGOH1BOyST8EcOrDnuT3Y3FtYbGdeDlH6AGQIDAQAB";


    /**
     * 需要确认的文件消息
     */
    private final static String WAITE_SEND_KEY = "files-wait-send-hash";





    /**
     * 包含已有的文件数字签名列表
     */
    private final static String FILES_SIGN_SET = "files-sign-set";


    /**
     * 临时xsd文件
     */
    private final static String XSD_FILES = "files-xsd";



    private static boolean xsd = false;


    public void getFileSchedule() throws Exception {
        System.out.println("this is sssss");
        getAllFiles("E:\\wechart\\project-redis\\src\\main\\resources\\xml");

    }

    private void initXSDResource() throws UnsupportedEncodingException {
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


    private void getAllFiles (String path) throws Exception {

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
            docInfo.setRequestContent(content);
            docInfos.put(sign, docInfo.toString());




            /**
             * 如何各项验证通过
             */
            if(checkDocByClient(content, docInfo)) {
                Map<String,String> doc = new HashMap<String,String>();
                doc.put(docInfo.getFileName(), docInfo.toString());
                jedis.hmset(WAITE_SEND_KEY, doc);
                continue;
            }

            Map<String,String> errorDoc = new HashMap<String,String>();

            errorDoc.put(docInfo.getFileName(), docInfo.toString());

            jedis.hmset(WAITE_SEND_KEY, errorDoc);

        }
    }

    private boolean checkDocByClient(String content, DocumentInfo docInfo) throws Exception {

        if (jedis.sismember(FILES_SIGN_SET, docInfo.getSign())) {

            saveFinalMessage.saveMessage(docInfo, "DOUBLE");

            return false;

        } else {

            jedis.sadd(FILES_SIGN_SET, docInfo.getSign());

        }

        /**
         * xsd  验证
         */
        String retype = XmlUtil.getTextByXpath("/Message/Head/RecType", content);

        if(!xsd) {
            initXSDResource();
        }

        String xsdString = jedis.hmget(XSD_FILES, new String[]{retype}).get(0);

        XmlUtil.XmlValidateResult result = XmlUtil.checkXmlByXsd(content, xsdString);

        if(!result.isValidated()) {
            saveFinalMessage.saveMessage(docInfo, "XSD");
            return false;
        }


        /**
         * 数字签名验证
         */

        boolean checkRSA = RSASignature.CheckXmlByPulKey(content, PUBLIC_KEY);

        if(!checkRSA) {

            saveFinalMessage.saveMessage(docInfo, "RSA");

            return false;
        }

        return true;
    }

    private String getFileByByte(File file) throws UnsupportedEncodingException {
        byte[] fileBytes = FileUtil.getBytes(file);
        return new String(fileBytes, "UTF-8");
    }






}

