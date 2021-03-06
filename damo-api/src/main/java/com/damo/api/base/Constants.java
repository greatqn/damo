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

public class Constants {

    /**
     * jwt
     */
    public static final String JWT_ID = "jwt";
    public static final String JWT_SECRET = "8463fcb717439ed602b22dca994f4376";
    public static final int JWT_TTL = 60 * 60 * 1000; // millisecond (60分钟)
    public static final int JWT_REFRESH_INTERVAL = 55 * 60 * 1000; // millisecond(55分钟)
    public static final int JWT_REFRESH_TTL = 12 * 60 * 60 * 1000; // millisecond(12小时)

}