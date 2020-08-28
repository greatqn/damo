package com.damo.generator.service;

import com.damo.generator.event.SequenceEvent;
import com.damo.generator.model.GidConfigDAO;

public interface SequenceService {
    /**
     * 创建一个序号。使用默认参数
     * @param code
     * @return
     */
    GidConfigDAO createConfig(String code);
    /**
     * 获取下一个序号
     * @param code
     * @return
     */
    String getNext(String code);

    String getNext(SequenceEvent event);

    /**
     * 获取下一个序号
     * @param code
     * @param number 数量，一般小于1000；
     * @return
     */
    String[] getNexts(String code, Integer number);

    /**
     * 退回未使用的序号
     * @param code
     * @param sn 序号
     */
    void backSn(String code, String sn);


    /**
     * 退回未使用的序号
     * @param code
     * @param sns 序号数组
     */
    void backSns(String code, String[] sns);

    /**
     * 更新配置。
     * @param config
     */
    void updateConfig(GidConfigDAO config);

    /**
     * 获取雪花
     * @return
     */
    Long getSnowId();

}
