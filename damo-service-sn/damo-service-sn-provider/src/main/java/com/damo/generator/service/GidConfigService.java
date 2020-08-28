package com.damo.generator.service;

import com.damo.generator.dao.GidConfigMapper;
import com.damo.generator.model.GidConfigDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class GidConfigService {
    @Autowired
    GidConfigMapper gidConfigMapper;

    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public GidConfigDAO findByCode(String code) {
        return gidConfigMapper.selectByCode(code);
    }

    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public void insert(GidConfigDAO config) {
        gidConfigMapper.insert(config);
    }

    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public void updateByPrimaryKey(GidConfigDAO config) {
        gidConfigMapper.updateByPrimaryKey(config);
    }

    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public int updateByLockLimit(GidConfigDAO config, String old) {
        return gidConfigMapper.updateByLockLimit(config,old);
    }

    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public int updateByLock(GidConfigDAO config, Long old) {
        return gidConfigMapper.updateByLock(config,old);
    }
}
