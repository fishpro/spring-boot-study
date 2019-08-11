package com.fishpro.sendemail.controller;

import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.JmsProperties;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@RestController
public class MailController {

    @Autowired
    private JavaMailSender mailSender;

    private FreeMarkerConfigurer freeMarkerConfigurer;

    private String sendUser;

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
}
