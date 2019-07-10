package com.fishpro.helloworld.service;

import org.springframework.stereotype.Service;

@Service
public class MyService {
    public String message(){
        return "this is module for helloworld.service method message";
    }
}
