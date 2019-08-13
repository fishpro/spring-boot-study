package com.fishpro.xmldom4j.util;

import com.fishpro.xmldom4j.domain.*;
import com.thoughtworks.xstream.XStream;

import java.util.*;

public class XmlUtil {
    /**
     * xml转对象
     * */
    public static Object xml2Bean(Map<String, Class> clazzMap, String xml) {
        XStream xstream = new XStream();
        for (Iterator it = clazzMap.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, Class> m = (Map.Entry<String, Class>) it.next();
            xstream.alias(m.getKey(), m.getValue());
        }
        Object bean = xstream.fromXML(xml);
        return bean;
    }


    //测试 xsteam
    public static void main(String[] args) {
        XStream xstream = new XStream();
        xstream.alias("person", Person.class);//设置节点person的名称
        xstream.alias("phonenumber", PhoneNumber.class);
        Person joe = new Person("Joe", "Walnes");
        joe.setPhone(new PhoneNumber(123, "1234-456"));
        joe.setFax(new PhoneNumber(123, "9999-999"));
        String xml = xstream.toXML(joe);//对象转 xml
        System.out.println(xml);
        /** 输出简单示例xml
         * <person>
         *   <firstname>Joe</firstname>
         *   <lastname>Walnes</lastname>
         *   <phone>
         *     <code>123</code>
         *     <number>1234-456</number>
         *   </phone>
         *   <fax>
         *     <code>123</code>
         *     <number>9999-999</number>
         *   </fax>
         * </person>
         * */
        Person newJoe = (Person)xstream.fromXML(xml);//xml 转 对象


        System.out.println("1.多节点设置别名");
        Blog teamBlog = new Blog(new Author("Guilherme Silveira"));
        teamBlog.add(new Entry("first","My first blog entry."));
        teamBlog.add(new Entry("tutorial",
                "Today we have developed a nice alias tutorial. Tell your friends! NOW!"));
        //1.如果不设置别名呢
        System.out.println(xstream.toXML(teamBlog));
        /** 1.不设置别名输出
         * <com.fishpro.xmldom4j.domain.Blog>
         *   <writer>
         *     <name>Guilherme Silveira</name>
         *   </writer>
         *   <entries>
         *     <com.fishpro.xmldom4j.domain.Entry>
         *       <title>first</title>
         *       <description>My first blog entry.</description>
         *     </com.fishpro.xmldom4j.domain.Entry>
         *     <com.fishpro.xmldom4j.domain.Entry>
         *       <title>tutorial</title>
         *       <description>Today we have developed a nice alias tutorial. Tell your friends! NOW!</description>
         *     </com.fishpro.xmldom4j.domain.Entry>
         *   </entries>
         * </com.fishpro.xmldom4j.domain.Blog>
         * */
        //2.设置别名
        xstream.alias("blog", Blog.class);
        xstream.alias("entry", Entry.class);
        System.out.println(xstream.toXML(teamBlog));

        /** 2.设置别名
         * <blog>
         *   <writer>
         *     <name>Guilherme Silveira</name>
         *   </writer>
         *   <entries>
         *     <entry>
         *       <title>first</title>
         *       <description>My first blog entry.</description>
         *     </entry>
         *     <entry>
         *       <title>tutorial</title>
         *       <description>Today we have developed a nice alias tutorial. Tell your friends! NOW!</description>
         *     </entry>
         *   </entries>
         * </blog>
         * */

        //3.writer 节点 转为 author节点
        System.out.println("2.writer 节点 转为 author节点");
        xstream.useAttributeFor(Blog.class, "writer");
        xstream.aliasField("author", Blog.class, "writer");
        System.out.println(xstream.toXML(teamBlog));

        xstream.addImplicitCollection(Blog.class, "entries");
        System.out.println(xstream.toXML(teamBlog));

        /** 3. author 替代了 write
         * <blog>
         *   <author>
         *     <name>Guilherme Silveira</name>
         *   </author>
         *   <entry>
         *     <title>first</title>
         *     <description>My first blog entry.</description>
         *   </entry>
         *   <entry>
         *     <title>tutorial</title>
         *     <description>Today we have developed a nice alias tutorial. Tell your friends! NOW!</description>
         *   </entry>
         * </blog>
         * */


        //4.writer 作为 blog 的属性
        System.out.println("4.作为blog的属性");
        xstream.useAttributeFor(Blog.class, "writer");
        xstream.registerConverter(new AuthorConverter());//作为blog的属性
        System.out.println(xstream.toXML(teamBlog));
        /** 4.writer 作为 blog 的属性
         * <blog author="Guilherme Silveira">
         *   <entry>
         *     <title>first</title>
         *     <description>My first blog entry.</description>
         *   </entry>
         *   <entry>
         *     <title>tutorial</title>
         *     <description>Today we have developed a nice alias tutorial. Tell your friends! NOW!</description>
         *   </entry>
         * </blog>
         * */

        System.out.println("5.使用注解");
        //5.使用注解
        xstream.processAnnotations(User.class);
        User msg = new User(1, "fishpro");
        System.out.println(xstream.toXML(msg));
        /** 使用注解 @XStreamAlias("user")
         * <user>
         *   <id>1</id>
         *   <username>fishpro</username>
         * </user>
         * */

        //6.使用注解 子节点是列表
        List<Address> addressList=new ArrayList<>();
        addressList.add(new Address("江苏省南京市玄武大道1000号","201001","1801989098"));
        addressList.add(new Address("江苏省南京市玄武大道1001号","201001","1811989098"));
        msg = new User(1, "fishpro",addressList);
        System.out.println(xstream.toXML(msg));
        /** 6.使用注解 子节点是列表
         * <user>
         *   <id>1</id>
         *   <username>fishpro</username>
         *   <address>
         *     <street>江苏省南京市玄武大道1000号</street>
         *     <zipcode>201001</zipcode>
         *     <mobile>1801989098</mobile>
         *   </address>
         *   <address>
         *     <street>江苏省南京市玄武大道1001号</street>
         *     <zipcode>201001</zipcode>
         *     <mobile>1811989098</mobile>
         *   </address>
         * </user>
         * */


    }
}
