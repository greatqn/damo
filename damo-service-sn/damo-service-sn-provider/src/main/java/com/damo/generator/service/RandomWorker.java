package com.damo.generator.service;


import com.damo.generator.model.GidConfigDAO;

public class RandomWorker implements ISeqWorker {

    private GidConfigDAO config;
    public void setConfig(GidConfigDAO config) {
        this.config = config;
    }

    @Override
    public String getNext() {
        return RandomService.getRanDomStr(config.getSn().intValue(),config.getSeqType());
    }

    @Override
    public void backSn(String sn) {

    }
}
