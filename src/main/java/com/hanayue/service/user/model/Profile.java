package com.hanayue.service.user.model;


import lombok.Data;

import java.util.Date;

/**
 * 用户的拓展资料
 */
@Data
public class Profile {
    private String id; //主键
    private String picture; //用户头像  存放服务器端的路径
    private String sex; //性别
    private Date birthday; //生日
    private String occupation; //职业
    private String company; //公司
    private String school; //学校
    private String location; //所在地
    private String email; //邮箱
    private String remark; //个人说明
}
