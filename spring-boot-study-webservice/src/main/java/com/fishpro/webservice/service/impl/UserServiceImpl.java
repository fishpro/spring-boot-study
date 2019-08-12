package com.fishpro.webservice.service.impl;

import com.fishpro.webservice.constant.WsConst;
import com.fishpro.webservice.dto.UserDto;
import com.fishpro.webservice.service.UserService;

import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;

@WebService(
        targetNamespace = WsConst.NAMESPACE_URI, //wsdl命名空间
        name = "userPortType",                 //portType名称 客户端生成代码时 为接口名称
        serviceName = "userService",           //服务name名称
        portName = "userPortName",             //port名称
        endpointInterface = "com.fishpro.webservice.service.UserService")//指定发布webservcie的接口类，此类也需要接入@WebService注解
public class UserServiceImpl implements UserService {

    @Override
    public UserDto get(@WebParam(name = "id") String id){
        if(null==id){
            return  null;
        }

        List<UserDto> list= getData();
        UserDto UserDto=null;
        for (UserDto user:list
                ) {
            if(id.equals(user.getUserId().toString())){
                UserDto=user;
                break;
            }
        }

        return UserDto;
    }

    @Override
    public List<UserDto> list(){
        List<UserDto> list=new ArrayList<>();

        list=getData();

        return list;
    }

    @Override
    public int count(){
        return getData().size();
    }

    @Override
    public int save(UserDto user){
        return 1;
    }

    @Override
    public int update(UserDto user){
        return 1;
    }

    @Override
    public int remove(Integer id){
        return 1;
    }

    @Override
    public int batchRemove(Integer[] ids){
        return 1;
    }

    /**
     * 模拟一组数据
     * */
    private List<UserDto> getData(){
        List<UserDto> list=new ArrayList<>();

        UserDto UserDto=new UserDto();
        UserDto.setUserId(1);
        UserDto.setUserName("admin");
        list.add(UserDto);

        UserDto=new UserDto();
        UserDto.setUserId(2);
        UserDto.setUserName("heike");
        list.add(UserDto);

        UserDto=new UserDto();
        UserDto.setUserId(3);
        UserDto.setUserName("tom");
        list.add(UserDto);

        UserDto=new UserDto();
        UserDto.setUserId(4);
        UserDto.setUserName("mac");
        list.add(UserDto);

        return  list;
    }

}
