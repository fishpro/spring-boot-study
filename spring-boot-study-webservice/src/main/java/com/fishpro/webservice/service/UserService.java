package com.fishpro.webservice.service;

import com.fishpro.webservice.constant.WsConst;
import com.fishpro.webservice.dto.UserDto;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

/**
 * 用户服务类 必须使用 @WebService
 * */
@WebService(targetNamespace = WsConst.NAMESPACE_URI ,name = "userPortType")
public interface UserService {
    /**
     * 根据用户id获取用户信息
     * */
    @WebMethod(operationName="getUserById")
    UserDto get(String id);

    /**
     * 获取全部用户信息
     * */
    @WebMethod(operationName="getUsers")
    List<UserDto> list();

    /**
     * 获取用户数
     * */
    @WebMethod(operationName="count")
    int count();

    /**
     * 新增用户
     * */
    @WebMethod(operationName="save")
    int save(@WebParam(name = "user")  UserDto user);

    /**
     * 更新用户
     * */
    @WebMethod(operationName="update")
    int update(@WebParam(name = "user")  UserDto user);

    /**
     * 删除用户
     * */
    @WebMethod(operationName="remove")
    int remove(@WebParam(name = "id")  Integer id);

    /**
     * 批量删除用户
     * */
    @WebMethod(operationName="batchRemove")
    int batchRemove(@WebParam(name = "ids")  Integer[] ids);
}
