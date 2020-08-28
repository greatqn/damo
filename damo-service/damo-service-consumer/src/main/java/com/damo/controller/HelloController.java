/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.damo.controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.damo.service.CatService;
import com.damo.service.DogService;
import com.damo.service.HelloService;

@RestController
public class HelloController {
//    @DubboReference(version = "1.0.0",
//        application = "${dubbo.application.id}", timeout=4000,
//        url = "dubbo://localhost:12345")
    @DubboReference
    private HelloService helloService;

    @Autowired
    private CatService catService;
    
    @Autowired
    private DogService dogService;
    
    /**
     * http://localhost:9091/sayHello?name=11&time=100
     * @param name
     * @return
     */
    @RequestMapping("/sayHello")
    public String sayHello(@RequestParam String name,@RequestParam(defaultValue = "0") Long time) {
        return helloService.sayHello(name,time);
    }
    
    /**
     * http://localhost:9091/sayCat?name=cat
     * @param name
     * @param time
     * @return
     */
    @RequestMapping("/sayCat")
    public String sayCat(@RequestParam String name,@RequestParam(defaultValue = "0") Long time) {
        return catService.sayHello(name);
    }
    
    /**
     * http://localhost:9091/sayDog?name=dog
     * @param name
     * @param time
     * @return
     */
    @RequestMapping("/sayDog")
    public String sayDog(@RequestParam String name,@RequestParam(defaultValue = "0") Long time) {
        return dogService.sayHello(name);
    }
}