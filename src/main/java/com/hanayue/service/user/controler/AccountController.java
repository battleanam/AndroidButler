package com.hanayue.service.user.controler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hanayue.service.common.model.ResMessage;
import com.hanayue.service.common.service.MessageService;
import com.hanayue.service.user.model.Account;
import com.hanayue.service.user.service.AccountService;
import okhttp3.Call;
import okhttp3.Response;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("account")
public class AccountController {

    @Resource
    private AccountService accountService;


    /**
     * 修改拓展信息接口
     *
     * @param param 修改内容  必须包含ID 否则返回参数传递错误
     * @return 修改成功或者失败
     */
    @RequestMapping("updateProfile")
    public ResMessage updateProfile(@RequestBody Map param) {
        if (param.get("id") == null) { //判断是否包含ID
            return ResMessage.getErrorMessage();
        }
        int data = accountService.updateProfile(param);
        if (data < 0) {
            return ResMessage.getNullMessage("修改失败", data);
        } else {
            return ResMessage.getSuccessMessage("修改成功", data);
        }
    }

    /**
     * 执行用户登陆验证
     *
     * @param param 用户的登陆信息
     * @return 返回登陆用户的基本信息
     */
    @RequestMapping("doLogin")
    public ResMessage doLogin(@RequestBody Map param) {
        String account = String.valueOf(param.get("account"));
        if (account == null) {
            return ResMessage.getErrorMessage();
        }
        Map<String, String> countParam = new HashMap<>();
        countParam.put("account", account);
        if (accountService.countByParam(countParam) == 0) {
            return ResMessage.getNullMessage("未注册的用户", null);
        }
        String password = String.valueOf(param.get("password"));
        if(password.equals("null") || password == null){
            String checkCode = String.valueOf(param.get("checkCode"));
            /**
             * 测试代码开始
             */
            if(checkCode.equals("123456")){
                Map<String, String> selectOneParam = new HashMap<>();
                selectOneParam.put("account", account);
                return ResMessage.getSuccessMessage("登陆成功", accountService.selectOneByParam(selectOneParam));
            }
            /**
             * 测试结束
             */
            if(MessageService.codeMap.containsKey(account)){
                if(checkCode.equals(MessageService.codeMap.get(account))){
                    Map<String, String> selectOneParam = new HashMap<>();
                    selectOneParam.put("account", account);
                    return ResMessage.getSuccessMessage("登陆成功", accountService.selectOneByParam(selectOneParam));
                }else {
                    return ResMessage.getNullMessage("验证码错误", null);
                }
            }else {
                return ResMessage.getNullMessage("验证码已过期，请重新发送验证码", null);
            }
        }else {
            Account res = accountService.doLogin(param);
            if (res == null) {
                return ResMessage.getNullMessage("账号或密码错误", null);
            } else {
                return ResMessage.getSuccessMessage("登陆成功", res);
            }
        }
    }

    /**
     * 执行用户注册
     *
     * @param param 用户的注册信息
     * @return 注册后的基本信息
     */
    @RequestMapping("doRegister")
    public ResMessage doRegister(@RequestBody Map<String, Object> param) {
        String account = String.valueOf(param.get("account"));
        String username = String.valueOf(param.get("username"));
        String password = String.valueOf(param.get("password"));
        String checkCode = String.valueOf(param.get("checkCode"));
        if (account == null || username == null || password == null || checkCode == null) {
            return ResMessage.getErrorMessage();
        }
        /**
         * 测试代码开始
         */
        if(checkCode.equals("123456")){
            if (accountService.doRegister(param) == 0) {
                return ResMessage.getNullMessage("注册失败,请稍后再试", null);
            } else {
                return ResMessage.getSuccessMessage("注册成功", 1);
            }
        }
        /**
         * 测试结束
         */
        if(MessageService.codeMap.containsKey(account)){
            String code = MessageService.codeMap.get(account);
            if(!code.equals(checkCode)){
                return ResMessage.getNullMessage("验证码错误", null);
            }
        }else {
            return ResMessage.getNullMessage("验证码已失效，请重新发送", null);
        }
        Map<String, String> countParam = new HashMap<>();
        countParam.put("account", account);
        int accountCount = accountService.countByParam(countParam);
        if (accountCount > 0) {
            return ResMessage.getNullMessage("此用户已被注册", null);
        }
        if (accountService.doRegister(param) == 0) {
            return ResMessage.getNullMessage("注册失败,请稍后再试", null);
        } else {
            return ResMessage.getSuccessMessage("注册成功", 1);
        }
    }

    @Resource
    private MessageService messageService;

