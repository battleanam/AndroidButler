package com.hanayue.service.location.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.csvreader.CsvReader;
import com.hanayue.service.location.dao.LocationDao;
import com.hanayue.service.location.model.City;

import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

import static java.lang.Integer.parseInt;

@Service
public class LocationService {

    @Resource
    private LocationDao locationDao;

    /**
     * 数据源文件的路径
     */
    private final String csvPath = "src/main/resources/static/csv/location.csv";

    /**
     * 通过参数查询列表
     * @param param 查询条件
     * @return 城市列表
     */
    public List<City> selectListByParam(Map param){
        return locationDao.selectListByParam(param);
    }

    /**
     * 通过CSV通过地址的编码获取经纬度
     * 彩云天气提供了一个各城市经纬度的对照表  是一个csv文件
     * 通过解析csv查找各地市的经纬度
     *
     * @param code 城市编码
     * @return 经纬度 121.6544,25.1552
     */
    public String getLocation(String code) {
        /**
         * 首先要把csv中的数据读到内存 然后放到一个数组中
         *
         * 然后将这个数组当作数据源  进行二分查找  查出对应的经纬度
         *
         */
        List<String[]> cities = new ArrayList<>(); // 用来存放从csv中读取的数据
        if (csvExists()) { //首先判断文件是否存在
            try {
                CsvReader csvReader = new CsvReader(csvPath, ',', Charset.forName("utf8")); //使用csvReader将csv文件读入到内存中
                while (csvReader.readRecord()) { //每次读取一行
                    cities.add(csvReader.getValues()); //将读到的那一行通过getValues()方法转换成字符串数组 然后放到cities中
                }
                //循环结束后  数据源就已经有了
                return find(cities, code); // 将cities作为数据源  进行二分查找
            } catch (FileNotFoundException e) {
                return "fe";
            } catch (IOException e) {
                return "ioe";
            }
        } else {
            return "fe";
        }
    }


    /**
     * 递归查询出code对应的经纬度
     * <p>
     * 这里用的是二分查找
     * 因为数据源是有序的  所以判断中间的code比我们要找的code大还是小
     * 通过这样的方式将数组分成了左右两边  左边的code永远比中间的code小  右边code永远比中间的code大
     * 如果中间的code 比我们要找的 code 大  说明code在小的一边  也就是左边
     * 我们就把左边的数组截取出来 当作新的数据源 然后再左边的数组里重复上边的过程
     *
     * @param source 数据源
     * @param code   数据源中的编码
     * @return 经纬度 121.6544,25.1552
     */
    private String find(List<String[]> source, String code) {
        /**
         * 把数组不断的分成两部分  最后一定会变成长度为1或者0的数组  我们先处理这两种情况
         *
         */

        //如果长度是source的长度为1  也就是说数组内只有一个元素 判断这是元素的第一位是否等于code  是的话 这个元素就是我们要找的
        if (source.size() == 1 && source.get(0)[0].equals(code)) {
            //将第五位和第六位拼成需要的格式  保留四位小数  从第一位开始取  取到小数点后边四位  为什么加5 是因为小数点也算进去了
            String lat = source.get(0)[4].substring(0, source.get(0)[4].indexOf('.') + 5); //经度
            String lon = source.get(0)[5].substring(0, source.get(0)[5].indexOf('.') + 5); //纬度
            return lat + ',' + lon; //最后用逗号隔开
        } else if (source.size() == 1 && !source.get(0)[0].equals(code)) { //长度是1 但是唯一的一个元素的第一位不是 code 说明soruce里没有我们要找的
            return "not found"; //返回not find
        }

        if (source.size() == 0) { //如果数组长度为0 显然没有我们要找的元素
            return "not found"; //返回not find
        }

        /**
         * 接下来处理长度不为1和0的情况
         */

        String[] item = source.get(source.size() / 2); //按照思路  首先将中间的元素取出来
        if (item[0].equals(code)) { //然后判断中间的元素是不是我们要找的 如果是
            String lat = String.format("%.4f", Float.parseFloat(item[4])); //经度
            String lon = String.format("%.4f", Float.parseFloat(item[5])); //纬度
            return lon + ',' + lat; //拼接成应该返回的形式
        } else if (parseInt(item[0]) < parseInt(code)) { //如果比较之后中间的元素偏小  说明要找的在右边
            List<String[]> newSource = source.subList(source.size() / 2, source.size()); //将右边的数组截取出来  从数组长度的一半 一直到数组最后一位
            return find(newSource, code); //递归寻找新截取的数组中有没有我们想要的元素
        } else { //否则  比较之后中间的元素偏大  截取左边的数组进行递归  这里用表达式传参 截取从第0个到第 source.size()-1个
            return find(source.subList(0, source.size() / 2), code);
        }
    }

