package com.fishpro.shiro.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {
    //shiro 认证成功后默认跳转页面
    @GetMapping("/index")
    public String index(){
        return "index";
    }

    @GetMapping("/403")
    public String err403(){
        return "403";
    }
    /**
     * 根据权限授权使用注解 @RequiresPermissions
     * */
    @GetMapping("/article")
    @RequiresPermissions("app:article:article")
    public String article(){
        return "article";
    }

    /**
     * 根据权限授权使用注解 @RequiresPermissions
     * */
    @GetMapping("/setting")
    @RequiresPermissions("app:setting:setting")
    public String setting(){
        return "setting";
    }

    @GetMapping("/show")
    @ResponseBody
    public String show(){

        Subject subject = SecurityUtils.getSubject();
        String str="";
        if(subject.hasRole("admin")){
            str=str+"您拥有 admin 权限";
        }else{
            str=str+"您没有 admin 权限";
        }
        if(subject.hasRole("sale")){
            str=str+"您拥有 sale 权限";
        }
        else{
            str=str+"您没有 sale 权限";
        }
        try{
            subject.checkPermission("app:setting:setting");
            str=str+"您拥有 app:setting:setting 权限";

        }catch (UnauthenticatedException ex){
            str=str+"您没有 app:setting:setting 权限";
        }
        return  str;
    }

    //get /login 方法，对应前端 login.html 页面
    @GetMapping("/login")
    public String login(){
        return "login";
    }

    //post /login 方法，对应登录提交接口
    @PostMapping("/login")
    @ResponseBody
    public Object loginsubmit(@RequestParam String userName,@RequestParam String password){
        Map<String,Object> map=new HashMap<>();
        //把身份 useName 和 证明 password 封装成对象 UsernamePasswordToken
        UsernamePasswordToken token=new UsernamePasswordToken(userName,password);
        //获取当前的 subject
        Subject subject = SecurityUtils.getSubject();
        try{
            subject.login(token);
            map.put("status",0);
            map.put("message","登录成功");
            return map;
        }catch (AuthenticationException e){
            map.put("status",1);
            map.put("message","用户名或密码错误");
            return map;
        }
    }


    @GetMapping("/logout")
    String logout(HttpSession session, SessionStatus sessionStatus, Model model) {
        //会员中心退出登录 当使用这两属性session属性退出
        session.removeAttribute("userData");
        sessionStatus.setComplete();
        SecurityUtils.getSubject().logout();
        return "redirect:/login";

    }
}
