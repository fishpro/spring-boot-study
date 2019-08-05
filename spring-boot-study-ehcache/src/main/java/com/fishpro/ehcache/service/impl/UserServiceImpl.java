package com.fishpro.ehcache.service.impl;

import com.fishpro.ehcache.domain.UserDO;
import com.fishpro.ehcache.service.UserService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import sun.nio.cs.US_ASCII;

import java.util.ArrayList;
import java.util.List;

// fpcache 对应 ehcache.xml 中的 fpcache 节点
@CacheConfig(cacheNames = {"fpcache"})
@Service
public class UserServiceImpl implements UserService {
    @Override
    @Cacheable("fpcache")
    public List<UserDO> list() {
        List<UserDO> list=new ArrayList<>();
        list.add( new UserDO(1,"fishpro","123456",1));
        list.add( new UserDO(2,"fishpro2","123456",1));
        list.add( new UserDO(3,"fishpro3","123456",1));
        System.out.println("获取用户列表使用 @Cacheable 注意执行第二次的时候不会有本语句输出了，部分删除掉缓存");
        return list;
    }

    @Override
    @Cacheable(value = "fpcache",key = "#id")
    public UserDO get(Integer id) {
        System.out.println("获取单个用户 get user by"+id);
        return new UserDO(1,"fishpro","123456",1);

    }

    @Override
    @CachePut(value = "fpcache",key = "#user.id")
    public UserDO save(UserDO user) {
        System.out.println("保存用户使用 @CachePut 每次都会执行语句并缓存 save user by "+user.getUserName());
        return user;

    }

    @Override
    @CachePut(value = "fpcache",key = "#user.id")
    public UserDO update(UserDO user) {
        System.out.println("更新用户使用 @CachePut 每次都会执行语句并缓存 update user by "+user.getUserName());
        return user;

    }

    @Override
    @CacheEvict(allEntries = true)
    public void delete(Integer id) {
        System.out.println("删除用户根据用户ID，如果 allEntries = true 则不论 key 是啥都全部删除缓存"+id);
    }
}
