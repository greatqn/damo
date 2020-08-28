package com.damo.generator.service;

import com.damo.generator.dao.GidWorkerMapper;
import com.damo.generator.enums.SequenceIdTypeEnum;
import com.damo.generator.enums.SequenceTypeEnum;
import com.damo.generator.event.SequenceEvent;
import com.damo.generator.model.GidConfigDAO;
import com.damo.generator.model.GidWorkerDAO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


import javax.annotation.PostConstruct;
import java.lang.management.ManagementFactory;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *  快速开始：
 *  取序号：SequenceService.getNext(code);
 *  说明：code在库里不存在时，会新建一个。然后累加。
 *
 *  GidConfigDAO 配置表(gid_config) 字段说明：
 *  code 唯一标识。可用模块+业务表达，VARCHAR(64)
 *  name 中文名
 *  idType id类型对应 SequenceIdTypeEnum
 *  sn 存当前序号值
 *  snLimit 序号限制值，比sn优先级高。有值时，sn无效。
 *  dateFormat 日期格式，配置循环的Key。比如按年，按月，按小时。
 *  idFormat 序号输出的格式。使用了String.format加SimpleDateFormat进行格式化输出。'Y'不转意
 *  step 步长；(VERIFI，RANDOM时表示长度)
 *  seqType 序号类型对应 SequenceTypeEnum (随机数时有用)
 *
 *  case1:业务单号：年+月+6位流水号；效果：202002000001
 *  config.setIdType(SequenceIdTypeEnum.REDIS.getCode());  //SEQUENCE存数据库，REDIS性能更好，不怕并发。
 *  config.setDateFormat("yyyyMM");  //到月就按月自增，到小时就按小时自增
 *  config.setIdFormat("yyyyMM%06d");  //不足6位补零。自增超过6位时，会超长。
 *
 *  case2:随机数；效果：026971，可用于短信验证码，图形验证码
 *  config.setIdType(SequenceIdTypeEnum.RANDOM.getCode());
 *  config.setSeqType(SequenceTypeEnum.NUMBER.getCode()); //数字，字母，混编 三种可选
 *  config.setSn(6L);  //长度
 *
 *  case3:兑换券；效果：ZKU4Z4RCT
 *  config.setIdType(SequenceIdTypeEnum.VERIFI.getCode());
 *  config.setSn(9L);  //活动码，可校验。
 *  config.setStep(8);  //长度
 *  VerifiableSerialService.getActId("ZKU4Z4RCT") 可以读出9；
 *  VerifiableSerialService.verify("ZKU4Z4RCT"，true) 可校验券码是否有效。
 *
 *  case4:雪花；效果：6632827278378307584 2020-02-11 10:47:35:137,41,0
 *  config.setIdType(SequenceIdTypeEnum.SNOWFLAKE.getCode());
 *  Snowflake.show(id) 可以解析出时间，机器码，序号。
 *
 *  case5:uuid；效果：32位 a2e7eabb25a64600b9589d9ab952afbb
 *  config.setIdType(SequenceIdTypeEnum.UUID.getCode());
 *  调用UUID.randomUUID()
 *
 *  case6:可退回序号；比如业务要求序号是连续的，有的序号暂时领用了却没有实际使用时，需要退回给下一次使用。
 *  ent.setIdType(SequenceIdTypeEnum.SEQUENCE.getCode());
 *  config.setSnLimit("1-100"); //序号范围，用完报错。VARCHAR(5000)，如果序号太过碎片化，字段超长时会有异常。
 *  取号：SequenceService.getNext(code);
 *  退回：SequenceService.backSn(code,"1");
 *
 *
 */
@DubboService()
@Component
@Slf4j
public class SequenceServiceImpl implements SequenceService {

    @Autowired
    GidConfigService gidConfigService;

    @Autowired
    GidWorkerMapper gidWorkerMapper;

    @Autowired
    RedisTemplate redisTemplate;

    private ScheduledExecutorService scheduledExecutorService;
    private Snowflake snowflake;
    private GidWorkerDAO worker;

    @PostConstruct
    public void init() {
        log.info("SequenceServiceImpl init");
        scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
        //心跳
        scheduledExecutorService.scheduleAtFixedRate(new Runnable(){
            @Override
            public void run() {
                checkMySid();
            }},0,300,TimeUnit.SECONDS);

        log.info("SequenceServiceImpl init end");
    }

    /**
     * 获取序号生成编码。1-1024台。启动心跳。
     * @return
     */
    public void checkMySid() {
//        GidWorkerDAO worker = null;
        if(worker == null){
            worker = gidWorkerMapper.selectByEmpty();
            if(worker == null){
                initWoker();
                worker = gidWorkerMapper.selectByEmpty();
            }
        }
        int result = 0;
        for (int i = 0; i < 10; i++) {
            String old = worker.getOperator();
            worker.setOperator(UUID.randomUUID().toString());
//            worker.setIp(NetworkUtil.getHostIp());
            worker.setIp("localhost");
            worker.setPid(ManagementFactory.getRuntimeMXBean().getName());
            worker.setInfo("schedule " + i);
            int k = gidWorkerMapper.updateByLock(worker,old);
            if(k==1) {
                result = worker.getId() - 1;
                snowflake = new Snowflake(result);
                break;
            }else{
                worker = gidWorkerMapper.selectByEmpty();
                log.info("checkMySid for " + i + "|" + worker.getId());
            }
        }
        log.info("checkMySid end " + worker.getId());
    }

