package com.fishpro.xmldom4j;

import com.fishpro.xmldom4j.util.Dom4jUtils;
import org.dom4j.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileWriter;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

@SpringBootApplication
public class Xmldom4jApplication {

    public static void main(String[] args) {
        SpringApplication.run(Xmldom4jApplication.class, args);
        //测试 http://feed.cnblogs.com/blog/u/312210/rss
        try {
            Document document = Dom4jUtils.parse(new URL("http://feed.cnblogs.com/blog/u/312210/rss"));
            System.out.println("根节点："+document.getRootElement().getName());

            System.out.println("====遍历================================");
            //获取根节点
            Element root = document.getRootElement();

            // 遍历根节点下的子节点
            for (Iterator<Element> it = root.elementIterator(); it.hasNext();) {
                Element element = it.next();
                // do something
                System.out.println("根节下子节点名称："+element.getName());
            }

            // 遍历子节点 author 下的子节点
            for (Iterator<Element> it = root.elementIterator("feed"); it.hasNext();) {
                Element foo = it.next();
                // do something
                System.out.println("author节点下节点名称："+foo.getName());
            }
            // 后去根节点的属性
            for (Iterator<Attribute> it = root.attributeIterator(); it.hasNext();) {
                Attribute attribute = it.next();
                // do something
                System.out.println("根节下子节点属性："+attribute.getName());
            }

            System.out.println("================================================");
            //使用 XPath 获取节点 获取多个节点
            List<Node> list = document.selectNodes("//feed/entry");
            for (Node node :list
                 ) {

                System.out.println("list node："+node.getName());
            }
            Node node = document.selectSingleNode("//feed");

            System.out.println("node："+node.getName());
            //写到文件里面
            Document document1=Dom4jUtils.createDocument();
            FileWriter out = new FileWriter("foo.xml");
            document1.write(out);
            out.close();
            //
            String text=document1.asXML();

            Document xd=DocumentHelper.parseText(text);
            System.out.println("node："+node.getName());


        }catch (DocumentException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
