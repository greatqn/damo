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

import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.damo.api.base.BaseResponse;
import com.damo.shiro.autoconfigure.stateless.ShiroWebAutoConfiguration;
import com.damo.shiro.autoconfigure.stateless.support.jwt.JWTManager;
import com.damo.shiro.autoconfigure.stateless.support.jwt.Payload;

/**
 * jwt认证中心。
 */
@RestController
@RequestMapping("/auth")
public class AuthenticateController {
    
    @Autowired
    private JWTManager jwtManager;

    @ApiOperation(value = "获取token", notes = "提供客户端key")
    @RequestMapping(value = "/apply-token", method = RequestMethod.POST)
    public Map<String, Object> applyToken(@RequestParam(name = "clientKey") String clientKey) {
        
        Payload payload = new Payload();
        payload.put("uid", 123L);
        payload.put("username", "中文");
        payload.put("roles", "T,G");
        payload.put("permissions", "system:user:view,system:user:add");
        String token = jwtManager.create(ShiroWebAutoConfiguration.DEFAULT_ISSUER, payload);
        
        BaseResponse result = BaseResponse.success();
        result.data(token);
        return result;
    }

    @ApiOperation(value = "test验证token", notes = "显示token的内容")
    @RequestMapping(value = "/issue-token", method = RequestMethod.POST)
    public String issueToken(@RequestParam(name = "token") String token) throws Exception {
        Payload payload = jwtManager.verifyAndParsePayload(ShiroWebAutoConfiguration.DEFAULT_ISSUER, token);
        return payload.toString();
    }

    
    @ApiOperation(value = "test显示session", notes = "显示session的内容")
    @RequestMapping(value = "/show-session", method = RequestMethod.GET)
    public String issueToken(@ApiIgnore() HttpSession session) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(session.toString());
        sb.append("{}");
        Enumeration<String> e = session.getAttributeNames();
        while (e.hasMoreElements()) {
            sb.append(session.getAttribute(e.nextElement().toString()));
            sb.append("|");
        }
        
        Payload payload = jwtManager.verifyAndParsePayload(ShiroWebAutoConfiguration.DEFAULT_ISSUER, SecurityUtils.getSubject().getPrincipal().toString());
        sb.append(payload.toString());
        
        return sb.toString();
    }
}