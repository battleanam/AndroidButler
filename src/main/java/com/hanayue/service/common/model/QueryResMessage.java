package com.hanayue.service.common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class QueryResMessage extends ResMessage{

    private int count; //查询出记录的数量
    private int total; //数据库中的总数

    private QueryResMessage(int code, String message, Object data, int count, int total) {
        super(code, message, data);
        this.count = count;
        this.total = total;
    }

    /**
     * 获取一个成功的消息
     * @param message 消息
     * @param data 数据
     * @return 成功的消息
     */
    public static QueryResMessage getSuccessMessage( String message, Object data, int count, int total){
        return new QueryResMessage(1000, message, data, count, total);
    }

    /**
     * 获取一个失败的消息
     * @return 失败的消息
     */
    public static QueryResMessage getErrorMessage(){
        return new QueryResMessage(400, "参数传递错误", null, 0, 0);
    }

    /**
     * 获取一个查询为空的消息
     * @param message 消息
     * @param data 数据
     * @return 查询为空的消息
     */
    public static QueryResMessage getNullMessage( String message, Object data, int count, int total){
        return new QueryResMessage(1, message, data, count, total);
    }
}
