package com.hanayue.service.amount.model;

import lombok.Data;

/**
 * 收支管理的实体类
 */
@Data
public class Amount {
    private String id; //ID
    private String userId;  // 用户ID
    private float count; //花费了多少钱
    private long noteTime; //记录的时间
    private AmountType type; //收入还是支出
    private String sourceType; //来源类型
    private String moneyType; //资本类型  现金还是支付宝？
    private String remark; //备注
}

enum AmountType {
    EXPEND, INCOME
}
