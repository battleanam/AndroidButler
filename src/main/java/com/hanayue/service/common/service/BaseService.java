package com.hanayue.service.common.service;

import com.hanayue.service.common.dao.BaseDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基础service
 * @param <T> 子类对应的实体类
 * @param <DAO> 注入的dao层类
 */
@Service
public class BaseService<T,DAO extends BaseDao<T>> {

    @Resource
    private DAO dao;

    /**
     * 获取dao便于此类的子类可以获取到此类中的dao
     * @return this.dao
     */
    public DAO getDao(){
        return this.dao;
    }

    /**
     * 通过主键ID查询一条记录
     * @param id 要查询的ID
     * @return 返回数据库中的一条记录
     */
    public T selectOneById(String id){
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        return dao.selectOneByParam(param);
    }

    /**
     * 通过参数查询数据库中的一条记录
     * @param param 查询的条件 各个条件之间是且的关系
     * @return 返回查询到的一条记录
     */
    public T selectOneByParam(Map param){
        return dao.selectOneByParam(param);
    }

    /**
     * 通过参数查询数据库中的多条记录
     * @param param 查询的条件 各个条件之间是且的关系
     * @return 返回查询到的多条记录
     */
    public List<T> selectListByParam(Map param){
        return dao.selectListByParam(param);
    }

    /**
     * 向数据库中插入一条记录
     * @param model 插入的数据
     * @return 返回插入数据的条数  如果不为0 则表示
     */
    public int insertOne(Map model){
        return dao.insertOne(model);
    }

    /**
     * 更改数据库中的数据
     * @param param 查询条件 决定那条记录会被更新
     * @return 被更改的数据的条数
     */
    public int update(Map param){
        return dao.update(param);
    }

    /**
     * 删除数据库中的某些符合条件的数据
     * @param param 查询条件 决定那条数据将会被删除
     * @return 被删除的数据的条数
     */
    public int delete(Map param){
        return dao.delete(param);
    }

    /**
     * 删除数据库中的某条数据
     * @param id 查询条件 决定那条数据将会被删除
     * @return 被删除的数据的条数
     */
    public int deleteById(String id){
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        return dao.delete(param);
    }

    /**
     * 查询数据库中所有数据的总量
     * @return
     */
    public int count(){
        return dao.count();
    }

    /**
     * 查询数据库中符合条件的数据的总量
     * @return 数据库中符合条件的数据的总量
     */
    public int countByParam(Map param){
        return dao.countByParam(param);
    }
}
