package com.fishpro.webservicejaxws;

import com.fishpro.webservicejaxws.user.UserDto;
import com.fishpro.webservicejaxws.user.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebservicejaxwsApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebservicejaxwsApplication.class, args);
        UserService userService=new UserService();
        UserDto userDto= userService.getUserPortName().getUserById("1");
        System.out.println("userdto "+userDto.getUserName());
    }

}
