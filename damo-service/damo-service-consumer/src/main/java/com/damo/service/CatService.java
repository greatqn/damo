package com.damo.service;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;


@Service
public class CatService {
    @DubboReference
    protected HelloService helloService;
    
    
    public String sayHello(String name){
        return helloService.sayHello(name, 10L);
    }
}
