package com.quanwe.apimanager;

import com.quanwe.bean.ApiInfoBean;
import com.quanwe.intf.BaseClassCreater;
import com.quanwe.intf.Setting;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static com.quanwe.apimanager.ClassFileTemplate.API_ARG_TM;
import static com.quanwe.apimanager.ClassFileTemplate.API_METHOD_CLASS_TM;
import static com.quanwe.apimanager.ClassFileTemplate.API_METHOD_TM;
import static com.quanwe.apimanager.ClassFileTemplate.API_PUT_PARAM_TM;
import static com.quanwe.utils.StringUtils.getMethodNameForUrl;
import static com.quanwe.utils.StringUtils.getNamrForUrl;
import static com.quanwe.utils.StringUtils.getParamsDesc;

/**
 * 接口请求方法类生成器
 * Created by WQ on 2017/12/4.
 */

public class AmMethodClassCreater extends BaseClassCreater {
    String CONFIG_CLASS_NAME;
    public AmMethodClassCreater(String CLASS_NAME,String CONFIG_CLASS_NAME, String FILE_SUFFIX) {
        super(CLASS_NAME, FILE_SUFFIX);
        this.CONFIG_CLASS_NAME=CONFIG_CLASS_NAME;
    }

    @Override
    public String generateClassContent(List<ApiInfoBean> apiInfos) {
        String api_method_class_content = "";
        HashSet<String> fieldExcludeKeywords = Setting.getDefault().getFieldExcludeKeywords();
        HashMap<String ,Integer>nameNumber=new HashMap<>();
        for (int i = 0; i < apiInfos.size(); i++) {
            ApiInfoBean apiInfoBean = apiInfos.get(i);
            String namrForUrl = getNamrForUrl(apiInfoBean.url, fieldExcludeKeywords, 3).toUpperCase();
            if (namrForUrl.isEmpty()) continue;
            if (nameNumber.get(namrForUrl)!=null) {
                int nameNo = nameNumber.get(namrForUrl) + 1;
                nameNumber.put(namrForUrl, nameNo);
                namrForUrl=namrForUrl+nameNo;
            }else {
                nameNumber.put(namrForUrl, 0);
            }
            String nameCode = namrForUrl + "CODE";

            //请求方法类生成
            String methodNameForUrl = getMethodNameForUrl(apiInfoBean.url, fieldExcludeKeywords, 3);
            if (nameNumber.get(methodNameForUrl)!=null) {
                int nameNo = nameNumber.get(methodNameForUrl) + 1;
                nameNumber.put(methodNameForUrl, nameNo);
                methodNameForUrl=methodNameForUrl+nameNo;
            }else {
                nameNumber.put(methodNameForUrl, 0);
            }
            api_method_class_content = api_method_class_content + "\n" +
                    String.format(API_METHOD_TM, apiInfoBean.name, getParamsDesc(apiInfoBean.params), methodNameForUrl
                            , getArgList(apiInfoBean.params), getRequestType(apiInfoBean.type),
                            namrForUrl, nameCode, getArgPut(apiInfoBean.params)
                    ) + "\n";
        }

        String api_method_class = String.format(API_METHOD_CLASS_TM, CLASS_NAME, CONFIG_CLASS_NAME, api_method_class_content);
        //输出生成结果
        System.out.println(api_method_class);
        return api_method_class;
    }


    /**
     * 生成参数列表
     *
     * @param params
     * @return
     */
    private static String getArgList(List<ApiInfoBean.Params> params) {
        if (params == null || params.size() == 0) return "";
        String paramsDesc = "";
        for (ApiInfoBean.Params param : params) {
            paramsDesc = paramsDesc + String.format(API_ARG_TM, "Object", param.name);
        }
        return paramsDesc;
    }

    /**
     * 生成put方法列表
     *
     * @param params
     * @return
     */
    private static String getArgPut(List<ApiInfoBean.Params> params) {
        if (params == null || params.size() == 0) return "";
        String paramsDesc = "";
        for (ApiInfoBean.Params param : params) {
            paramsDesc = paramsDesc + String.format(API_PUT_PARAM_TM, param.name, param.name);
        }
        return paramsDesc;
    }

    /**
     * 生成请求方法
     *
     * @param type
     * @return
     */
    private static String getRequestType(String type) {
        return "GET".equalsIgnoreCase(type) ? "requestAsynGet" : "requestAsynPost";
    }

}
