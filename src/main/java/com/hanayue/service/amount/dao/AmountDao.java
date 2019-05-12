package com.hanayue.service.amount.dao;

import com.hanayue.service.amount.model.Amount;
import com.hanayue.service.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmountDao extends BaseDao<Amount> {
}