    private void initWoker() {
        for (int i = 0; i < 1024; i++) {
            GidWorkerDAO record = new GidWorkerDAO();
            record.setInfo("info");
            record.setPid("pid");
            record.setIp("ip");
            record.setOperator(UUID.randomUUID().toString());
            record.setId(i+1);
            gidWorkerMapper.insert(record);
            System.out.println(i+"|" + record.getId());
        }
    }

    @Override
//    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public GidConfigDAO createConfig(String code) {
        GidConfigDAO config = gidConfigService.findByCode(code);
        if(config != null){
            return config;
        }
        GidConfigDAO ent = new GidConfigDAO();
        ent.setCode(code);
        ent.setName("默认名");
        ent.setStep(1);
        ent.setSn(0L);
        ent.setIdType(SequenceIdTypeEnum.SEQUENCE.getCode());
        ent.setSeqType(SequenceTypeEnum.NUMBER.getCode());

        gidConfigService.insert(ent);
        return ent;
    }

    public GidConfigDAO createConfig(SequenceEvent event) {
        GidConfigDAO config = gidConfigService.findByCode(event.getCode());
        if(config != null){
            return config;
        }
        GidConfigDAO ent = new GidConfigDAO();
        ent.setCode(event.getCode());
        ent.setName("默认名");
        ent.setStep(1);
        ent.setSn(0L);

        ent.setIdType(event.getIdType()==null ? SequenceIdTypeEnum.SEQUENCE.getCode() : event.getIdType());
        ent.setSeqType(event.getSeqType()==null ? SequenceTypeEnum.NUMBER.getCode() : event.getSeqType());
        ent.setDateFormat(event.getDateFormat());
        ent.setIdFormat(event.getIdFormat());
        gidConfigService.insert(ent);
        return ent;
    }


    @Override
//    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public String getNext(String code) {
        GidConfigDAO config = createConfig(code);
        ISeqWorker worker = getSequenceWorker(config);
        return worker.getNext();
    }

    @Override
    public String getNext(SequenceEvent event) {
        GidConfigDAO config = createConfig(event);
        ISeqWorker worker = getSequenceWorker(config);
        return worker.getNext();
    }

    @Override
    public Long getSnowId(){
        return snowflake.next();
    }

    private ISeqWorker getSequenceWorker(GidConfigDAO config) {
        switch (SequenceIdTypeEnum.getByCode(config.getIdType()) ){
            case SEQUENCE://本地，自增长，带步长，可退id；
                DbSeqWorker dworker = new DbSeqWorker();
                dworker.setConfig(config);
                dworker.setConfigService(gidConfigService);
                dworker.setSequenceService(this);
                return dworker;
            case RANDOM://随机。
                RandomWorker rworker = new RandomWorker();
                rworker.setConfig(config);
                return rworker;
            case VERIFI://带校验
                VerifiWorker vworker = new VerifiWorker();
                vworker.setConfig(config);
                return vworker;
            case SNOWFLAKE://雪花
                SnowflakeWorker sworker = new SnowflakeWorker();
                sworker.setConfig(config);
                sworker.setSnowflake(snowflake);
                return sworker;
            case UUID://uuid。不带-
                UuidWorker uworker = new UuidWorker();
                return uworker;
            case REDIS://按reids_key，自增，
                RedisSeqWorker worker = new RedisSeqWorker();
                worker.setConfig(config);
                worker.setJedis(redisTemplate);
                return worker;
        }
        return null;
    }

    @Override
    public String[] getNexts(String code, Integer number) {
        GidConfigDAO config = createConfig(code);
        ISeqWorker worker = getSequenceWorker(config);
        String[] result = new String[number];
        for (int i = 0; i <number; i++) {
            result[i] = worker.getNext();
        }
        return result;
    }

    @Override
    public void backSn(String code, String sn) {
        GidConfigDAO config = gidConfigService.findByCode(code);
        ISeqWorker worker = getSequenceWorker(config);
        worker.backSn(sn);
    }

    @Override
    public void backSns(String code, String[] sns) {
        GidConfigDAO config = gidConfigService.findByCode(code);
        ISeqWorker worker = getSequenceWorker(config);
        for (int i = 0; i < sns.length; i++) {
            worker.backSn(sns[i]);
        }
    }

    @Override
    public void updateConfig(GidConfigDAO config) {
        gidConfigService.updateByPrimaryKey(config);
    }

}

