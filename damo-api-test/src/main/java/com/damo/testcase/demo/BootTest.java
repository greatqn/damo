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

package com.damo.testcase.demo;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONObject;
import com.damo.api.Application;
import com.damo.api.Constants;
import com.damo.api.hello.HelloApi;

@SpringBootTest(classes = {Application.class}, webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class BootTest extends AbstractTestNGSpringContextTests {

    private static Logger logger = LoggerFactory.getLogger(BootTest.class);

    @Autowired
    private HelloApi helloApi;

    @BeforeClass
    public void before() {
        Constants.token = helloApi.getToken();
    }

    @Test
    public void helloSuccessTest() {
        ResponseEntity<Map> map = helloApi.success();
        Map<String, Object> maps = map.getBody();
        logger.info(JSONObject.toJSONString(maps));
        Assert.assertEquals(JSONObject.toJSONString(maps), "{\"message\":\"success\",\"code\":200}");

    }

    @Test
    public void helloFailTest() {
        ResponseEntity<Map> map = helloApi.fail();
        Map<String, Object> maps = map.getBody();
        logger.info(JSONObject.toJSONString(maps));
        Assert.assertEquals(JSONObject.toJSONString(maps), "{\"message\":\"fail\",\"code\":400}");
    }

}