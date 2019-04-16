package com.hanayue.service.common.model;

import lombok.Data;

/**
 * 返回消息列表
 */
@Data
public class ResMessage {
    private static final int SUCCESS_CODE = 1000; //成功消息的状态码
    private static final int ERROR_CODE = 400; //参数传递错误消息的状态码
    private static final int NULL_CODE = 1; //查询为空的消息的状态码

    private int code; //状态码  通过状态码判断消息类型
    private String message; //返回的消息
    private Object data; //返回的数据

    ResMessage(int code, String message, Object data){
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 获取一个成功的消息
     * @param message 消息
     * @param data 数据
     * @return 成功的消息
     */
    public static ResMessage getSuccessMessage( String message, Object data){
        return new ResMessage(1000, message, data);
    }

    /**
     * 获取一个失败的消息
     * @return 失败的消息
     */
    public static ResMessage getErrorMessage(){
        return new ResMessage(400, "参数传递错误", null);
    }

    /**
     * 获取一个查询为空的消息
     * @param message 消息
     * @param data 数据
     * @return 查询为空的消息
     */
    public static ResMessage getNullMessage( String message, Object data){
        return new ResMessage(1, message, data);
    }
}
