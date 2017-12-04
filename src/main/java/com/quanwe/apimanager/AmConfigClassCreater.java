package com.quanwe.apimanager;

import com.quanwe.bean.ApiInfoBean;
import com.quanwe.intf.BaseClassCreater;
import com.quanwe.intf.Setting;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static com.quanwe.apimanager.ClassFileTemplate.API_CONFIG_CLASS_TM;
import static com.quanwe.apimanager.ClassFileTemplate.API_URL_CODE_TM;
import static com.quanwe.apimanager.ClassFileTemplate.API_URL_TM;
import static com.quanwe.utils.StringUtils.getMethodNameForUrl;
import static com.quanwe.utils.StringUtils.getNamrForUrl;

/**
 * 接口地址配置类生成器
 * Created by WQ on 2017/12/4.
 */

public class AmConfigClassCreater extends BaseClassCreater {
    public AmConfigClassCreater(String CLASS_NAME, String FILE_SUFFIX) {
        super(CLASS_NAME, FILE_SUFFIX);
    }

    /**
     * 生成类文件内容
     * @param apiInfos
     * @return
     */
    @Override
    public String generateClassContent(List<ApiInfoBean> apiInfos) {
        String api_config_class_content = "";
        HashMap<String ,Integer> nameNumber=new HashMap<>();
        HashSet<String> fieldExcludeKeywords = Setting.getDefault().getFieldExcludeKeywords();
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
            //接口地址及请求码定义类生成
            api_config_class_content = api_config_class_content + "\n" + String.format(API_URL_TM, namrForUrl, apiInfoBean.url, apiInfoBean.name) + "\n";
            String nameCode = namrForUrl + "CODE";
            api_config_class_content = api_config_class_content + "\n" + String.format(API_URL_CODE_TM, nameCode, "0x0" + apiInfoBean.no) + "\n";

            //请求方法类生成
            String methodNameForUrl = getMethodNameForUrl(apiInfoBean.url, fieldExcludeKeywords, 3);
            if (nameNumber.get(methodNameForUrl)!=null) {
                int nameNo = nameNumber.get(methodNameForUrl) + 1;
                nameNumber.put(methodNameForUrl, nameNo);
            }else {
                nameNumber.put(methodNameForUrl, 0);
            }
        }

        String api_config_class = String.format(API_CONFIG_CLASS_TM, CLASS_NAME, api_config_class_content);
        //输出生成结果
        System.out.println(api_config_class);
        return api_config_class;
    }

}
