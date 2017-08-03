package com.client.send.service;

import com.common.model.DocumentInfo;
import com.common.model.ResponseMessage;
import com.common.util.JaxbUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/3
 * @description
 */
@Service
public class SaveFinalMessage {

    /**
     * 最终的生成文件（包含响应文件）
     */
    private final static String XSD_FILES_FINAL = "files-final-hash";

    @Autowired
    @Qualifier("write")
    Jedis jedis;

    public void saveFinalMessage(ResponseMessage responseMessage, DocumentInfo documentInfo) throws UnsupportedEncodingException {

        Map<String,String> data = new HashMap<String,String>();

        String resp =  new String(JaxbUtil.convertToXml(responseMessage).getBytes("UTF-8"),"UTF-8");

        documentInfo.setReponseContent(resp);

        data.put(documentInfo.getFileName(), documentInfo.toString());

        jedis.hmset(XSD_FILES_FINAL, data);

    }

    public ResponseMessage getResPonseMessage(DocumentInfo docInfo, String type) {
        ResponseMessage responseMessage = new ResponseMessage();

        responseMessage.setBizMsgId(docInfo.getBizMsgId());

        responseMessage.setRecType(docInfo.getRecType());

        if("RSA".equals(type)) {

            responseMessage.setResponseCode("00001");

            responseMessage.setSuccessFlag("false");

            responseMessage.setResponseInfo("数字签名验证失败!");
        }

        if("DOUBLE".equals(type)) {

            responseMessage.setResponseCode("00003");

            responseMessage.setSuccessFlag("false");

            responseMessage.setResponseInfo("重复验证未通过!");
        }

        if("XSD".equals(type)) {

            responseMessage.setResponseCode("00002");

            responseMessage.setSuccessFlag("false");

            responseMessage.setResponseInfo("XSD验证失败!");
        }

        if("TRUE".equals(type)) {

            responseMessage.setResponseCode("00000");

            responseMessage.setSuccessFlag("true");

            responseMessage.setResponseInfo("数据已上传到服务器!");

        }
        return responseMessage;
    }

    public void saveMessage(DocumentInfo documentInfo, String type) throws UnsupportedEncodingException {
        saveFinalMessage(getResPonseMessage(documentInfo, type), documentInfo);
    }

    public void saveMessage(DocumentInfo documentInfo, ResponseMessage responseMessage) throws UnsupportedEncodingException {
        saveFinalMessage(responseMessage, documentInfo);
    }

}
