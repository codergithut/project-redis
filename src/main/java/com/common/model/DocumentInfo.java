package com.common.model;


import com.alibaba.fastjson.JSON;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/1
 * @description
 */
public class DocumentInfo {
    private String fileName;

    private String code;

    private String queueName;

    private String recType;

    private String bizMsgId;

    private String sign;

    private String requestContent;

    private String reponseContent;

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getRecType() {
        return recType;
    }

    public void setRecType(String recType) {
        this.recType = recType;
    }

    public String getBizMsgId() {
        return bizMsgId;
    }

    public void setBizMsgId(String bizMsgId) {
        this.bizMsgId = bizMsgId;
    }

    public String getRequestContent() {
        return requestContent;
    }

    public void setRequestContent(String requestContent) {
        this.requestContent = requestContent;
    }

    public String getReponseContent() {
        return reponseContent;
    }

    public void setReponseContent(String reponseContent) {
        this.reponseContent = reponseContent;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
