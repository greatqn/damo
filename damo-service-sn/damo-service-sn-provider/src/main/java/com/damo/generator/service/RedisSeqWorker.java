package com.damo.generator.service;

import com.damo.generator.model.GidConfigDAO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;

public class RedisSeqWorker implements ISeqWorker {
    private GidConfigDAO config;
//    private RedisCacheClient jedis;
    private RedisTemplate jedis;
    @Override
    public String getNext() {

        Date dt = new Date();
        Long point = 0L;
        String key = getRedisKey();
        //日期限制
        if(StringUtils.isNotEmpty(config.getDateFormat())){
            key = key + DateFormatUtils.format(dt,config.getDateFormat());
        }
        point = jedis.opsForValue().increment(key);//jedis.incr(key);


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

    }

    private String getRedisKey() {
        return "seq:"+config.getCode();
    }

    public void setConfig(GidConfigDAO config) {
        this.config = config;
    }

    public void setJedis(RedisTemplate jedis) {
        this.jedis = jedis;
    }

}
