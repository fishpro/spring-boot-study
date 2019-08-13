Xml 现在仍然占据着比较重要的地位，比如微信接口中使用了 Xml 进行消息的定义。本章重点讨论 Xml 的新建、编辑、查找、转化，可以这么理解，本章是使用了 dom4j、xstream 也是在开发者中使用最为广泛的 。 本章主要是位大家提供一个操作 Xml 的类库。

# 0 Dom4j XStream 简单介绍

一句话 Dom4j 专注于 Xml 操作的高性能库，Xstream 则专注于 对象之间的转换。

## Dom4j
Dom4j 为了支持 XPath、XML Schema、基于事件处理大文档或流文档。

Dom4j 为提供构建文档表示的选项，为可通过 Dom4j-API 和标准底层 dom-API 并行访问功能。

为实现上述宏伟目标，Dom4j 使用接口和抽象基本类方法并大量使用 JDK 中 Collections 类。

所以 Dom4j 有丰富的 API,在灵活性上面 Dom4j 更占有优势，性能方面也无可挑剔。

声名在外的 Sun-JAXM,大名鼎鼎的 Hibernate 中XML 配置文件解析都使用的是 Dom4j。

## XStream
XStream 为基于注解不需要其它辅助类或映射文件 的OXMapping 技术，如果你用过 hibernate 或 mybatis 之类的 ORM 框架就不难理解这里的 OXM。

XStream 可以将 JavaBean 序列化为 XML，或将 XML 反序列化为 JavaBean，使得XML序列化不再繁琐。

XStream 也可以将 JavaBean 序列化成 Json 或反序列化，使用非常方便。

没有映射文件而且底层使用 xmlpull 推模型解析 XML，高性能、低内存占用，结合简洁明了的 API，上手基本是分分钟的事情。

XStream 同时也可以定制转换类型策略并配有详细的错误诊断，能让你快速定位问题。


# 1 新建 Spring Boot Maven 示例工程项目
注意：是用来 IDEA 开发工具
1. File > New > Project，如下图选择 `Spring Initializr` 然后点击 【Next】下一步
2. 填写 `GroupId`（包名）、`Artifact`（项目名） 即可。点击 下一步
    groupId=com.fishpro   
    artifactId=xmldom4j
3. 选择依赖 `Spring Web Starter` 前面打钩。
4. 项目名设置为 `spring-boot-study-xmldom4j`.
   
# 2 引入依赖 Pom
- dom4j-1.6.1 支持 Java 1.4+
- dom4j-2.0.2 支持 Java 5+
- dom4j-2.1.1 支持 Java 8+

mvnrepository 只有 1.6.1 那就用1.6.1

```xml
<dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!-- https://mvnrepository.com/artifact/dom4j/dom4j -->
        <dependency>
            <groupId>dom4j</groupId>
            <artifactId>dom4j</artifactId>
            <version>1.6.1</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/jaxen/jaxen xpath解析的时候需要引入 jaxen包 否则会报错 java.lang.NoClassDefFoundError: org/jaxen/JaxenException-->
        <dependency>
            <groupId>jaxen</groupId>
            <artifactId>jaxen</artifactId>
            <version>1.1.6</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/com.thoughtworks.xstream/xstream 支持xml转bean -->
        <dependency>
            <groupId>com.thoughtworks.xstream</groupId>
            <artifactId>xstream</artifactId>
            <version>1.4.11.1</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

# 3 Dom4j 代码实例

## 3.1 打开一个远程 xml

```java
 /**
     * 解析远程 XML 文件
     * @return Document xml 文档
     * */
    public static Document parse(URL url) throws DocumentException{
        SAXReader reader = new SAXReader();
        Document document =reader.read(url);
        return document;
    }
```


## 3.2 创建一个 xml 文档

```java
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
```

## 3.3 遍历

```java
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
```

## 3.4 使用 xpath 获取节点

```java
System.out.println("================================================");
            //使用 XPath 获取节点 获取多个节点
            List<Node> list = document.selectNodes("//feed/entry");
            for (Node node :list
                 ) {

                System.out.println("list node："+node.getName());
            }
            Node node = document.selectSingleNode("//feed");

            System.out.println("node："+node.getName());

```

## 3.5 保存到 文件

```java
//写到文件里面
            Document document1=Dom4jUtils.createDocument();
            FileWriter out = new FileWriter("foo.xml");
            document1.write(out);
            out.close();
```


## 3.6 XML 文件转文本
```java
 Document xd=DocumentHelper.parseText(text);
```


## 3.7 文本转 XML 文档对象
```java
 String text = "<person> <name>James</name> </person>";
