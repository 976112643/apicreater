package com.quanwe.intf;

import com.quanwe.bean.ApiInfoBean;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 接口生成器-
 * Created by WQ on 2017/12/4.
 */

public  class ApiCreater {
    private IApiParser apiParser;
    private Set<IClassCreater>classCreaters=new HashSet<>();

    /**
     * 接口解析器
     * @param apiParser
     */
    public ApiCreater(IApiParser apiParser) {
        this.apiParser = apiParser;
    }

    /**
     * 添加类生成器
     * @param iClassCreaters
     */
    public void addClassCreater(IClassCreater...iClassCreaters){
        Collections.addAll(classCreaters, iClassCreaters);
    }
    public void clearClassCreater(){
        classCreaters.clear();
    }

    /**
     * 生成
     */
    public void generate(){
        List<ApiInfoBean> apiInfos = apiParser.parser();
        for (IClassCreater classCreater : classCreaters) {
            classCreater.generateClassFile(apiInfos);
        }
    }
}
