package com.quanwe;

import com.quanwe.apimanager.AmConfigClassCreater;
import com.quanwe.apimanager.AmMethodClassCreater;
import com.quanwe.apimanager.AmParser;
import com.quanwe.intf.ApiCreater;
import com.quanwe.intf.Setting;

import java.io.File;

/**
 * 测试接口创建
 * Created by WQ on 2017/12/4.
 */

public class TestApiCreater {
    public static void main(String[] args) {
        String FILE_SUFFIX=".java";
        String API_CONFIG_CLASS_NAME = "BaseQuestConfig";
        String API_METHOD_CLASS_NAME = "BaseQuestStart";
        String apiUrl = "http://apimanager.quanwe.top/index.php?act=api&tag=7";
        String baseUrl = "http://apimanager.quanwe.top";
        //创建接口生成器,配置接口解析器
        ApiCreater apiCreater=new ApiCreater(new AmParser(apiUrl,baseUrl));
        //添加类文件生成器
        apiCreater.addClassCreater(new AmConfigClassCreater(API_CONFIG_CLASS_NAME,FILE_SUFFIX));
        apiCreater.addClassCreater(new AmMethodClassCreater(API_METHOD_CLASS_NAME,API_CONFIG_CLASS_NAME,FILE_SUFFIX));
        //配置url排除的关键字
        Setting.getDefault().addUrlExcludeKeywords("Api/");
        //配置生成的字段/方法名中排除的关键字
        Setting.getDefault().addFieldExcludeKeywords("Api");
        //设置文件保存路径
        Setting.getDefault().setSaveDir(new File("./"));
        //开始生成
        apiCreater.generate();
    }
}
