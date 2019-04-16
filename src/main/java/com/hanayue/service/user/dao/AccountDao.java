package com.hanayue.service.user.dao;

import com.hanayue.service.common.dao.BaseDao;
import com.hanayue.service.user.model.Account;
import org.apache.ibatis.annotations.Mapper;
import com.hanayue.service.user.model.Profile;

import java.util.Map;

/**
 * 用户账户Dao层接口
 * 用来声明对数据库中Account表的操作
 */
@Mapper
public interface AccountDao extends BaseDao<Account> {
    /**
     * 根据ID查询某条的拓展信息
     * @param id
     * @return
     */
    public Profile selectOnesProfile(String id);

    /**
     * 更新拓展信息
     * @param param
     * @return
     */
    public int updateProfile(Map param);

    public Account doLogin(Map param);
}
