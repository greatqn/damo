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

package com.damo.api.base;

import java.util.HashMap;

/**
 * 响应封装
 * 
 */
public class BaseResponse extends HashMap<String, Object> {

    private static final long serialVersionUID = 6272655632566784880L;

    private static final String MESSAGE = "message";
    private static final String CODE = "code";
    private static final String DATA = "data";
//    {"code":0,"message":"服务器未知异常","data":{}}
    
    // 成功响应
    public static final int CODE_SUCCEED = 200;
    // 失败响应
    public static final int CODE_FAILURE = 400;


    public static BaseResponse build() {
        return new BaseResponse();
    }

    public static BaseResponse success() {
        BaseResponse baseReturn = new BaseResponse();
        baseReturn.put(CODE, CODE_SUCCEED);
        baseReturn.put(MESSAGE, "success");
        return baseReturn;
    }

    public static BaseResponse success(String message) {
        BaseResponse baseReturn = new BaseResponse();
        baseReturn.put(CODE, CODE_SUCCEED);
        baseReturn.put(MESSAGE, message);
        return baseReturn;
    }

    public static BaseResponse fail() {
        BaseResponse baseReturn = new BaseResponse();
        baseReturn.put(CODE, CODE_FAILURE);
        baseReturn.put(MESSAGE, "fail");
        return baseReturn;
    }

    public static BaseResponse fail(String message) {
        BaseResponse baseReturn = new BaseResponse();
        baseReturn.put(CODE, CODE_FAILURE);
        baseReturn.put(MESSAGE, message);
        return baseReturn;
    }

    public BaseResponse code(int state) {
        this.put(CODE, state);
        return this;
    }

    public BaseResponse message(String message) {
        this.put(MESSAGE, message);
        return this;
    }
    
    public BaseResponse data(Object data) {
        this.put(DATA, data);
        return this;
    }

    public BaseResponse add(String key, Object value) {
        this.put(key, value);
        return this;
    }
}