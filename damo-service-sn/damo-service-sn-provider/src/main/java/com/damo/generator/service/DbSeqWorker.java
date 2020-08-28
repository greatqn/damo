package com.damo.generator.service;

import com.damo.generator.model.GidConfigDAO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

public class DbSeqWorker implements ISeqWorker {

    private GidConfigDAO config;
    private SequenceService sequenceService;
    private GidConfigService gidConfigService;

    public void setConfig(GidConfigDAO config) {
        this.config = config;
    }

    @Override
    public String getNext() {
        if(StringUtils.isNotEmpty(config.getSnLimit())){
            return getPointLimit().toString();

        }
        Date dt = new Date();
        Long point = 0L;
        //日期限制
        if(StringUtils.isNotEmpty(config.getDateFormat())){
            String dtCode = config.getCode() + DateFormatUtils.format(dt,config.getDateFormat());
            point = Long.valueOf(sequenceService.getNext(dtCode));
        }else{
            point = getPoint();
        }
        //格式限制
        if(StringUtils.isNotEmpty(config.getDateFormat())){
            String sn = String.format(config.getIdFormat(),point);
            sn = DateFormatUtils.format(dt,sn);
            return sn;
        }else{
            return point.toString();
        }
    }

    @Override
    public void backSn(String sn) {
        //乐观锁，重试100次。
        for (int i = 0; i < 100; i++) {
            String old = config.getSnLimit();
            SeqManager sm = new SeqManager(old);
            sm.back(Long.parseLong(sn));
            config.setSnLimit(sm.toString());
            if(gidConfigService.updateByLockLimit(config,old) ==1){
                return ;
            };
            config = gidConfigService.findByCode(config.getCode());
            System.out.println("backSn " + config.getSnLimit() + ":" + i);
        }
        throw new RuntimeException("系统忙");
    }

    private Long getPointLimit() {
        //乐观锁，重试100次。
        for (int i = 0; i < 100; i++) {
            String old = config.getSnLimit();
            SeqManager sm = new SeqManager(old);
            Long sn = sm.next();
            config.setSnLimit(sm.toString());
            if(gidConfigService.updateByLockLimit(config,old) ==1){
                return sn;
            };
            config = gidConfigService.findByCode(config.getCode());
            System.out.println("getPointLimit " + config.getSnLimit() + ":" + i);
        }
        throw new RuntimeException("系统忙");
    }

    private long getPoint() {
        //乐观锁，重试100次。
        for (int i = 0; i < 100; i++) {
            Long old = config.getSn();
            Long sn = old + config.getStep();
            config.setSn(sn);

            if(gidConfigService.updateByLock(config,old) ==1){
                return sn;
            };
            config = gidConfigService.findByCode(config.getCode());
            System.out.println("getPoint " + config.getSn() + ":" + i);
        }
        throw new RuntimeException("系统忙");
    }

    public void setSequenceService(SequenceService sequenceService) {
        this.sequenceService = sequenceService;
    }

    public void setConfigService(GidConfigService gidConfigService) {
        this.gidConfigService = gidConfigService;
    }
}
