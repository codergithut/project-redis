package com.common.model;

import java.io.Serializable;

/**
 * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
 * @version 1.0, 2017/3/23
 * @description 服务端发送消息封装
 */
public class MessageServer implements Serializable{

    //客户端数据存放地址
    private String path;
    //是否成功插入
    private boolean result;
    //回馈错误消息
    private String backInfo;
    //客户端名称 可以删除
    private String serverName;

    private String queueToName;

    private ResponseMessage responseMessage;

    public MessageServer() {
    }

    public String getPath() {
        return path;
    }

    public boolean isResult() {
        return result;
    }

    public String getBackInfo() {
        return backInfo;
    }

    public String getServerName() {
        return serverName;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public void setBackInfo(String backInfo) {
        this.backInfo = backInfo;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public ResponseMessage getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(ResponseMessage responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getQueueToName() {
        return queueToName;
    }

    public void setQueueToName(String queueToName) {
        this.queueToName = queueToName;
    }
}
