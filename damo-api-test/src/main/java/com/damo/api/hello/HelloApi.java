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

package com.damo.api.hello;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.damo.api.Constants;

@Service
public class HelloApi {
    
    @Value("${test.servicePath}")
    private String servicePath;
    
    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<Map> success() {
        String url = servicePath + "/hello/success";
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-access-token", Constants.token);

        HttpEntity<String> requestEntity = new HttpEntity<String>(null, headers);
        LinkedMultiValueMap<String, Object> para = new LinkedMultiValueMap<>();
        ResponseEntity<Map> map = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Map.class, para);
        return map;
    }

    public ResponseEntity<Map> fail() {
        String url = servicePath + "/hello/fail";
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-access-token", Constants.token);

        HttpEntity<String> requestEntity = new HttpEntity<String>(null, headers);
        LinkedMultiValueMap<String, Object> para = new LinkedMultiValueMap<>();
        ResponseEntity<Map> map = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Map.class, para);
        return map;
    }
    
    public String getToken() {
        String url = servicePath + "/auth/apply-token";
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("clientKey", "auto11");

        ResponseEntity<Map> response = restTemplate.postForEntity(url, map, Map.class);
        Map<String, Object> result = response.getBody();
        return result.get("data").toString();
    }
}