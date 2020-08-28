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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONObject;
import com.damo.api.Application;
import com.damo.api.utils.DataPprocess;
import com.damo.api.utils.ReadExcel;
import com.damo.api.utils.StringUtils;

@SpringBootTest(classes = {Application.class}, webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class DemoTest extends AbstractTestNGSpringContextTests {
    private static Logger logger = LoggerFactory.getLogger(DemoTest.class);

    @Value("${test.servicePath}")
    private String servicePath;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static HashMap<String, Object> userMap = new HashMap<String, Object>();

    @BeforeClass
    public void BeforeCalss() throws JSONException {
        logger.info("BeforeCalss");
        DataPprocess.delete("delete from dw_user");
        logger.info("DemoTest.class.getPackage().getName() = {} ", DemoTest.class.getPackage().getName());
        String path = StringUtils.getPath(DemoTest.class.getPackage().getName(), "T011_012_dw_user.xlsx");
        logger.info("path = {}", path);
        ReadExcel.insertForXls(path, "", userMap, "");
    }

    @Test
    public void t1() {
        logger.info("t1 {}", servicePath);
        String sql = "select * from dw_user where user_id = 1274341";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        logger.info(JSONObject.toJSONString(list));
    }
}