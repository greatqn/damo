package com.damo.generator.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * gid_config
 * @author
 */
@Data
public class GidWorkerDAO implements Serializable {
    private Integer id;

    private String ip;

    private String pid;

    /**
     * app环境
     */
    private String info;

    private String operator;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtCreate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtModify;

    private static final long serialVersionUID = 1L;

}