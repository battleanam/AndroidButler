package com.hanayue.service.common.service;

import com.alibaba.fastjson.JSONObject;
import com.hanayue.service.utils.HttpUtils;
import com.hanayue.service.utils.MD5;
import com.hanayue.service.utils.SMSTemplate;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MessageService {

    private final String ACOUNT_SID = "8aaf070869dc0b88016a11f437d6188c";
    private final String AUTH_TOKEN = "6042b82d44be49d7a6246625f51e0feb";
    private final String APP_ID = "8aaf070869dc0b88016a11f438291892";


    /**
     * 发送短信验证码
     * @param template
     * @return
     */
    public Call sendCode(JSONObject template){
        Random random = new Random();
        String[] code = {random.nextInt(10)+"", "5"};
        for(int i = 0; i < 5; i++){
            code[0] += (random.nextInt(10)+"");
        }
        template.put("appId", APP_ID);
        template.put("templateId", '1');
        template.put("datas", code);
        System.out.println(url());
        RequestBody body = RequestBody
                .create(MediaType.parse("application/json; charset=utf-8"), template.toJSONString());
        Request request = new Request.Builder()
                .url("https://app.cloopen.com:8883"+url())
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", authorization())
                .addHeader("Content-Length", "256")
                .post(body)
                .build();
        return HttpUtils.post(request);
    }

    private String authorization(){
        String res = ACOUNT_SID+":"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        return Base64.getEncoder().encodeToString(res.getBytes());
    }

    private String url(){
        return "/2013-12-26/Accounts/"+ACOUNT_SID+"/SMS/TemplateSMS?sig="+md5()+"";
    }

    private String md5(){
        String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        return MD5.md5Only(ACOUNT_SID+AUTH_TOKEN+date).toUpperCase();
    }

    //    public Map sendMessage(){
//        Map result = null;
//
//        //初始化SDK
//        CCPRestSmsSDK restAPI = new CCPRestSmsSDK();
//
//        //******************************注释*********************************************
//        //*初始化服务器地址和端口                                                       *
//        //*沙盒环境（用于应用开发调试）：restAPI.init("sandboxapp.cloopen.com", "8883");*
//        //*生产环境（用户应用上线使用）：restAPI.init("app.cloopen.com", "8883");       *
//        //*******************************************************************************
//        restAPI.init("app.cloopen.com", "8883");
//
//        //******************************注释*********************************************
//        //*初始化主帐号和主帐号令牌,对应官网开发者主账号下的ACCOUNT SID和AUTH TOKEN     *
//        //*ACOUNT SID和AUTH TOKEN在登陆官网后，在“应用-管理控制台”中查看开发者主账号获取*
//        //*参数顺序：第一个参数是ACOUNT SID，第二个参数是AUTH TOKEN。                   *
//        //*******************************************************************************
//        restAPI.setAccount("8aaf070869dc0b88016a11f437d6188c", "6042b82d44be49d7a6246625f51e0feb");
//
//
//        //******************************注释*********************************************
//        //*初始化应用ID                                                                 *
//        //*测试开发可使用“测试Demo”的APP ID，正式上线需要使用自己创建的应用的App ID     *
//        //*应用ID的获取：登陆官网，在“应用-应用列表”，点击应用名称，看应用详情获取APP ID*
//        //*******************************************************************************
//        restAPI.setAppId("8aaf070869dc0b88016a11f438291892");
//
//
//        //******************************注释****************************************************************
//        //*调用发送模板短信的接口发送短信                                                                  *
//        //*参数顺序说明：                                                                                  *
//        //*第一个参数:是要发送的手机号码，可以用逗号分隔，一次最多支持100个手机号                          *
//        //*第二个参数:是模板ID，在平台上创建的短信模板的ID值；测试的时候可以使用系统的默认模板，id为1。    *
//        //*系统默认模板的内容为“【云通讯】您使用的是云通讯短信模板，您的验证码是{1}，请于{2}分钟内正确输入”*
//        //*第三个参数是要替换的内容数组。																														       *
//        //**************************************************************************************************
//
//        //**************************************举例说明***********************************************************************
//        //*假设您用测试Demo的APP ID，则需使用默认模板ID 1，发送手机号是13800000000，传入参数为6532和5，则调用方式为           *
//        //*result = restAPI.sendTemplateSMS("13800000000","1" ,new String[]{"6532","5"});																		  *
//        //*则13800000000手机号收到的短信内容是：【云通讯】您使用的是云通讯短信模板，您的验证码是6532，请于5分钟内正确输入     *
//        //*********************************************************************************************************************
//        return restAPI.sendTemplateSMS("17865316810","1" ,new String[]{"6532","5"});
//    }
}
