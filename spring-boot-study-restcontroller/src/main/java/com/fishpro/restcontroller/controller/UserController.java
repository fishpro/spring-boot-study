package com.fishpro.restcontroller.controller;

import com.fishpro.restcontroller.domain.UserDO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    /**
     * 模拟一组数据
     * */
    private List<UserDO> getData(){
        List<UserDO> list=new ArrayList<>();

        UserDO userDO=new UserDO();
        userDO.setUserId(1);
        userDO.setUserName("admin");
        list.add(userDO);

        userDO=new UserDO();
        userDO.setUserId(2);
        userDO.setUserName("heike");
        list.add(userDO);

        userDO=new UserDO();
        userDO.setUserId(3);
        userDO.setUserName("tom");
        list.add(userDO);

        userDO=new UserDO();
        userDO.setUserId(4);
        userDO.setUserName("mac");
        list.add(userDO);

        return  list;
    }

    /**
     * GET  返回用户列表信息
     * */
    @RequestMapping(method = RequestMethod.GET,value = "/index")
    public String index(Model model){
        List<UserDO> list =getData();
        model.addAttribute("list",list);//返回 用户 list
        return "user/index";
    }


    /**
     * GET 返回add页面
     * @GetMapping("/add") = @RequestMapping(method = RequestMethod.GET,value = "/add")
     * */
    @GetMapping("/add")
    public String add(){
        return "user/add";
    }

    /**
     * GET 返回编辑页面
     * @GetMapping("/edit/{id}") = @RequestMapping(method = RequestMethod.GET,value = "/edit/{id}")
     * @PathVariable("id") 表示路由中的动态参数部分
     * @param  id 表示要编辑的用户id
     * @param model 表示将要输出到页面的 Model 对象
     * @return 返回到 user/edit页面
     * */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, Model model){
        UserDO user =new UserDO();
        user.setUserId(3);
        user.setUserName("fishpro");
        model.addAttribute("user",user);
        return "user/edit";
    }

    /**
     * POST 新增用户api
     * @RequestBody 表示参数使用 json 对象传输
     * @return 返回 map对象
     * */
    @RequestMapping(method = RequestMethod.POST,value = "/save")
    @ResponseBody
    public Object save(UserDO user){
        List<UserDO> list= getData();
        list.add(user);//模拟向列表中增加数据
        Map<String,Object> map=new HashMap<>();
        if(null==user){

            map.put("status",3);
            map.put("message","没有传任何对象");
            return  map;
        }
        map.put("status",0);
        map.put("data",user);
        return  map;
    }

    /**
     * POST 修改用户api
     * @RequestBody 表示参数使用 json 对象传输
     * @return 返回 map对象
     * */
    @PostMapping("/update")
    @ResponseBody
    public Object update(@RequestBody UserDO user){
        Map<String,Object> map=new HashMap<>();
        if(null==user){

            map.put("status",3);
            map.put("message","没有传任何对象");
            return  map;
        }
        //更新逻辑
        map.put("status",0);
        return  map;
    }

    /**
     * POST 修改用户api
     * @return 返回 map对象
     * */
    @PostMapping("/delete/{id}")
    @ResponseBody
    public Object delete(@PathVariable("id") Integer id){
        List<UserDO> list= getData();
        UserDO userDO=null;
        for (UserDO user:list
                ) {
            if(id.equals(user.getUserId().toString())){
                //删除用户
                userDO=user;
                break;
            }
        }

        Map<String,Object> map=new HashMap<>();
        map.put("status",0);
        map.put("data",userDO);
        return  map;
    }
}
