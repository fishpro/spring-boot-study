package com.fishpro.shiro.config;

import com.fishpro.shiro.domain.UserDO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;

import java.security.Principal;
import java.util.*;

/**
 * 用户领域 重写了 AuthorizingRealm ，AuthorizingRealm（授权） 其实是继承了 AuthenticatingRealm（认证）
 * 所在在这里只要继承 AuthorizingRealm（授权），主要实现 授权和认证的方法重写
 * 1.doGetAuthenticationInfo 重写认证
 * 2.doGetAuthorizationInfo 重写授权
 * */
public class UserRealm extends AuthorizingRealm {
    /**
     * doGetAuthenticationInfo 重写认证
     * @param authenticationToken token
     * @return 返回认证信息实体（好看身份和证明） AuthenticationInfo
     * */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username=(String)authenticationToken.getPrincipal();//身份 例如 用户名
        Map<String ,Object> map=new HashMap<>(16);
        map.put("username",username);
        String password  =new String((char[]) authenticationToken.getCredentials());//证明 例如 密码
        //对身份+证明的数据认证 这里模拟了一个数据源
        //如果是数据库 那么这里应该调用数据库判断用户名密码是否正确
        if(!"admin".equals(username) || !"123456".equals(password)){
            throw new IncorrectCredentialsException("账号或密码不正确");
        }
        //认证通过
        UserDO user=new UserDO();
        user.setId(1);//假设用户ID=1
        user.setUserName(username);
        user.setPassword(password);
        //建立一个 SimpleAuthenticationInfo 认证模块，包括了身份】证明等信息
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, password, getName());
        return info;
    }

    /**
     * 重写授权 doGetAuthorizationInfo 返回  授权信息对象 AuthorizationInfo
     * @param  principalCollection 身份信息
     * @return  返回  授权信息对象 AuthorizationInfo
     * */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        UserDO userDO  = (UserDO)principalCollection.getPrimaryPrincipal();
        Integer userId= userDO.getId();//转成 user 对象
        //授权 新建一个授权模块 SimpleAuthorizationInfo 把 权限赋值给当前的用户
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        //设置当前会话拥有的角色 实际场景根据业务来如从数据库获取角色列表
        Set<String> roles=new HashSet<>();
        roles.add("admin");
        roles.add("finance");
        info.setRoles(roles);

        //设置当前会话可以拥有的权限 实际场景根据业务来如从数据库获取角色列表下的权限列表
        Set<String> permissions=new HashSet<>();
        permissions.add("system:article:article");
        permissions.add("system:article:add");
        permissions.add("system:article:edit");
        permissions.add("system:article:remove");
        permissions.add("system:article:batchRemove");
        info.setStringPermissions(permissions);
        return  info;
    }

}
