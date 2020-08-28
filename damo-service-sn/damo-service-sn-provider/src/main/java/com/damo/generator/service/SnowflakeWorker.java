package com.damo.generator.service;

import com.damo.generator.model.GidConfigDAO;

public class SnowflakeWorker implements ISeqWorker {

    private GidConfigDAO config;
    private Snowflake snowflake;

    public void setConfig(GidConfigDAO config) {
        this.config = config;
    }

    @Override
    public String getNext() {
        return snowflake.next()+"";
    }

    public void setSnowflake(Snowflake snowflake) {
        this.snowflake = snowflake;
    }

    @Override
    public void backSn(String sn) {

    }
}