    /**
     * 判断文件是否存在
     *
     * @return
     */
    private boolean csvExists() {
        return new File(csvPath).exists();
    }

    /**
     * 将CSV中的数据导入数据库
     */
    public String transformToJSON(List<String[]> source) {

        List<Object> list = new ArrayList<>();
        Map<String, Object> json = new LinkedHashMap<>();
        for (int i = 0; i < source.size(); i++) {
            String[] item = source.get(i);
            String code = item[0];
            String a = item[1];
            String b = item[2];
            String c = item[3];
            if (!json.containsKey(a)) {
                Map<String, Object> bjson = new LinkedHashMap<>();
                Map<String, Object> cjson = new LinkedHashMap<>();
                cjson.put(c, code);
                bjson.put(b, cjson);
                json.put(a, bjson);
                list.add(a);
            } else {
                Map<String, Object> bjson = (Map<String, Object>) json.get(a);
                if (!bjson.containsKey(b)) {
                    JSONObject cjson = new JSONObject();
                    cjson.put(c, code);
                    bjson.put(b, cjson);
                } else {
                    Map<String, Object> cjson = (Map<String, Object>) bjson.get(b);
                    cjson.put(c, code);
                    bjson.put(b, cjson);
                }
            }
        }
        Object[] ps = json.keySet().toArray();
        int id = 1;
        int firstId = id;
        int secondId = id;
        boolean res = true;
        for (int i = 0; i < ps.length; i++) {
            City province = new City();
            province.setId(id++);
            province.setName(String.valueOf(ps[i]));
            province.setPid(-1);
            province.setIsLeaf(0);
            JSONObject pJson = JSONObject.parseObject(JSON.toJSONString(province));
            int pr = locationDao.insertOne(pJson);
            res = res && pr > 0;
            firstId = province.getId();
            Map cities = (Map) json.get(province.getName());
            Object[] cs = cities.keySet().toArray();
            for (int j = 0; j < cs.length; j++) {
                City city = new City();
                city.setId(id++);
                city.setName(String.valueOf(cs[j]));
                city.setPid(firstId);
                Map areas = (Map) cities.get(city.getName());
                if (areas.keySet().contains("无")) {
                    city.setIsLeaf(1);
                    String code = (String) areas.get("无");
                    city.setValue(getLocation(code));
                    int cr = locationDao.insertOne(JSONObject.parseObject(JSON.toJSONString(city)));
                    res = res && cr > 0;
                } else {
                    city.setIsLeaf(0);
                    int cr = locationDao.insertOne(JSONObject.parseObject(JSON.toJSONString(city)));
                    res = res && cr > 0;
                    secondId = city.getId();
                    Object[] as = areas.keySet().toArray();
                    System.out.println("areas" + areas);
                    for (int k = 0; k < as.length; k++) {
                        City area = new City();
                        area.setId(id++);
                        area.setPid(secondId);
                        area.setName(String.valueOf(as[k]));
                        area.setIsLeaf(1);
                        String code = (String) areas.get(area.getName());
                        area.setValue(getLocation(code));
                        int ar = locationDao.insertOne(JSONObject.parseObject(JSON.toJSONString(area)));
                        res = res && ar > 0;
                    }
                }
            }
        }
        return res ? "success" : "failed";
    }

}
