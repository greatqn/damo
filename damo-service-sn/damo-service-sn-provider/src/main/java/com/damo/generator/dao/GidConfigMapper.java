package com.damo.generator.dao;


import com.damo.generator.model.GidConfigDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface GidConfigMapper {
//    long countByExample(GidConfigDAOExample example);
//
//    int deleteByExample(GidConfigDAOExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(GidConfigDAO record);

    int insertSelective(GidConfigDAO record);

//    List<GidConfigDAO> selectByExample(GidConfigDAOExample example);

    GidConfigDAO selectByPrimaryKey(Integer id);

//    int updateByExampleSelective(@Param("record") GidConfigDAO record, @Param("example") GidConfigDAOExample example);
//
//    int updateByExample(@Param("record") GidConfigDAO record, @Param("example") GidConfigDAOExample example);

//    int updateByPrimaryKeySelective(GidConfigDAO record);

    @Transactional(propagation= Propagation.REQUIRES_NEW)
    int updateByPrimaryKey(GidConfigDAO record);

    GidConfigDAO selectByCode(@Param("code")String code);

    @Transactional(propagation= Propagation.REQUIRES_NEW)
    int updateByLock(@Param("config")GidConfigDAO record, @Param("old")Long old);

    @Transactional(propagation= Propagation.REQUIRES_NEW)
    int updateByLockLimit(@Param("config")GidConfigDAO config, @Param("old")String old);

    List<GidConfigDAO> getList(Map<String, Object> param);
}