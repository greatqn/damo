package com.damo.generator;

import com.alibaba.fastjson.JSON;
import com.damo.Application;
import com.damo.generator.dao.GidConfigMapper;
import com.damo.generator.dao.GidWorkerMapper;
import com.damo.generator.enums.SequenceIdTypeEnum;
import com.damo.generator.enums.SequenceTypeEnum;
import com.damo.generator.event.SequenceEvent;
import com.damo.generator.model.GidConfigDAO;
import com.damo.generator.service.SequenceService;
import com.damo.generator.service.Snowflake;
import com.damo.generator.service.VerifiableSerialService;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
@Slf4j
public class SequenceServiceTest {

    @Autowired
    SequenceService sequenceService;

    @Autowired
    GidConfigMapper gidConfigMapper;

    @Autowired
    GidWorkerMapper gidWorkerMapper;


    @Test
    public void T01_基础操作(){
        System.out.println("test");
        String code = UUID.randomUUID().toString();
        GidConfigDAO record = new GidConfigDAO();
        record.setCode(code);
        record.setName("test");
        record.setIdType(SequenceIdTypeEnum.SEQUENCE.getCode());
        record.setSeqType(SequenceTypeEnum.NUMBER.getCode());
        record.setIdFormat("yyyy");
        record.setSn(0L);
        record.setStep(1);
        record.setGmtCreate(new Date());
        record.setGmtModify(new Date());
        int i = gidConfigMapper.insert(record);
        System.out.println(i+"|" + record.getId());

        GidConfigDAO ent = gidConfigMapper.selectByCode(code);
        System.out.println(JSON.toJSONString(ent));

        gidConfigMapper.updateByPrimaryKey(ent);

        gidConfigMapper.deleteByPrimaryKey(2);

        System.out.println(SequenceIdTypeEnum.SEQUENCE);
        System.out.println(SequenceTypeEnum.NUMBER);

        PageHelper.startPage(1, 10);
        PageHelper.orderBy("id desc");
        Map<String, Object> param  = new HashMap<>(3);
        List<GidConfigDAO> list =  gidConfigMapper.getList(param);
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    public void T02_默认取值(){
        String code = UUID.randomUUID().toString();
        System.out.println(sequenceService.getNext(code));
        System.out.println(sequenceService.getNext(code));
        System.out.println(sequenceService.getNext(code));
    }

    @Test
    public void T031_建序号_默认模式(){
        String code = "order_sn";
        sequenceService.createConfig(code);
        testTime(code);
    }

    @Test
    public void T032_建序号_ramdon模式(){
        String code = "order_ramdon";
        GidConfigDAO ent = gidConfigMapper.selectByCode(code);
        if(ent == null){
            GidConfigDAO config = sequenceService.createConfig(code);
            config.setIdType(SequenceIdTypeEnum.RANDOM.getCode());
            config.setSeqType(SequenceTypeEnum.NUMBER.getCode());
            config.setSn(6L);
            sequenceService.updateConfig(config);
        }
        testTime(code);
    }

    @Test
    public void T033_建序号_snow模式(){
        String code = "order_snow";
        GidConfigDAO ent = gidConfigMapper.selectByCode(code);
        if(ent == null){
            GidConfigDAO config = sequenceService.createConfig(code);
            config.setIdType(SequenceIdTypeEnum.SNOWFLAKE.getCode());
            sequenceService.updateConfig(config);
        }
        testTime(code);
        System.out.println(Snowflake.show(6633279246305239040L));
    }

    @Test
    public void T034_建序号_uuid模式(){
        String code = "order_uuid";
        GidConfigDAO ent = gidConfigMapper.selectByCode(code);
        if(ent == null){
            GidConfigDAO config = sequenceService.createConfig(code);
            config.setIdType(SequenceIdTypeEnum.UUID.getCode());
            sequenceService.updateConfig(config);
        }
        testTime(code);
    }

    @Test
    public void T035_建序号_VERIFI模式(){
        String code = "order_verifi";
        GidConfigDAO ent = gidConfigMapper.selectByCode(code);
        if(ent == null){
            GidConfigDAO config = sequenceService.createConfig(code);
            config.setIdType(SequenceIdTypeEnum.VERIFI.getCode());
            config.setSn(9L);
            config.setStep(8);
            sequenceService.updateConfig(config);
        }
        testTime(code);

        System.out.println("ZQTDKR2J8");
        System.out.println("verify: " + VerifiableSerialService.verify("ZQTDKR2J8",true));
        System.out.println("actId: " + VerifiableSerialService.getActId("ZQTDKR2J8"));
    }

    @Test
    public void T04_带格式_db(){
        //汇款单号：年+月+6位流水号
        String code = "order_format_db";
        GidConfigDAO config = sequenceService.createConfig(code);
        config.setDateFormat("yyyyMM");
        config.setIdFormat("yyyyMM%06d");
        sequenceService.updateConfig(config);

        testTime(code);
    }

    @Test
    public void T05_带格式_redis(){
        //汇款单号：年+月+6位流水号
        String code = "order_format_r";
        GidConfigDAO config = sequenceService.createConfig(code);
        config.setIdType(SequenceIdTypeEnum.REDIS.getCode());
        config.setDateFormat("yyyyMM");
        config.setIdFormat("yyyyMM%06d");
        sequenceService.updateConfig(config);

        testTime(code);
    }
    @Test
    public void T05_带格式_db_公文(){
        //公文号。
        String code = "format_gw";
        GidConfigDAO config = sequenceService.createConfig(code);
        config.setDateFormat("yyyy");
        config.setIdFormat("浙发改法字〔yyyy〕%d号");
        sequenceService.updateConfig(config);

        testTime(code);
    }

    @Test
    public void T06_带范围可回退(){
        //公文号。
        String code = "sn_limit";
        GidConfigDAO config = sequenceService.createConfig(code);
//        config.setDateFormat("");
//        config.setIdFormat("");
        config.setSnLimit("1-100");
        sequenceService.updateConfig(config);
        testTime(code);
        sequenceService.backSn(code,"1");
        testTime(code);
        sequenceService.backSn(code,"1");
    }

    @Test
    public void T07_并发测试(){
        //公文号。
        String code = "sn_limit";

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    String sn = sequenceService.getNext(code);
                    System.out.println(Thread.currentThread().getId()+"||"+sn);
                }

            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    sequenceService.backSn(code,"1");
                }

            }
        }).start();

        try {
            Thread.sleep(2000);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Autowired
    com.damo.generator.service.SequenceServiceImpl SequenceServiceImpl;

    @Test
    public void T08_并发测试(){
        //公文号。
        String code = "sn_limit";

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    SequenceServiceImpl.checkMySid();
                }

            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    SequenceServiceImpl.checkMySid();
                }

            }
        }).start();

        try {
            Thread.sleep(2000);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void testTime(String code){
        if(true) {
            long start = System.currentTimeMillis();
            int repeat = 0;
            int totalx = 2;
            int totaly = 2;
            Map<String, String> map = new HashMap<>();
            for (int j = 0; j < totalx; j++) {
                for (int i = 0; i < totaly; i++) {
                    String sn = sequenceService.getNext(code);
                    System.out.println(i + ":" + sn);
                    if (map.containsKey(sn)) {
                        repeat++;
                        continue;
                    }
                    map.put(sn, sn);
                    //System.out.println(code);
                }
            }
            System.out.println("生成码" + (totalx * totaly) + "个，重复" + repeat + "个，重复率" + (repeat * 1.0D / (totalx * totaly)) + "耗时： " + (System.currentTimeMillis() - start) + "ms " + (System.currentTimeMillis() - start)/(totalx * totaly) +"ms/个");
        }

//        try {
//            System.in.read();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
    }

    @Test
    public void T11_格式化序号(){
        SequenceEvent event = new SequenceEvent();
        event.setCode("t11_test");
        event.setIdType((byte)1);
        event.setSeqType((byte)1);
        event.setDateFormat("yyyyMMdd");
        event.setIdFormat("yyyyMMdd%06d");

        System.out.println(sequenceService.getNext(event));
        System.out.println(sequenceService.getNext(event));
        System.out.println(sequenceService.getNext(event));

    }

    @Test
    public void T12_格式化序号(){
        SequenceEvent event = new SequenceEvent();
        event.setCode("t12_test");

        System.out.println(sequenceService.getNext(event));
        System.out.println(sequenceService.getNext(event));
        System.out.println(sequenceService.getNext(event));

    }
}
