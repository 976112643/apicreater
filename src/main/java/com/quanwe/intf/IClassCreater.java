package com.quanwe.intf;

import com.quanwe.bean.ApiInfoBean;

import java.util.List;

/**
 * 类文件创建器
 * Created by WQ on 2017/12/4.
 */

public interface IClassCreater {
    void generateClassFile(List<ApiInfoBean> apiInfos);
}
