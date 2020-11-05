package com.damo.spider;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.damo.api.Application;
import com.damo.testcase.demo.DemoTest;
import org.assertj.core.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * https://zb.oschina.net/projects/list.html
 * 众包大厅的任务
 * applyCount <5 的，有免费报名名额。
 * skillList 技能里含Java
 */
@SpringBootTest(classes = {Application.class}, webEnvironment = WebEnvironment.DEFINED_PORT)
public class Oschina {
    private static Logger logger = LoggerFactory.getLogger(DemoTest.class);

    @Test
    public void 读取10页200条任务_过滤出免费带JAVA的() {
        logger.info("t1");
        RestTemplate restTemplate = new RestTemplate();
        String currentTime = DateUtil.formatAsDatetime(new Date());
        logger.info(currentTime);
        //"2020-09-28+21:16:44";

        ArrayList<String> apps = new ArrayList<>();

        for (int p = 1; p < 11 ; p++) {
            String url = String.format("https://zb.oschina.net/project/contractor-browse-project-and-reward?keyword=&applicationAreas=&sortBy=30&currentTime=%s&pageSize=20&currentPage=%d",
                    currentTime,p);
            String result = restTemplate.getForObject(url, String.class);
            apps.add(result);
        }

        JSONArray dt = trans(apps);
        List list = dt.stream().filter(obj->{
            JSONObject json = (JSONObject)obj;
            return json.getInteger("applyCount")<5;
        }).filter(obj->{
            JSONObject json = (JSONObject)obj;
            JSONArray skillList = json.getJSONArray("skillList");
            return skillList.stream().filter(k->{
                JSONObject skill = (JSONObject)k;
                return skill.getString("value").equals("Java") || skill.getString("value").equals("PHP");
            }).count() > 0;
        }).collect(Collectors.toList());

        for (Object i : list){
            logger.info(printTask(i));
        }
    }

    private String printTask(Object tasko) {
        StringBuffer sb = new StringBuffer();
        JSONObject task = (JSONObject)tasko;
        sb.append(task.getString("publishTime"));
        sb.append("\t");
        sb.append(task.getString("application"));
        sb.append("\t");
        sb.append(task.getString("name"));
        sb.append("\t");
        sb.append(task.getString("applyCount"));
        return sb.toString();
    }


    //    {"isTendencyDistrict":1,
//            "imagePath":"static/project-reward/cover/qiyeyingyong_1.png",
//            "applyCount":0,
//            "statusLastTime":"2020-09-03 14:38:24",
//            "projectType":[],
//        "type":1,
//            "projectNo":"R9949L",
//            "payType":1,
//            "handleStatus":1,
//            "id":17427395,
//            "publishStatus":1,
//            "publishTime":"2020-09-03 14:38:24",
//            "visible":1,
//            "skillList":[{"id":58434, "type":3, "projectId":30586, "value":"PHP"}],
//        "depositMoney":0,
//            "tendencyType":1,
//            "tendencyProvince":"",
//            "roleList":[],
//        "subStatus":1,
//            "budgetMaxByYuan":"10000",
//            "depositHostingStatus":1,
//            "canApplyByPulishTime":true,
//            "userAccountId":94942968,
//            "name":"ChinaSin课件",
//            "budgetMinByYuan":"5000",
//            "status":3,
//            "residentRequire":1,
//            "userAccountIconPath":"group1/M00/02/E4/wKgBDV7IgGaAJssxAABjEd7sZAU324.gif",
//            "userAccountNickname":"陈则太",
//            "serviceFeePayStatus":1,
//            "budgetMax":1000000,
//            "cycle":15,
//            "tendencyCity":"",
//            "top":0,
//            "attachmentVisible":1,
//            "viewCount":1183,
//            "budgetMin":500000,
//            "serviceFeeByYuan":"",
//            "userId":499559991,
//            "application":"企业应用",
//            "auditStatus":2,
//            "attachmentCount":1,
//            "cycleName":"天"}
//
    private JSONArray trans(ArrayList<String> apps) {
        JSONArray datas = new JSONArray();
        for (String s : apps){
            JSONObject json = JSONObject.parseObject(s);
            JSONArray array = json.getJSONObject("data").getJSONArray("data");
            datas.addAll(array);
        }
//        logger.info("size:{}",datas.size());
//        for (Object json : datas){
//            logger.info(JSON.toJSONString(json));
//        }
        return datas;
    }
}
