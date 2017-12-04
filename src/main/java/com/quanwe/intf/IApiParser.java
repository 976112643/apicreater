package com.quanwe.intf;

import com.quanwe.bean.ApiInfoBean;

import java.util.List;

/**
 * 接口信息解析器
 * Created by WQ on 2017/12/4.
 */

public interface IApiParser {
    List<ApiInfoBean> parser();
}
