package com.damo.generator.model;


import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * gid_config
 * @author 
 */
@Data
public class GidConfigDAO implements Serializable {

    private Integer id;

    private String name;

    private String code;

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
     * 暂不用
     */
    private String idKey;

    /**
     * yyyyMM%06d
     */
    private String idFormat;

    /**
     * 累计值
     */
    private Long sn;

    /**
     * 取值范围 1-100
     */
    private String snLimit;

    /**
     * 步长(默认为1)
     * 随机时，存长度。
     */
    private Integer step;

    private String operator;
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date gmtCreate;
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date gmtModify;

    private static final long serialVersionUID = -3712358882707582824L;

}