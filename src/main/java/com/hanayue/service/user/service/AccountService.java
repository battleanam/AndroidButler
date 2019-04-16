package com.hanayue.service.user.service;

import com.hanayue.service.common.service.BaseService;
import com.hanayue.service.user.dao.AccountDao;
import com.hanayue.service.user.model.Account;
import com.hanayue.service.user.model.Profile;
import com.hanayue.service.utils.MD5;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户服务类
 */
@Service
public class AccountService extends BaseService<Account, AccountDao> {
    /**
     * 更新某个拓展资料
     * @param param 更新的内容
     * @return 被修改的记录条数
     */
    public int updateProfile(Map param){
        return  getDao().updateProfile(param);
    }

    /**
     * 执行用户登陆验证
     * @param param 用户的登陆信息
     * @return 返回登陆用户的基本信息
     */
    public Account doLogin(Map param){
        String password = String.valueOf(param.get("password"));
        Map<String, Object> queryParam = new HashMap<>();
        queryParam.put("account",param.get("account"));
        queryParam.put("password", MD5.md5(password));
        return getDao().doLogin(queryParam);
    }

    /**
     * 执行用户注册
     * @param param 用户的注册信息
     * @return 注册后的基本信息
     */
    public int doRegister(Map<String, Object> param){
        param.replace("password", MD5.md5(String.valueOf(param.get("password"))));
        int insertResult = getDao().insertOne(param);
        return insertResult;
    }
}
