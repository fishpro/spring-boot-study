本示例在 websocketdemo 示例基础上增加了udp服务，UDP实现了与C/C++的struct 结构体的传输
- 实现了 websocket
- 实现了 udp服务器
- 实现了 udp 发送
- 实现了 udp 结构体接收（c/c++）

本章只说明UDP部分，关于websocket请参考websocket章节

**本文仅仅适用**
- spring boot 2.x
- jdk 1.8+

本将建立一个服务端 websocket，并使用一个 rest api 进行消息方式。使用一个thymeleaf的页面进行js接收

[本项目源码下载](https://github.com/fishpro/spring-boot-study/tree/master/spring-boot-study-udpdemo)

# 0 补充说明

字节顺序是指占内存多于一个字节类型的数据在内存中的存放顺序，通常有小端、大端两种字节顺序。小端字节序指低字节数据存放在内存低地址处，高字节数据存放在内存高地址处；大端字节序是高字节数据存放在低地址处，低字节数据存放在高地址处。

基于X86平台的PC机是小端字节序的，而有的嵌入式平台则是大端字节序的。

网络协议规定接收到得第一个字节是高字节，存放到低地址，所以发送时会首先去低地址取数据的高字节。

所有网络协议也都是采用big endian的方式来传输数据的。所以有时我们也会把big endian方式称之为网络字节序。当两台采用不同字节序的主机通信时，在发送数据之前都必须经过字节序的转换成为网络字节序后再进行传输。

网络字节顺序是TCP/IP中规定好的一种数据表示格式，它与具体的CPU类型、操作系统等无关，从而可以保证数据在不同主机之间传输时能够被正确解释。网络字节顺序采用big endian排序方式。

C/C++语言编写的程序里数据存储顺序是跟编译平台所在的CPU相关的。在windows的字节序为低字节开头，在linux,unix的字节序为高字节开头。而 

**JAVA编写的程序则唯一采用big endian方式来存储数据，无论平台怎样变化，都是高字节开头。所以JAVA 与 小端排序的VC 需要设置字节顺序为小端排序**
# 1 新建 Spring Boot Maven 示例工程项目
注意：是用来 IDEA 开发工具
1. File > New > Project，如下图选择 `Spring Initializr` 然后点击 【Next】下一步
2. 填写 `GroupId`（包名）、`Artifact`（项目名） 即可。点击 下一步
    groupId=com.fishpro   
    artifactId=udpdemo
3. 选择依赖 `Spring Web Starter` 前面打钩。
4. 项目名设置为 `spring-boot-study-udpdemo`.
   
# 2 引入依赖 Pom

- websocket
- thymeleaf
- javolution
增加了javolution的引用
```xml
 <?xml version="1.0" encoding="UTF-8"?>
 <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
     <modelVersion>4.0.0</modelVersion>
     <parent>
         <groupId>org.springframework.boot</groupId>
         <artifactId>spring-boot-starter-parent</artifactId>
         <version>2.3.1.RELEASE</version>
         <relativePath/> <!-- lookup parent from repository -->
     </parent>
     <groupId>com.fishpro</groupId>
     <artifactId>udpdemo</artifactId>
     <version>0.0.1-SNAPSHOT</version>
     <name>udpdemo</name>
     <description>Demo project for Spring Boot</description>
 
     <properties>
         <java.version>1.8</java.version>
     </properties>
 
     <dependencies>
         <dependency>
             <groupId>org.springframework.boot</groupId>
             <artifactId>spring-boot-starter-thymeleaf</artifactId>
         </dependency>
         <!-- https://mvnrepository.com/artifact/org.javolution/javolution -->
         <dependency>
             <groupId>org.javolution</groupId>
             <artifactId>javolution</artifactId>
             <version>5.3.1</version>
         </dependency>
         <dependency>
             <groupId>org.springframework.boot</groupId>
             <artifactId>spring-boot-starter-web</artifactId>
         </dependency>
         <dependency>
             <groupId>org.springframework.boot</groupId>
             <artifactId>spring-boot-starter-websocket</artifactId>
         </dependency>
 
         <dependency>
             <groupId>org.springframework.boot</groupId>
             <artifactId>spring-boot-starter-test</artifactId>
             <scope>test</scope>
             <exclusions>
                 <exclusion>
                     <groupId>org.junit.vintage</groupId>
                     <artifactId>junit-vintage-engine</artifactId>
                 </exclusion>
             </exclusions>
         </dependency>
     </dependencies>
 
     <build>
         <plugins>
             <plugin>
                 <groupId>org.springframework.boot</groupId>
                 <artifactId>spring-boot-maven-plugin</artifactId>
             </plugin>
         </plugins>
     </build>
 
 </project>

```


# 3 UDP 服务 
```java
import javolution.io.Struct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

/*
 * 服务器端，实现基于UDP的用户登陆
 */
@WebListener
public class UDPServer implements ServletContextListener {
    public static Logger logger = Logger.getLogger(UDPServer.class.getName());
    public static final int MAX_UDP_DATA_SIZE = 4096;
    public static final int UDP_PORT = 6008;
    public static DatagramPacket packet = null;
    public static DatagramSocket socket = null;


    @Autowired
    private SimpMessagingTemplate template;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            logger.info("========启动一个线程，监听UDP数据报.PORT:" + UDP_PORT + "=========");
            // 启动一个线程，监听UDP数据报
            new Thread(new UDPProcess(UDP_PORT)).start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    class UDPProcess implements Runnable {

        public UDPProcess(final int port) throws SocketException {
            //创建服务器端DatagramSocket，指定端口
            socket = new DatagramSocket(port);
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            logger.info("=======创建数据报，用于接收客户端发送的数据======");
            while (true) {
                byte[] buffer = new byte[MAX_UDP_DATA_SIZE];
                packet = new DatagramPacket(buffer, buffer.length);
                //udp message struct
                DemoStruct message=new DemoStruct();
                 //add at 2020-09-22 如果对方是VC window平台那么就要设置 byteOrder 为 小端排序
                                ByteBuffer byteBuffer=ByteBuffer.wrap(buffer);
                                byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
                                message.setByteBuffer(byteBuffer,0);
                //                message.setByteBuffer(ByteBuffer.wrap(buffer),0);

                try {
                    logger.info("=======此方法在接收到数据报之前会一直阻塞======");
                    socket.receive(packet);
                    logger.info(message.Col1[0].toString());
                    logger.info(message.Col1[1].toString());
                    logger.info(message.Col1[2].toString());
                    logger.info(message.Col2[0].toString());
                    logger.info(message.Col3[1].toString());
                    logger.info(message.Col4[2].toString());
                    new Thread(new Process(packet)).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    class Process implements Runnable {
        public Process(DatagramPacket packet) throws UnsupportedEncodingException {
            // TODO Auto-generated constructor stub
            logger.info("=======接收到的UDP信息======");
            byte[] buffer = packet.getData();// 接收到的UDP信息，然后解码
//            String srt1 = new String(buffer, "GBK").trim();
            //            logger.info("=======Process srt1 GBK======" + srt1);
            String srt2 = new String(buffer, "UTF-8").trim();
            logger.info("=======Process srt2 UTF-8======" + srt2);
            if(!"".equals(srt2)){
                template.convertAndSend("/queue/bigtopic/hot", srt2);
            }
//            String srt3 = new String(buffer, "ISO-8859-1").trim();
//            logger.info("=======Process srt3 ISO-8859-1======" + srt3);
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            logger.info("====过程运行=====");
            try {
                logger.info("====向客户端响应数据=====");
                //1.定义客户端的地址、端口号、数据
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                byte[] data2 = "{'request':'alive','errcode':'0'}".getBytes();
                //2.创建数据报，包含响应的数据信息
                DatagramPacket packet2 = new DatagramPacket(data2, data2.length, address, port);
                //3.响应客户端
                socket.send(packet2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("========UDPListener摧毁=========");
    }


    public static class DemoStruct extends Struct{
        public  final Struct.Bool Do=new Bool();
        public final Unsigned8[] Col1=array(new Unsigned8[3]);
        public final Unsigned8[] Col2=array(new Unsigned8[3]);
        public final Unsigned8[] Col3=array(new Unsigned8[3]);
        public final Unsigned8[] Col4=array(new Unsigned8[3]);
        public final Unsigned8[] Col5=array(new Unsigned8[3]);
        public final Unsigned8[] Col6=array(new Unsigned8[3]);
        public final Unsigned8 Col7=new Unsigned8();
        public final Unsigned32[] Ao=array(new Unsigned32[4]);

        /**
         *  //add at 2020-09-22 如果对方是VC window平台那么就要设置 byteOrder 为 小端排序
         * */
        @Override
        public ByteOrder byteOrder() {
            return ByteOrder.LITTLE_ENDIAN;
        }
    }
}

```

# 4 C/C++ 结构体
## 4.1  C/C++ 结构体 

```c

typedef uint32_t UINT32;
typedef uint8_t BYTE;
struct DemoData {
	bool Do[1]; //故障（T：亮  F：灭）

	BYTE Co1[3];//排温 数码管
	BYTE Co2[3];//滑压 数码管
	BYTE Co3[3];//助液压 数码管
	BYTE Co4[3];//主液压 数码管
	BYTE Co5[3];//交流 数码管
	BYTE Co6[3];//直流 数码管
	BYTE Co7;//组别
	UINT32 Ao[4];
}DD;

```
## 4.2 C/C++ udp 发送

## 4.3 运行

右键 RedisApplication 选择 Run RedisApplication 在浏览器中输入 http://localhost:8080/index
```bash

2020-06-21 18:44:00.806  INFO 7540 --- [     Thread-117] com.fishpro.udpdemo.UDPServer            : =======此方法在接收到数据报之前会一直阻塞======
2020-06-21 18:44:00.806  INFO 7540 --- [     Thread-175] com.fishpro.udpdemo.UDPServer            : ====过程运行=====
2020-06-21 18:44:00.806  INFO 7540 --- [     Thread-175] com.fishpro.udpdemo.UDPServer            : ====向客户端响应数据=====
2020-06-21 18:44:05.808  INFO 7540 --- [     Thread-117] com.fishpro.udpdemo.UDPServer            : 201
2020-06-21 18:44:05.808  INFO 7540 --- [     Thread-117] com.fishpro.udpdemo.UDPServer            : 200
2020-06-21 18:44:05.808  INFO 7540 --- [     Thread-117] com.fishpro.udpdemo.UDPServer            : 128
2020-06-21 18:44:05.808  INFO 7540 --- [     Thread-117] com.fishpro.udpdemo.UDPServer            : 4
2020-06-21 18:44:05.808  INFO 7540 --- [     Thread-117] com.fishpro.udpdemo.UDPServer            : 8
2020-06-21 18:44:05.808  INFO 7540 --- [     Thread-117] com.fishpro.udpdemo.UDPServer            : 12
2020-06-21 18:44:05.808  INFO 7540 --- [     Thread-117] com.fishpro.udpdemo.UDPServer            : =======接收到的UDP信息======
2020-06-21 18:44:05.808  INFO 7540 --- [     Thread-117] com.fishpro.udpdemo.UDPServer            : =======Process srt2 UTF-8======�Ȁ	
```

## 4。3 如何接收 C/C++定义的嵌套结构体

### C/C++
```c++

typedef uint32_t UINT32;
typedef uint8_t BYTE;
struct DemoData {
	struct Device1 {
		bool Do[1]; //故障（T：亮  F：灭）

		BYTE Co1[3];//排温 数码管
		BYTE Co2[3];//滑压 数码管
		BYTE Co3[3];//助液压 数码管
		BYTE Co4[3];//主液压 数码管
		BYTE Co5[3];//交流 数码管
		BYTE Co6[3];//直流 数码管
		BYTE Co7;//组别
		UINT32 Ao[4];

	}Dev1;
	struct Device2 {
		UINT32 Ao[4];
	}Dev2;
	struct Device3 {
		bool Do[1]; //故障（T：亮  F：灭）
	}Dev3;

}DD;

```

### Java
```java

    /**
     * 注意下面是嵌套的struct写法
     * */
    public static class DemoStruct extends Struct {
        public final Dev1 dev1 = inner(new Dev1());
        public final Dev2 dev2 = inner(new Dev2());
        public final Dev3 dev3 = inner(new Dev3());


    }

    public static class Dev1 extends Struct {
        public final Struct.Bool Do = new Bool();
        public final Unsigned8[] Col1 = array(new Unsigned8[3]);
        public final Unsigned8[] Col2 = array(new Unsigned8[3]);
        public final Unsigned8[] Col3 = array(new Unsigned8[3]);
        public final Unsigned8[] Col4 = array(new Unsigned8[3]);
        public final Unsigned8[] Col5 = array(new Unsigned8[3]);
        public final Unsigned8[] Col6 = array(new Unsigned8[3]);
        public final Unsigned8 Col7 = new Unsigned8();
        public final Unsigned32[] Ao = array(new Unsigned32[4]);

    }

    public static class Dev2 extends Struct {
        public final Unsigned32[] Ao = array(new Unsigned32[4]);

    }

    public static class Dev3 extends Struct {
        public final Struct.Bool Do = new Bool();

    }

```