    @RequestMapping("sendMessage")
    public ResMessage sendMessage(@RequestBody JSONObject template) {
        try {
            String to = template.getString("to");
            if (to == null || to.equals("null")) {
                return ResMessage.getErrorMessage();
            }
            Call call = messageService.sendCode(template);
            Thread thread = new Thread(() -> { // 倒计时验证吗
                try {
                    Thread.sleep(3 * 60 * 1000);
                    MessageService.codeMap.remove(to);
                } catch (InterruptedException e) {
                    MessageService.codeMap.remove(to);
                }
            });
            if(thread.isAlive()){ // 如果正在计时 打断
                thread.interrupt();
            }
            Response response = call.execute();
            thread.start(); // 开始计时
            assert response.body() != null;
            JSONObject res = JSON.parseObject(response.body().string());
            if (res.getString("statusCode").equals("000000")) {
                ResMessage message = ResMessage.getSuccessMessage("发送成功", null);
                return ResMessage.getSuccessMessage("发送成功", null);
            } else {
                return ResMessage.getNullMessage("发送失败", null);
            }
        } catch (IOException e) {
            return ResMessage.getErrorMessage();
        }
    }

    /**
     * 通过主键ID查询一条记录
     *
     * @param id 要查询的ID
     * @return 返回数据库中的一条记录
     */
    @RequestMapping("selectOneById")
    public ResMessage selectOneById(String id) {
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        Account data = accountService.selectOneByParam(param);
        if (data != null) {
            return ResMessage.getSuccessMessage("查询成功", data);
        } else {
            return ResMessage.getNullMessage("查询为空", null);
        }
    }

    /**
     * 通过参数查询数据库中的一条记录
     *
     * @param param 查询的条件 各个条件之间是且的关系
     * @return 返回查询到的一条记录
     */
    @RequestMapping("selectOneByParam")
    public ResMessage selectOneByParam(@RequestBody Map param) {
        Account data = accountService.selectOneByParam(param);
        if (data != null) {
            return ResMessage.getSuccessMessage("查询成功", data);
        } else {
            return ResMessage.getNullMessage("查询为空", null);
        }
    }

    /**
     * 通过参数查询数据库中的多条记录
     *
     * @param param 查询的条件 各个条件之间是且的关系
     * @return 返回查询到的多条记录
     */
    @RequestMapping("selectListByParam")
    public ResMessage selectListByParam(@RequestBody Map param) {
        List<Account> data = accountService.selectListByParam(param);
        if (data != null) {
            return ResMessage.getSuccessMessage("查询成功", data);
        } else {
            return ResMessage.getNullMessage("查询为空", null);
        }
    }

    /**
     * 向数据库中插入一条记录
     *
     * @param model 插入的数据
     * @return 返回插入数据的条数  如果不为0 则表示
     */
    @RequestMapping("insertOne")
    public ResMessage insertOne(@RequestBody Map model) {
        int data = accountService.insertOne(model);
        if (data > 0) {
            return ResMessage.getSuccessMessage("添加成功", data);
        } else {
            return ResMessage.getNullMessage("插入失败", data);
        }
    }

    /**
     * 更改数据库中的多条数据
     *
     * @param param 查询条件 决定那条记录会被更新
     * @return 被更改的数据的条数
     */
    @RequestMapping("update")
    public ResMessage update(@RequestBody Map param) {
        if (param.get("id") == null) {
            return ResMessage.getErrorMessage();
        }
        int data = accountService.update(param);
        if (data > 0) {
            return ResMessage.getSuccessMessage("修改成功", data);
        } else {
            return ResMessage.getNullMessage("修改失败", data);
        }
    }

    /**
     * 删除数据库中的某些符合条件的数据
     *
     * @param param 查询条件 决定那条数据将会被删除
     * @return 被删除的数据的条数
     */
    @RequestMapping("delete")
    public ResMessage delete(@RequestBody Map param) {
        int data = accountService.delete(param);
        if (data > -1) {
            return ResMessage.getSuccessMessage("删除成功", data);
        } else {
            return ResMessage.getNullMessage("删除失败", data);
        }
    }

    /**
     * 删除数据库中的某条数据
     *
     * @param id 查询条件 决定那条数据将会被删除
     * @return 被删除的数据的条数
     */
    @RequestMapping("deleteById")
    public ResMessage deleteById(String id) {
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        int data = accountService.delete(param);
        if (data > -1) {
            return ResMessage.getSuccessMessage("删除成功", data);
        } else {
            return ResMessage.getNullMessage("删除失败", data);
        }
    }

    /**
     * 查询数据库中所有数据的总量
     *
     * @return 数据库中所有数据的总量
     */
    @RequestMapping("count")
    public int count() {
        return accountService.count();
    }

    /**
     * 查询数据库中符合条件的数据的总量
     *
     * @return 数据库中符合条件的数据的总量
     */
    @RequestMapping("countByParam")
    public int countByParam(@RequestBody Map param) {
        return accountService.countByParam(param);
    }
}
