package com.hanayue.service.note.controler;

import com.alibaba.fastjson.JSONObject;
import com.hanayue.service.common.model.ResMessage;
import com.hanayue.service.note.dao.NoteDao;
import com.hanayue.service.note.model.Note;
import com.hanayue.service.note.service.NoteService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/note")
public class NoteController {
    @Resource
    private NoteDao noteDao;

    @Resource
    private NoteService noteService;

    /**
     * 查询已上传的列表
     * @param params 查询条件
     * @return 已上传的便签
     */
    @RequestMapping("selectListByParam")
    public ResMessage selectListByParam(@RequestBody JSONObject params) {
        String userId = params.getString("userId");
        if (userId == null) {
            return ResMessage.getErrorMessage();
        } else {
            List<Note> data = noteDao.selectListByParam(params);
            return ResMessage.getSuccessMessage("查询成功", data);
        }
    }

    /**
     * 备份一个便签
     * @param model 便签的内容
     * @return 备份成功或者失败
     */
    @RequestMapping("insertOne")
    public ResMessage insertOne(@RequestBody JSONObject model) {
        if (isNotFull(model)) {
            return ResMessage.getErrorMessage();
        }else {
            int data = noteService.insertOne(model);
            if(data > 0){
                return ResMessage.getSuccessMessage("上传成功", data);
            }else {
                return ResMessage.getNullMessage("上传失败", data);
            }
        }
    }

    /**
     * 更新某个便签
     * @param model 更新条件
     * @return 更新是否成功
     */
    @RequestMapping("update")
    public ResMessage update(@RequestBody JSONObject model){
        String userId = model.getString("userId");
        long noteId = model.getLongValue("noteId");
        if (model.keySet().size() < 3  || userId == null || noteId == 0L ) {
            return ResMessage.getErrorMessage();
        }else {
            int data = noteDao.update(model);
            if(data > 0){
                return ResMessage.getSuccessMessage("上传成功", data);
            }else {
                return ResMessage.getNullMessage("上传失败", data);
            }
        }
    }

    /**
     * 删除某个便签
     * @param params 删除条件
     * @return 删除成功
     */
    @RequestMapping("delete")
    public ResMessage delete(@RequestBody JSONObject params){
        String userId = params.getString("userId");
        long noteId = params.getLongValue("noteId");
        if (userId == null || noteId == 0L ) {
            return ResMessage.getErrorMessage();
        }else {
            int data = noteService.delete(params);
            if(data > 0){
                return ResMessage.getSuccessMessage("上传成功", data);
            }else {
                return ResMessage.getNullMessage("上传失败", data);
            }
        }
    }

    /**
     * 判断一个便签的内容是否完整
     * @param model 便签
     * @return 是否完整
     */
    private boolean isNotFull(JSONObject model){
        String userId = model.getString("userId");
        long noteId = model.getLongValue("noteId");
        String title = model.getString("title");
        String content = model.getString("content");
        long createTime = model.getLongValue("createTime");
        long noteTime = model.getLongValue("noteTime");
        return userId == null || title == null || content == null || noteId == 0L || createTime == 0L || noteTime == 0L;
    }
}
