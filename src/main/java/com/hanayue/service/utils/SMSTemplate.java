package com.hanayue.service.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SMSTemplate {
    private String to;
    private String appId;
    private String templateId;
    private List<String> datas;

    private SMSTemplate(String to, String appId, String templateId, List<String> datas){
        this.to = to;
        this.appId = appId;
        this.templateId = templateId;
        this.datas = datas;
    }

    public static SMSTemplate getInstance(String to, String appId, String templateId, List<String> datas){
        return new SMSTemplate(to, appId, templateId, datas);
    }

    public JSONObject toJSON(){
        JSONObject res = new JSONObject();
        res.put("to", this.to);
        res.put("appId", this.appId);
        res.put("templateId", this.templateId);
        res.put("datas", this.datas);
        return res;
    }
}
