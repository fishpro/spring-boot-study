package com.fishpro.redis.service;

import com.fishpro.redis.domain.UserDO;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {

    List<UserDO> list();
    UserDO get(Integer id);
    UserDO save(UserDO user);
    UserDO update(UserDO user);
    void delete(Integer id);

}
