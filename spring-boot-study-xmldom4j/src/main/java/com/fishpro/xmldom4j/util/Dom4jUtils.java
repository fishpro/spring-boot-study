package com.fishpro.xmldom4j.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.DocumentResult;
import org.dom4j.io.DocumentSource;
import org.dom4j.io.SAXReader;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import java.net.URL;

public class Dom4jUtils {
    /**
     * 解析远程 XML 文件
     * @return Document xml 文档
     * */
    public static Document parse(URL url) throws DocumentException{
        SAXReader reader = new SAXReader();
        Document document =reader.read(url);
        return document;
    }

    /**
     * 创建一个 xml 文档
     * */
    public static Document createDocument() {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("root");

        Element author1 = root.addElement("author")
                .addAttribute("name", "James")
                .addAttribute("location", "UK")
                .addText("James Strachan");

        Element author2 = root.addElement("author")
                .addAttribute("name", "Bob")
                .addAttribute("location", "US")
                .addText("Bob McWhirter");

        return document;
    }

    /**
     * 使用XSLT转换XML
     * */
    public static Document styleDocument(Document document, String stylesheet) throws Exception {

        TransformerFactory factory = TransformerFactory.newInstance();//实例化转换器工厂 TransformerFactory
        Transformer transformer = factory.newTransformer(new StreamSource(stylesheet));// 创建一个转化格式对象

        DocumentSource source = new DocumentSource(document); // XML 源对象
        DocumentResult result = new DocumentResult(); //转换结果对象
        transformer.transform(source, result);//转换操作

        Document transformedDoc = result.getDocument();//获取转换后的文档
        return transformedDoc;
    }

}
