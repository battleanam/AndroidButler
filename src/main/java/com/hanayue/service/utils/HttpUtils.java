package com.hanayue.service.utils;

import okhttp3.*;

public class HttpUtils {
    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    /**
     * post请求
     */
    public static Call post(String url, String json){
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        return client.newCall(request);
    }

    public static Call post(Request request){
        OkHttpClient client = new OkHttpClient();
        return client.newCall(request);
    }
}
