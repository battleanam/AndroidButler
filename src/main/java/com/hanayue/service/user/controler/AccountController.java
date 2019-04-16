package com.hanayue.service.user.controler;

import com.hanayue.service.common.controller.BaseController;
import com.hanayue.service.common.dao.BaseDao;
import com.hanayue.service.common.model.ResMessage;
import com.hanayue.service.common.service.BaseService;
import com.hanayue.service.user.model.Account;
import com.hanayue.service.user.service.AccountService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("account")
public class AccountController extends BaseController<Account, BaseService<Account, BaseDao<Account>>> {

    @Resource
    private AccountService accountService;

    /**
     * 修改拓展信息接口
     * @param param 修改内容  必须包含ID 否则返回参数传递错误
     * @return 修改成功或者失败
     */
    @RequestMapping("updateProfile")
    public ResMessage updateProfile(Map param){
        if(param.get("id") == null){ //判断是否包含ID
            return ResMessage.getErrorMessage();
        }
        int data = accountService.updateProfile(param);
        if(data < 0){
            return ResMessage.getNullMessage("修改失败", data);
        }else {
            return ResMessage.getSuccessMessage("修改成功", data);
        }
    }

    /**
     * 执行用户登陆验证
     * @param param 用户的登陆信息
     * @return 返回登陆用户的基本信息
     */
    @RequestMapping("doLogin")
    public ResMessage doLogin(@RequestBody Map param){
        String account = String.valueOf(param.get("account"));
        if(account == null){
            return ResMessage.getErrorMessage();
        }
        Map<String, String> countParam = new HashMap<>();
        countParam.put("account", account);
        if(accountService.countByParam(countParam) == 0){
            return ResMessage.getNullMessage("未注册的用户", null);
        }
        Account res = accountService.doLogin(param);
        if(res == null){
            return ResMessage.getNullMessage("用户登陆验证失败", null);
        }else {
            return ResMessage.getSuccessMessage("登陆成功", res);
        }
    }

    /**
     * 执行用户注册
     * @param param 用户的注册信息
     * @return 注册后的基本信息
     */
    @RequestMapping("doRegister")
    public ResMessage doRegister(@RequestBody Map<String, Object> param){
        String account = String.valueOf(param.get("account"));
        String username = String.valueOf(param.get("username"));
        String password = String.valueOf(param.get("password"));
        if(account == null || username == null || password == null){
            return ResMessage.getErrorMessage();
        }
        Map<String, String> countParam = new HashMap<>();
        countParam.put("account", account);
        int accountCount = accountService.countByParam(countParam);
        if (accountCount > 0) {
            return ResMessage.getNullMessage("此用户已被注册", null);
        }
        if(accountService.doRegister(param) == 0){
            return ResMessage.getNullMessage("注册失败,请稍后再试", null);
        }else {
            return ResMessage.getSuccessMessage("注册成功", 1);
        }
    }
}