Document document = DocumentHelper.parseText(text);
```

## 3.8  使用 XSLT 转换 XML

```java
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
```
# 4 XStream 代码实例

## 4.1 不使用别名直接输出 xml
 Blog.java (路径 src/main/java/com/fishpro/xmldom4j/domain/Blog.java)
```java
public class Blog {
    private Author writer;
    private List entries = new ArrayList();

    public Blog(Author writer) {
        this.writer = writer;
    }

    public void add(Entry entry) {
        entries.add(entry);
    }

    public List getContent() {
        return entries;
    }
}
```

部分代码
```java
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
```
 
## 4.2 使用别名直接输出 xml

注意使用了上面代码的 Blog teamBlog  的定义。**注释部分为输出效果**

```java
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
```

## 4.3 替换节点名称

注意使用了上面代码的 Blog teamBlog  的定义。**注释部分为输出效果**

```java
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
```



## 4.4 实体对象属性作为节点的属性

注意使用了上面代码的 Blog teamBlog  的定义。**注释部分为输出效果**

```java
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
```

## 4.5 使用注解 @XStreamAlias

@XStreamAlias 可以应用到类似，也可以应用到实体对象 Bean 的属性名上

本示例中使用了 User 和 Address 实体,他们的关系是 User 可以拥有多个 Address

User (路径 src/main/java/com/fishpro/xmldom4j/domain/User.java)
```java
@XStreamAlias("user")
public class User {
    @XStreamAlias("id")
    private Integer userId;

    @XStreamAlias("username")
    private String username;

    @XStreamImplicit
    private List<Address> addresses;

    public User(Integer userId,String username){
        this.userId=userId;
        this.username=username;
    }
    public User(Integer userId,String username,List<Address> addresses){
        this.userId=userId;
        this.username=username;
        this.addresses=addresses;
    }
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
```

Address (路径 src/main/java/com/fishpro/xmldom4j/domain/Address.java)
```java
@XStreamAlias("address")
public class Address {
    private String street;
    private String zipcode;
    private String mobile;

    public Address(String street,String zipcode,String mobile){
        this.street=street;
        this.zipcode=zipcode;
        this.mobile=mobile;
    }
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
```

```java
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
```

## 4.6 使用注解 @XStreamImplicit

注意使用了上面代码的 Blog teamBlog  的定义。**注释部分为输出效果**
```java
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
```

## 4.9 属性转换器
当我们遇到日期的是，可能需要转换成想要的格式，我们给 User 对象增加 created 属性
```java 
    private Calendar created = new GregorianCalendar();
```
那么上面的示例就会输出，注意以下多出了created节点
```xml
<user>
  <id>1</id>
  <username>fishpro</username>
  <address>
    <street>江苏省南京市玄武大道1000号</street>
    <zipcode>201001</zipcode>
    <mobile>1801989098</mobile>
  </address>
  <address>
    <street>江苏省南京市玄武大道1001号</street>
    <zipcode>201001</zipcode>
    <mobile>1811989098</mobile>
  </address>
  <created>
    <time>1565691626712</time>
    <timezone>Asia/Shanghai</timezone>
  </created>
</user>
```

接下来我们新建一个转换器类
SingleValueCalendarConverter (路径 src/main/java/com/fishpro/xmldom4j/util/SingleValueCalendarConverter.java)
```java
/**
 * 日期转换器
 * */
public class SingleValueCalendarConverter implements Converter {

    public void marshal(Object source, HierarchicalStreamWriter writer,
                        MarshallingContext context) {
        Calendar calendar = (Calendar) source;
        writer.setValue(String.valueOf(calendar.getTime().getTime()));
    }

    public Object unmarshal(HierarchicalStreamReader reader,
                            UnmarshallingContext context) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date(Long.parseLong(reader.getValue())));
        return calendar;
    }

    public boolean canConvert(Class type) {
        return type.equals(GregorianCalendar.class);
    }
}
```

在运行 main 示例，如下 created 节点变化了
```xml
<user>
  <id>1</id>
  <username>fishpro</username>
  <address>
    <street>江苏省南京市玄武大道1000号</street>
    <zipcode>201001</zipcode>
    <mobile>1801989098</mobile>
  </address>
  <address>
    <street>江苏省南京市玄武大道1001号</street>
    <zipcode>201001</zipcode>
    <mobile>1811989098</mobile>
  </address>
  <created>1565691762404</created>
</user>
```

## 4.10 反序列化

```java
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
```

**问题：**

Exception in thread "main" java.lang.NoClassDefFoundError: org/jaxen/JaxenException

需要引入 

```
<!-- https://mvnrepository.com/artifact/jaxen/jaxen -->
<dependency>
    <groupId>jaxen</groupId>
    <artifactId>jaxen</artifactId>
    <version>1.1.6</version>
</dependency>
```


----
参考

- [https://dom4j.github.io/](https://dom4j.github.io/)
- [http://x-stream.github.io/tutorial.html](http://x-stream.github.io/tutorial.html)