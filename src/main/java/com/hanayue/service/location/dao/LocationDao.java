package com.hanayue.service.location.dao;

import com.hanayue.service.common.dao.BaseDao;
import com.hanayue.service.location.model.City;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LocationDao extends BaseDao<City>{
}
