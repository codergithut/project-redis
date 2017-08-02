package com.common.model;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
 * @version 1.0, 2017/3/23
 * @description 客户端发送消息封装
 */
public class MessageClient implements Serializable{

    //客户端消息队列的名称
    private String serverName;
    //状态
    private String status;
    //发送方地区编码
    private String form;

    private String password;

    //消息队列的名称
    private String queueName;
    //每次扫描将数据全部放到这边
    private List<FileMessage> fileMessages;

    //发送消息的时间
    private Date sendTime;

    //发送到省或者国家级服务器的名称
    private String queueToName;

    //JSON工具需要
    public MessageClient() {
    }

    public Date getSendTime() {
        return sendTime;
    }

    public List<FileMessage> getMessageContents() {
        return fileMessages;
    }

    public String getQueueName() {
        return queueName;
    }

    public String getServerName() {
        return serverName;
    }

    public String getStatus() {
        return status;
    }

    public String getForm() {
        return form;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public void setMessageContents(List<FileMessage> fileMessages) {
        this.fileMessages = fileMessages;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getQueueToName() {
        return queueToName;
    }

    public void setQueueToName(String queueToName) {
        this.queueToName = queueToName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
