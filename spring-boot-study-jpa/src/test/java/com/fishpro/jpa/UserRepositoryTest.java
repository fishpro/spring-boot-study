package com.fishpro.jpa;

import com.fishpro.jpa.domain.UserDO;
import com.fishpro.jpa.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    /**
     * 初始化一个对象 UserDO 测试Insert过程
     * */
    @Before
    public void before(){
        UserDO userDO=new UserDO();
        userDO.setId(1);
        userDO.setUserName("fishpro");
        userDO.setSex(1);
        userDO.setLastLoginTime(new Date());
        userDO.setPassword("passWord");
        userRepository.save(userDO);
    }

    @Test
    public void testAddUser(){
        UserDO userDO=new UserDO();
        userDO.setId(2);
        userDO.setUserName("fishpro2");
        userDO.setSex(1);
        userDO.setLastLoginTime(new Date());
        userDO.setPassword("passWord");
        userRepository.save(userDO);
        userDO=new UserDO();
        userDO.setId(3);
        userDO.setUserName("fishpro3");
        userDO.setSex(1);
        userDO.setLastLoginTime(new Date());
        userDO.setPassword("passWord");
        userRepository.save(userDO);
    }
    @Test
    public void testFind(){
        Optional<UserDO> optionalUserDO=userRepository.findById(1);
        if(optionalUserDO.isPresent()){
            UserDO userDO=optionalUserDO.get();
            System.out.println("testFind user"+userDO.getUserName());
        }

    }

    @Test
    public void testUpdate(){
        Optional<UserDO> optionalUserDO=userRepository.findById(1);
        if(optionalUserDO.isPresent()){
            UserDO userDO=optionalUserDO.get();
            userDO.setUserName("fishpro001");
            userRepository.save(userDO);
            System.out.println("testFind user"+userDO.getUserName());
        }

    }

    @Test
    public void testFindAll(){
        List<UserDO> list=userRepository.findAll();
        for (UserDO user:list
             ) {
            System.out.println("user_name:"+user.getUserName());
        }
    }

    @After
    public void after(){
    }
}
