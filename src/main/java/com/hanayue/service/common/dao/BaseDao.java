package com.hanayue.service.common.dao;

import java.util.List;
import java.util.Map;

/**
 * 基本Dao层接口
 * 这个接口定义了基本对于数据库应有的操作
 */
public interface BaseDao<T> {
    /**
     * 通过参数查询数据库中的一条记录
     *
     * @param param 查询的条件 各个条件之间是且的关系
     * @return 返回查询到的一条记录
     */
    public T selectOneByParam(Map param);

    /**
     * 通过参数查询数据库中的多条记录
     *
     * @param param 查询的条件 各个条件之间是且的关系
     * @return 返回查询到的多条记录
     */
    public List<T> selectListByParam(Map param);

    /**
     * 向数据库中插入一条记录
     *
     * @param model 插入的数据
     * @return 返回插入数据的条数  如果不为0 则表示
     */
    public int insertOne(Map model);

    /**
     * 更改数据库中的一条数据
     *
     * @param param 查询条件 决定那条记录会被更新
     * @return 被更改的数据的条数
     */
    public int update(Map param);

    /**
     * 删除数据库中的某些符合条件的数据
     *
     * @param param 查询条件 决定那条数据将会被删除
     * @return 被删除的数据的条数
     */
    public int delete(Map param);

    /**
     * 查询数据库中所有数据的总量
     *
     * @return 数据库中所有数据的总量
     */
    public int count();

    /**
     * 查询数据库中符合条件的数据的总量
     * @return 数据库中符合条件的数据的总量
     */
    public int countByParam(Map param);

}
