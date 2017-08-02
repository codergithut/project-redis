package com.common.model;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;


/**
 * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
 * @version 1.0, 2017/3/23
 * @description 文件消息
 */
public class FileMessage implements Serializable{
    //文件名称
    String fileName;
    //文件二进制文件
    byte[] content;
    //文件类型
    String type;
    //文件编码
    String encode;
    //文件存放路径
    String path;
    //事件编号
    String bizMsgId;
    //处理结果
    String result;
    //业务类型
    String rectype;
    //不动产单元号
    String estate_num;
    //业务对应的地区编号
    String areaCode;
    //数字签名
    String sign;

    public FileMessage(String fileName, String type) {
        this.fileName=fileName;
        this.content=content;
        this.type=type;
    }

    public FileMessage(){

    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getContent() {
        return content;
    }

    public String getType() {
        return type;
    }

    public String getPath() {
        return path;
    }

    public String getEncode() {

        return encode;
    }

    public String getResult() {
        return result;
    }

    public String getBizMsgId() {
        return bizMsgId;
    }

    public String getRectype() {
        return rectype;
    }

    public void setEncode(String encode) {
        this.encode = encode;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setBizMsgId(String bizMsgId) {
        this.bizMsgId = bizMsgId;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setRectype(String rectype) {
        this.rectype = rectype;
    }

    public String getEstate_num() {
        return estate_num;
    }

    public void setEstate_num(String estate_num) {
        this.estate_num = estate_num;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String toString() {
        try {
            if(encode != null) {
                return new String(content, encode);
            } else {
                return new String(content, "UTF-8");
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
