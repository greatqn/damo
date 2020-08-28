package com.damo.generator.dao;

import com.damo.generator.model.GidWorkerDAO;
import org.apache.ibatis.annotations.Param;

public interface GidWorkerMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(GidWorkerDAO record);

    int insertSelective(GidWorkerDAO record);

    GidWorkerDAO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GidWorkerDAO record);

    int updateByPrimaryKey(GidWorkerDAO record);

    GidWorkerDAO selectByEmpty();

    int updateByLock(@Param("worker")GidWorkerDAO worker, @Param("old")String old);
}