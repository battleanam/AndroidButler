package com.hanayue.service.location.controler;

import com.hanayue.service.common.model.ResMessage;
import com.hanayue.service.location.service.LocationService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("location")
public class LocationController {

    @Resource
    private LocationService locationService;

    /**
     * 通过城市编码查询出城市的经纬度
     * @param code 城市编码
     * @return 经纬度
     */
    @RequestMapping("getLocationByCode")
    public ResMessage getLocationByCode(String code) {
        try {
            Integer.parseInt(code); //判断code是否能转换为数值整型  如果不能转换的会报NumberFormatException数字匹配异常 转到catch代码块 后边的语句不会执行
            String res = locationService.getLocation(code); //调用locationService中查询城市编码的服务 拿到一个结果
            switch (res) { //使用switch对结果进行解析
                case "fe": //如果是fe说明 未找到数据源文件
                    return ResMessage.getNullMessage("未找到数据源文件", null);
                case "ioe": //如果是ioe说明 访问数据源文件时发生IO错误
                    return ResMessage.getNullMessage("访问数据源文件时发生IO错误", null);
                case "not found": //如果是not found说明 未找到对应的经纬度
                    return ResMessage.getNullMessage("未找到对应的经纬度", null);
                default: //如果不是上述的三种情况  说明查询成功 返回一个成功的消息
                    return ResMessage.getSuccessMessage("查询成功", res);
            }
        }catch (NumberFormatException nfe){
            return ResMessage.getErrorMessage(); //返回参数传递错误
        }
    }
}
