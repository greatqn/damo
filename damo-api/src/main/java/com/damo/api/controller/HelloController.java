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

package com.damo.api.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.damo.api.base.BaseResponse;
import com.damo.api.base.GlobalException;
import com.damo.shiro.autoconfigure.stateless.support.jwt.JWTManager;
import com.damo.shiro.autoconfigure.stateless.support.jwt.Payload;


@RestController
@RequestMapping(value = "/hello")
public class HelloController {
    
    @Autowired
    private JWTManager jwtManager;

    @ApiOperation(value = "打个招呼", notes = "")
    @RequestMapping(value = "/success", method = RequestMethod.GET)
    public BaseResponse success() {
        BaseResponse result = BaseResponse.success();
        result.put("token", SecurityUtils.getSubject().getPrincipal());
        return result;
    }
    
    @ApiOperation(value = "打个招呼", notes = "")
    @RequestMapping(value = "/fail", method = RequestMethod.GET)
    public BaseResponse fail() {
        BaseResponse result = BaseResponse.fail();

        return result;
    }
    
    @ApiOperation(value = "打个招呼", notes = "")
    @RequestMapping(value = "/exception", method = RequestMethod.GET)
    public BaseResponse exception() {
        throw new GlobalException(300,"一个异常");
    }
    
    @ApiOperation(value = "获取用户详细信息", notes = "根据url的id来获取用户详细信息")
    @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Long" ,paramType="path", example ="1")
    @RequestMapping(value = "/findone/{userId}", method = RequestMethod.GET)
    public BaseResponse getUser(@PathVariable Long userId) {
        BaseResponse result = BaseResponse.success();
        
        return result;
    }
    
    public Payload parseJWT() {
        Object principal = SecurityUtils.getSubject().getPrincipal();
        if(principal == null) return null;
        
        return jwtManager.verifyAndParsePayload("access_token", SecurityUtils.getSubject().getPrincipal().toString());
    }
    
    public Integer getUserId(){
        Payload payload = parseJWT();
        if(payload == null) return 0;
        return (Integer)payload.get("uid");
    }
    
}