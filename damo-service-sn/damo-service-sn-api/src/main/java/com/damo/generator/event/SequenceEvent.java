package com.damo.generator.event;


import lombok.Data;

import java.io.Serializable;

@Data
public class SequenceEvent implements Serializable {
    public String code;
    public String sn;

    /**
     * 日期格式化:yyyyMM；用于按月生成序号的模式。
     */
    private String dateFormat;

    /**
     * 序号类型（1:自增,2:随机,3:雪花,4:uuid,5:redis）
     */
    private Byte idType;

    /**
     * 序号类型（1:纯数字,2:纯字母,3:混排）
     */
    private Byte seqType;
    /**
     * yyyyMM%06d
     */
    private String idFormat;
}

