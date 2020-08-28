package com.damo.generator.service;

import com.damo.generator.model.GidConfigDAO;

import java.util.UUID;

public class UuidWorker implements ISeqWorker {

    private GidConfigDAO config;
    public void setConfig(GidConfigDAO config) {
        this.config = config;
    }

    @Override
    public String getNext() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    @Override
    public void backSn(String sn) {

    }
}
