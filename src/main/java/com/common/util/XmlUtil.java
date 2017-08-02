package com.common.util;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.xml.sax.SAXException;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/1
 * @description
 */
public class XmlUtil {

    public static String getTextByXpath(String xpath, String xml)
            throws Exception
    {
        Document document = DocumentHelper.parseText(xml);
        Node destNode = document.selectSingleNode(xpath);
        if (destNode == null)
        {
            throw new Exception(
                    "XML文档中找不到匹配的路径/Message/Head/BizMsgID,无法解析文档.");
        }
        return destNode.getText();
    }

    /**
     *
     * @param xmlStr 需要验证的xml字符串
     * @param xsdStr 验证xml的xsd字符串
     * @return 包含验证结果的对象
     */
    public static XmlValidateResult checkXmlByXsd(String xmlStr,String xsdStr){

        // 创建返回值类，默认为失败
        XmlValidateResult vs = new XmlValidateResult();

        if(xmlStr==null || xsdStr==null){
            return vs;
        }
        SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
// 包装待验证的xml字符串为Reader
        Reader xmlReader = new BufferedReader(new StringReader(xmlStr));
// 保障Schema xsd字符串为Reader
        Reader xsdReader = new BufferedReader(new StringReader(xsdStr));
        try {
// 构造Schema Source
            Source xsdSource = new StreamSource(xsdReader);
// 解析作为Schema的指定源并以Schema形式返回它
            Schema schema = factory.newSchema(xsdSource);
// 根据Schema检查xml文档的处理器,创建此 Schema的新 validator
            Validator validator = schema.newValidator();
// 构造待验证xml Source
            Source xmlSource = new StreamSource(xmlReader);
// 执行验证
            validator.validate(xmlSource);
// 设置验证通过
            vs.setValidated(true);
            return vs;
        } catch (SAXException ex) {
// 设置验证失败
            vs.setValidated(false);
// 设置验证失败信息
            vs.setErrorMsg(ex.getMessage());
            return vs;
        } catch (IOException e) {
// 设置验证失败
            vs.setValidated(false);
// 设置验证失败信息
            vs.setErrorMsg(e.getMessage());
            return vs;
        }
    }

    public static class XmlValidateResult {

        // 是否通过验证
        private boolean validated;

        // 错误信息
        private String errorMsg;

        // 构造函数，默认为不通过，错误原因为空字符串
        public XmlValidateResult() {
            validated = false;
            errorMsg = "";
        }

        public String getErrorMsg() {
            return errorMsg;
        }

        public void setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
        }

        public boolean isValidated() {
            return validated;
        }

        public void setValidated(boolean validated) {
            this.validated = validated;
        }

    }

}
