package com.hanayue.service.location.controler;

import com.hanayue.service.common.model.ResMessage;
import com.hanayue.service.location.model.City;
import com.hanayue.service.location.service.LocationService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("location")
public class LocationController {

    @Resource
    private LocationService locationService;

    /**
     * 根据条件查询城市列表
     *
     * @param param 查询条件
     * @return 城市列表
     */
    @RequestMapping("selectListByParam")
    public ResMessage selectListByParam(@RequestBody Map<String, Object> param) {

        // 对参数进行处理 确保参数类型正确
        if (param.containsKey("id") && param.get("id") instanceof String) {
            try {
                param.replace("id", Integer.valueOf((String) param.get("id")));
            } catch (NumberFormatException e) {
                return ResMessage.getErrorMessage();
            }
        } else if (param.containsKey("pid") && param.get("pid") instanceof String) {
            try {
                param.replace("pid", Integer.valueOf((String) param.get("pid")));
            } catch (NumberFormatException e) {
                return ResMessage.getErrorMessage();
            }
        }

        if (param.keySet().size() < 1) {
            return ResMessage.getNullMessage("至少传递一个查询条件", null);
        } else {
            List<City> cities = locationService.selectListByParam(param);
            if (cities.size() == 0) {
                return ResMessage.getNullMessage("查询数据为空", null);
            } else {
                return ResMessage.getSuccessMessage("查询成功", cities);
            }
        }
    }

    /**
     * 根据彩云天气提供的CSV通过城市编码查询出城市的经纬度
     *
     * @param code 城市编码
     * @return 经纬度
     */
    @RequestMapping("getLocation")
    public ResMessage getLocation(String code) {
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
        } catch (NumberFormatException nfe) {
            return ResMessage.getErrorMessage(); //返回参数传递错误
        }
    }

}
