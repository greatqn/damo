package com.damo.generator;

import com.damo.Application;
import com.damo.generator.enums.SequenceIdTypeEnum;
import com.damo.generator.event.SequenceEvent;
import com.damo.generator.model.GidConfigDAO;
import com.damo.generator.service.SequenceService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class SequenceServiceTest {

    @DubboReference
    SequenceService sequenceService;

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
