Spring Boot 使用 JavaMailSender 来发送邮件，Spring Boot 是用来自动配置实现配置。邮件发送没有什么技术难点，拿来即用。


[本项目源码下载](https://github.com/fishpro/spring-boot-study/tree/master/spring-boot-study-sendemail)

# 1 新建 Spring Boot Maven 示例工程项目
注意：是用来 IDEA 开发工具
1. File > New > Project，如下图选择 `Spring Initializr` 然后点击 【Next】下一步
2. 填写 `GroupId`（包名）、`Artifact`（项目名） 即可。点击 下一步
    groupId=com.fishpro   
    artifactId=sendemail
3. 选择依赖 `Spring Web Starter` 前面打钩。
4. 项目名设置为 `spring-boot-study-sendemail`.
   
# 2 引入依赖 Pom
包括了
- mail
- freemarker
```xml
 <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-mail</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-freemarker</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

# 3 配置邮件信息
```bash
# JavaMailSender 邮件发送的配置
spring.mail.host=smtp.qq.com
spring.mail.username=用户qq邮箱
#QQ邮箱的授权码
spring.mail.password=授权码
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
spring.mail.default-encoding=UTF-8
```

# 4 编写示例代码
本示例中编写了一个RestController 类 MailController，以下是功能片段
## 4.1 发送文本邮件
 ```java
    /**
     * 发送文本标题 文本内容
     * */
    @GetMapping("/sendMail")
    public String sendMail(){
        try{

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(sendUser);
            message.setTo("fishpro@vip.qq.com");
            message.setSubject("主题邮件");
            message.setText("邮件内容");
            mailSender.send(message);
            return "success";
        }catch (Exception e){
            e.printStackTrace();
            return "fail";
        }
    }
 ```

## 4.2 发送邮件内容为 Html 的邮件
 ```java
 /**
     * 发送html内容
     * */
    @GetMapping("/sendHtmlMail")
    public String sendHtmlMail(){
        MimeMessage message=null;
        try{

            message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            helper.setFrom(sendUser);
            helper.setTo("fishpro@vip.qq.com");
            helper.setSubject("主题邮件");
            StringBuilder sb=new StringBuilder();
            sb.append("<h1>尊敬的客户您好！</h1>")
                    .append("<p>欢迎您访问我的博客 www.fishpro.com.cn</p>");
            helper.setText(sb.toString(),true);
        }catch (Exception e){
            e.printStackTrace();
            return "fail";
        }
        mailSender.send(message);
        return "success";
    }
 ```

## 4.3 发送带有附件的邮件
 ```java
/**
     * 发送附件内容
     * */
    @GetMapping("/sendAttachMail")
    public String sendAttachMail(){
        MimeMessage message=null;
        try{

            message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            helper.setFrom(sendUser);
            helper.setTo("fishpro@vip.qq.com");
            helper.setSubject("主题邮件");
            StringBuilder sb=new StringBuilder();
            sb.append("<h1>尊敬的客户您好！</h1>")
                    .append("<p>欢迎您访问我的博客 www.fishpro.com.cn</p>");
            helper.setText(sb.toString(),true);
            //获取附件资源
            FileSystemResource fileSystemResource =new FileSystemResource(new File(""));
            //把附件资源加入到发送消息中
            helper.addAttachment("",fileSystemResource);

        }catch (Exception e){
            e.printStackTrace();
            return "fail";
        }
        mailSender.send(message);
        return "success";
    }
 ```


## 4.4 发送带静态资源的邮件
 ```java
 /**
     * 发送带静态资源的邮件
     * */
    @GetMapping("/sendInlineMail")
    public String sendInlineMail(){
        MimeMessage message=null;
        try{

            message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            helper.setFrom(sendUser);
            helper.setTo("fishpro@vip.qq.com");
            helper.setSubject("主题邮件");
            StringBuilder sb=new StringBuilder();
            sb.append("<h1>尊敬的客户您好！</h1>")
                    .append("<p>欢迎您访问我的博客 www.fishpro.com.cn</p><p><img src='cid:pic' /></p>");
            helper.setText(sb.toString(),true);
            //获取附件资源
            FileSystemResource fileSystemResource =new FileSystemResource(new File(""));
            //把附件资源加入到发送消息中
            helper.addInline("pic",fileSystemResource);

        }catch (Exception e){
            e.printStackTrace();
            return "fail";
        }
        mailSender.send(message);
        return "success";
    }
 ```

## 4.5 发送基于 freemarker 模板内容的邮件
 ```java
/**
     * 基于 freemarker 模板发送
     * */
    @GetMapping("/sendTemplateMail")
    public String sendTemplateMail(){
        MimeMessage message = null;
        try{

            message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            helper.setFrom(sendUser);
            helper.setTo("fishpro@vip.qq.com");
            helper.setSubject("主题邮件");
            Map<String,Object> model =new HashMap<>();
            model.put("welcome","欢迎您，hello world template email ");
            //使用 freeMarkerConfigurer 获取模板 index.ftl
            Template template = freeMarkerConfigurer.getConfiguration().getTemplate("index.ftl");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            helper.setText(html, true);

        }catch (Exception e){
            e.printStackTrace();
            return "fail";
        }
        mailSender.send(message);
        return "success";
    }
 ```

完整代码 
[本项目源码下载](https://github.com/fishpro/spring-boot-study/tree/master/spring-boot-study-sendemail)

----
参考：
https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-email.html