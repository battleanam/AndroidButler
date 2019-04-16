package com.hanayue.service.common.controller;

import com.hanayue.service.common.dao.BaseDao;
import com.hanayue.service.common.model.ResMessage;
import com.hanayue.service.common.service.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基本控制器
 *
 * @param <T>       子类对应的实体类
 * @param <SERVICE> 注入的service层服务
 */
@RestController
public class BaseController<T, SERVICE extends BaseService<T,  BaseDao<T>>> {

    @Resource
    private SERVICE service;

    /**
     * 获取service便于此类的子类可以获取到此类中的service
     *
     * @return this.service
     */
    public SERVICE getService() {
        return this.service;
    }

    /**
     * 通过主键ID查询一条记录
     *
     * @param id 要查询的ID
     * @return 返回数据库中的一条记录
     */
    @RequestMapping("selectOneById")
    public ResMessage selectOneById(String id) {
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        T data = service.selectOneByParam(param);
        if (data != null) {
            return ResMessage.getSuccessMessage("查询成功", data);
        } else {
            return ResMessage.getNullMessage("查询为空", null);
        }
    }

    /**
     * 通过参数查询数据库中的一条记录
     *
     * @param param 查询的条件 各个条件之间是且的关系
     * @return 返回查询到的一条记录
     */
    @RequestMapping("selectOneByParam")
    public ResMessage selectOneByParam(Map param) {
        T data = service.selectOneByParam(param);
        if (data != null) {
            return ResMessage.getSuccessMessage("查询成功", data);
        } else {
            return ResMessage.getNullMessage("查询为空", null);
        }
    }

    /**
     * 通过参数查询数据库中的多条记录
     *
     * @param param 查询的条件 各个条件之间是且的关系
     * @return 返回查询到的多条记录
     */
    @RequestMapping("selectListByParam")
    public ResMessage selectListByParam(Map param) {
        List<T> data = service.selectListByParam(param);
        if (data != null) {
            return ResMessage.getSuccessMessage("查询成功", data);
        } else {
            return ResMessage.getNullMessage("查询为空", null);
        }
    }

    /**
     * 向数据库中插入一条记录
     *
     * @param model 插入的数据
     * @return 返回插入数据的条数  如果不为0 则表示
     */
    @RequestMapping("insertOne")
    public ResMessage insertOne(Map model) {
        int data = service.insertOne(model);
        if (data > 0) {
            return ResMessage.getSuccessMessage("添加成功", data);
        } else {
            return ResMessage.getNullMessage("插入失败", data);
        }
    }

    /**
     * 更改数据库中的多条数据
     * @param param 查询条件 决定那条记录会被更新
     * @return 被更改的数据的条数
     */
    @RequestMapping("update")
    public ResMessage update(Map param) {
        if(param.get("id") == null){
            return ResMessage.getErrorMessage();
        }
        int data = service.update(param);
        if (data > 0) {
            return ResMessage.getSuccessMessage("修改成功", data);
        } else {
            return ResMessage.getNullMessage("修改失败", data);
        }
    }

    /**
     * 删除数据库中的某些符合条件的数据
     *
     * @param param 查询条件 决定那条数据将会被删除
     * @return 被删除的数据的条数
     */
    @RequestMapping("delete")
    public ResMessage delete(Map param) {
        int data = service.delete(param);
        if(data > -1){
            return ResMessage.getSuccessMessage("删除成功", data);
        }else {
            return ResMessage.getNullMessage("删除失败", data);
        }
    }

    /**
     * 删除数据库中的某条数据
     * @param id 查询条件 决定那条数据将会被删除
     * @return 被删除的数据的条数
     */
    @RequestMapping("deleteById")
    public ResMessage deleteById(String id) {
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        int data = service.delete(param);
        if(data > -1){
            return ResMessage.getSuccessMessage("删除成功", data);
        }else {
            return ResMessage.getNullMessage("删除失败", data);
        }
    }

    /**
     * 查询数据库中所有数据的总量
     *
     * @return 数据库中所有数据的总量
     */
    @RequestMapping("count")
    public int count() {
        return service.count();
    }

    /**
     * 查询数据库中符合条件的数据的总量
     * @return 数据库中符合条件的数据的总量
     */
    public int countByParam(Map param){
        return service.countByParam(param);
    }
}
