package com.fishpro.restcontroller.controller;

import com.fishpro.restcontroller.domain.UserDO;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("user2")
public class UserRestController {


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
