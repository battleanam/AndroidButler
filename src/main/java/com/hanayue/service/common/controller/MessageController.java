package com.hanayue.service.common.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hanayue.service.common.model.ResMessage;
import com.hanayue.service.common.service.MessageService;
import okhttp3.Call;
import okhttp3.Response;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
public class MessageController {

    @Resource
    private MessageService messageService;

    @RequestMapping("sendMessage")
    public ResMessage sendMessage(@RequestBody JSONObject template) {
        Call call = messageService.sendCode(template);
        try {
            Response response = call.execute();
            assert response.body() != null;
            JSONObject res = JSON.parseObject(response.body().string());
            if (res.getString("statusCode").equals("000000")) {
                return ResMessage.getSuccessMessage("发送成功", null);
            }else {
                return ResMessage.getNullMessage("发送失败", null);
            }
        } catch (IOException e) {
            return ResMessage.getErrorMessage();
        }
    }

}
