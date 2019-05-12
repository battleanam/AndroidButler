package com.hanayue.service.user.model;

import lombok.Data;

/**
 * 用户实体类
 */
@Data
public class Account {
    /**
     * 主键 采用一个64位的UUID
     */
    private String id;
    /**
     * 登陆用户名
     */
    private String account;
    /**
     * 用户的昵称
     */
    private String username;
    /**
     * 用户拓展资料的ID
     */
    private String profileId;
    /**
     * 用户的拓展资料
     */
    private Profile profile;
}

