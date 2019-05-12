package com.hanayue.service.common.service;

import com.alibaba.fastjson.JSONObject;
import com.hanayue.service.utils.HttpUtils;
import com.hanayue.service.utils.MD5;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MessageService {

    private final String ACOUNT_SID = "8aaf070869dc0b88016a11f437d6188c";
    private final String AUTH_TOKEN = "6042b82d44be49d7a6246625f51e0feb";
    private final String APP_ID = "8aaf070869dc0b88016a11f438291892";
    public static Map<String, String> codeMap = new HashMap<>();


    /**
     * 发送短信验证码
     *
     * @param template
     * @return
     */
    public Call sendCode(JSONObject template) {
        Random random = new Random();
        String[] code = {random.nextInt(10) + "", "3"};
        for (int i = 0; i < 5; i++) {
            code[0] += (random.nextInt(10) + "");
        }
        template.put("appId", APP_ID);
        template.put("templateId", '1');
        template.put("datas", code);
        codeMap.put(template.getString("to"), code[0]);
        System.out.println(url());
        RequestBody body = RequestBody
                .create(MediaType.parse("application/json; charset=utf-8"), template.toJSONString());
        Request request = new Request.Builder()
                .url("https://app.cloopen.com:8883" + url())
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", authorization())
                .addHeader("Content-Length", "256")
                .post(body)
                .build();
        return HttpUtils.post(request);
    }

    private String authorization() {
        String res = ACOUNT_SID + ":" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        return Base64.getEncoder().encodeToString(res.getBytes());
    }

    private String url() {
        return "/2013-12-26/Accounts/" + ACOUNT_SID + "/SMS/TemplateSMS?sig=" + md5() + "";
    }

    private String md5() {
        String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        return MD5.md5Only(ACOUNT_SID + AUTH_TOKEN + date).toUpperCase();
    }

}
