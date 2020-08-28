package com.damo.service;

import org.springframework.stereotype.Service;


@Service
public class DogService extends CatService {

    @Override
    public String sayHello(String name){
        return helloService.sayHello(name, 10L);
    }
}
