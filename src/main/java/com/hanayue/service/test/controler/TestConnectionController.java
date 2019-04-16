package com.hanayue.service.test.controler;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试能否访问服务器的Controller(控制器)
 * 通过像服务器端发送一个请求 localhost:8081/test/connect
 * 如果返回 connect success! 表示服务端正常运行
 */
@RestController
@RequestMapping("test")
public class TestConnectionController {

    @RequestMapping("connect")
    public String testConnection(){
      return "connect success!";
    }
}
