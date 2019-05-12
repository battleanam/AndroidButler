package com.hanayue.service.user.service;

import com.hanayue.service.user.dao.AccountDao;
import com.hanayue.service.user.model.Account;
import com.hanayue.service.utils.MD5;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户服务类
 */
@Service
public class AccountService {

    @Resource
    private AccountDao accountDao;

    /**
     * 通过主键ID查询一条记录
     * @param id 要查询的ID
     * @return 返回数据库中的一条记录
     */
    public Account selectOneById(String id){
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        return accountDao.selectOneByParam(param);
    }

    /**
     * 通过参数查询数据库中的一条记录
     * @param param 查询的条件 各个条件之间是且的关系
     * @return 返回查询到的一条记录
     */
    public Account selectOneByParam(Map param){
        return accountDao.selectOneByParam(param);
    }

    /**
     * 通过参数查询数据库中的多条记录
     * @param param 查询的条件 各个条件之间是且的关系
     * @return 返回查询到的多条记录
     */
    public List<Account> selectListByParam(Map param){
        return accountDao.selectListByParam(param);
    }

    /**
     * 向数据库中插入一条记录
     * @param model 插入的数据
     * @return 返回插入数据的条数  如果不为0 则表示
     */
    public int insertOne(Map model){
        return accountDao.insertOne(model);
    }

    /**
     * 更改数据库中的数据
     * @param param 查询条件 决定那条记录会被更新
     * @return 被更改的数据的条数
     */
    public int update(Map param){
        return accountDao.update(param);
    }

    /**
     * 删除数据库中的某些符合条件的数据
     * @param param 查询条件 决定那条数据将会被删除
     * @return 被删除的数据的条数
     */
    public int delete(Map param){
        return accountDao.delete(param);
    }

    /**
     * 删除数据库中的某条数据
     * @param id 查询条件 决定那条数据将会被删除
     * @return 被删除的数据的条数
     */
    public int deleteById(String id){
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        return accountDao.delete(param);
    }

    /**
     * 查询数据库中所有数据的总量
     * @return
     */
    public int count(){
        return accountDao.count();
    }

    /**
     * 查询数据库中符合条件的数据的总量
     * @return 数据库中符合条件的数据的总量
     */
    public int countByParam(Map param){
        return accountDao.countByParam(param);
    }
    /**
     * 更新某个拓展资料
     * @param param 更新的内容
     * @return 被修改的记录条数
     */
    public int updateProfile(Map param){
        return  accountDao.updateProfile(param);
    }

    /**
     * 执行用户登陆验证
     * @param param 用户的登陆信息
     * @return 返回登陆用户的基本信息
     */
    public Account doLogin(Map param){
        String password = String.valueOf(param.get("password"));
        Map<String, Object> queryParam = new HashMap<>();
        queryParam.put("account",param.get("account"));
        queryParam.put("password", MD5.md5(password));
        return accountDao.doLogin(queryParam);
    }

    /**
     * 执行用户注册
     * @param param 用户的注册信息
     * @return 注册后的基本信息
     */
    public int doRegister(Map<String, Object> param){
        param.replace("password", MD5.md5(String.valueOf(param.get("password"))));
        return accountDao.insertOne(param);
    }
}
