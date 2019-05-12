package com.hanayue.service.note.service;

import com.alibaba.fastjson.JSONObject;
import com.hanayue.service.note.dao.NoteDao;
import com.hanayue.service.note.model.Note;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class NoteService {

    @Resource
    private NoteDao noteDao;

    /**
     * 上传便签
     * @param model 上传的便签
     * @return 更新的条数
     */
    public int insertOne(JSONObject model){
        Map<String, Object> countParam = new HashMap<>();
        countParam.put("userId", model.getString("userId"));
        countParam.put("noteId", model.getLongValue("noteId"));
        if(noteDao.countByParam(countParam) > 0){ // 如果数据库中有了 就更新  countByParam是查询数据库中这条便签的数量  大于零说明有这条
            return noteDao.update(model);
        }else { // 没有就添加
            return noteDao.insertOne(model);
        }
    }

    /**
     * 删除某个便签
     * @param model 删除条件
     * @return 删除的条数
     */
    public int delete(JSONObject model){
        Note note = noteDao.selectOneByParam(model);
        Map<String, String> deleteMap = new HashMap<>();
        deleteMap.put("id", note.getId());
        return  noteDao.delete(deleteMap);
    }
}
