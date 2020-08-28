package com.damo.generator.service;

import com.damo.generator.model.GidConfigDAO;

public class VerifiWorker implements ISeqWorker {

    private GidConfigDAO config;
    public void setConfig(GidConfigDAO config) {
        this.config = config;
    }

    @Override
    public String getNext() {
        return VerifiableSerialService.create(config.getSn().intValue(),config.getStep());
    }

    @Override
    public void backSn(String sn) {

    }
}