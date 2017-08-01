package com.service.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
 * @version 1.0, 2017/3/30
 * @description 生成的标准xml响应报文
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class ResponseMessage implements Serializable{

    //实例编号
    @XmlElement
    private String bizMsgId;

    //是否成功
    @XmlElement
    private String successFlag;

    //相应消息
    @XmlElement
    private String responseInfo;

    //相应编码
    @XmlElement
    private String responseCode;

    //业务编码
    @XmlElement
    private String recType;

    //消息服务端名称
    @XmlElement
    private String serverName;

    public ResponseMessage() {
    }


    public String getBizMsgId() {
        return bizMsgId;
    }

    public String getSuccessFlag() {
        return successFlag;
    }

    public String getResponseInfo() {
        return responseInfo;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public String getRecType() {
        return recType;
    }

    public void setBizMsgId(String bizMsgId) {
        this.bizMsgId = bizMsgId;
    }

    public void setSuccessFlag(String successFlag) {
        this.successFlag = successFlag;
    }

    public void setResponseInfo(String responseInfo) {
        this.responseInfo = responseInfo;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public void setRecType(String recType) {
        this.recType = recType;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
}
