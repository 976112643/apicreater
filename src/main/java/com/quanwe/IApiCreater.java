package com.quanwe;


import com.quanwe.apimanager.MenuBean;
import com.quanwe.bean.ApiInfoBean;
import com.quanwe.bean.ApiInfoBean.Params;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;

import static com.quanwe.apimanager.ClassFileTemplate.API_ARG_TM;
import static com.quanwe.apimanager.ClassFileTemplate.API_CONFIG_CLASS_TM;
import static com.quanwe.apimanager.ClassFileTemplate.API_METHOD_CLASS_TM;
import static com.quanwe.apimanager.ClassFileTemplate.API_METHOD_TM;
import static com.quanwe.apimanager.ClassFileTemplate.API_PUT_PARAM_TM;
import static com.quanwe.apimanager.ClassFileTemplate.API_URL_CODE_TM;
import static com.quanwe.apimanager.ClassFileTemplate.API_URL_TM;

public class IApiCreater {
    public static void main(String[] args) {


        String API_CONFIG_CLASS_NAME = "BaseQuestConfig";
        String API_METHOD_CLASS_NAME = "BaseQuestStart";
        String EXCLUDE_KEYWORD="Api/";
//        if (true) {
//            saveToFile(API_CONFIG_CLASS_NAME + ".java", String.format(API_CONFIG_CLASS_TM, API_CONFIG_CLASS_NAME, ""));
//            return;
//        }
        String apiUrl = "http://test.cnsunrun.com/ApiManager/index.php?act=api&tag=51#info_api_934b535800b1cba8f96a5d72f72f1611";
        try {
            Document jsoup = Jsoup.parse(new URL(apiUrl), 5000);
//				Elements doc=jsoup.select("div.de-desc");
            Elements doc = jsoup.select("ul.list-unstyled > li");
            jsoup.setBaseUri("http://test.cnsunrun.com/ApiManager");
            //搜集左侧导航所有Api接口名称
            List<MenuBean> apis = new ArrayList<>();
            List<ApiInfoBean> apiInfos = new ArrayList<>();
            for (Element element : doc) {

//                    解析创建导航菜单并存放
                Element linkEle = $(element, "a");
                String hrefAttr = linkEle.attr("href").split("#")[1];
                MenuBean menuBean = new MenuBean(linkEle.text(), hrefAttr);

                apis.add(menuBean);

//                    解析接口信息数据 并存放
                ApiInfoBean apiInfoBean = new ApiInfoBean();

                Element apiInfo = $(jsoup, "div#" + menuBean.apiId);
                apiInfoBean.name = $(apiInfo, "h4.textshadow").text();
                apiInfoBean.no = $(apiInfo, "div > p > b > span").text();
                apiInfoBean.type = $(apiInfo, "div > div > kbd").text();
                apiInfoBean.url = $(apiInfo, "div > div > kbd", 1).text();
                apiInfoBean.returnValue = $(apiInfo, "div:has(pre) > pre").text();
                apiInfoBean.remark = $(apiInfo, "div:has(pre[style=background:honeydew]) > pre").text();
                apiInfoBean.url=String.valueOf(apiInfoBean.url).replaceFirst(EXCLUDE_KEYWORD,"");
                Elements tbody = apiInfo.select("table.table > tbody > tr");
                for (int i = 0; i < tbody.size(); i++) {
                    Element tableTr = tbody.get(i);
                    Params params = createParams(tableTr);
                    apiInfoBean.addParams(params);
                }
                apiInfos.add(apiInfoBean);
                System.out.println(apiInfoBean);
            }
            generateClassFile(apiInfos, API_CONFIG_CLASS_NAME, API_METHOD_CLASS_NAME);

//				System.out.println(jsoup.html());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generateClassFile(List<ApiInfoBean> apiInfos, String api_config_class_name, String api_method_class_name) {
        String api_config_class_content = "";
        String api_method_class_content = "";
        HashMap<String ,Integer>nameNumber=new HashMap<>();
        for (int i = 0; i < apiInfos.size(); i++) {
            ApiInfoBean apiInfoBean = apiInfos.get(i);
            String namrForUrl = getNamrForUrl(apiInfoBean.url, "Api", 3).toUpperCase();
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
            String methodNameForUrl = getMethodNameForUrl(apiInfoBean.url, "Api", 3);
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

        String api_config_class = String.format(API_CONFIG_CLASS_TM, api_config_class_name, api_config_class_content);
        String api_method_class = String.format(API_METHOD_CLASS_TM, api_method_class_name, api_config_class_name, api_method_class_content);
        //输出生成结果
        System.out.println(api_config_class);
        System.out.println(api_method_class);

        saveToFile(api_config_class_name + ".java", api_config_class);
        saveToFile(api_method_class_name + ".java", api_method_class);

    }

    /**
     * 生成参数列表
     *
     * @param params
     * @return
     */
    private static String getArgList(List<Params> params) {
        if (params == null || params.size() == 0) return "";
        String paramsDesc = "";
        for (Params param : params) {
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
    private static String getArgPut(List<Params> params) {
        if (params == null || params.size() == 0) return "";
        String paramsDesc = "";
        for (Params param : params) {
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

    /**
     * 生成参数描述
     *
     * @param params
     * @return
     */
    private static String getParamsDesc(List<Params> params) {
        if (params == null || params.size() == 0) return "";
        String paramsDesc = "* @param ";
        for (Params param : params) {
            paramsDesc = paramsDesc + String.format("%s %s", param.name, param.descript) + "\n";
        }
        return paramsDesc;
    }

    private static String getNamrForUrl(String url, String excudeStr, int maxDeep) {
        if (!url.contains("/") || url.startsWith("http")) return "";
        String keywords[] = url.split("/");
        excudeStr = excudeStr.toLowerCase();
        LinkedHashSet<String> keywordsSet = new LinkedHashSet<>();
        for (String keyword : keywords) {
            String lowerCase = keyword.toLowerCase();
            if (excudeStr.contains(lowerCase)) continue;
            keywordsSet.add(lowerCase);
        }
        List<String> keywordsData = new ArrayList<>(keywordsSet);
        String nameForUrl = "";
        for (int i = keywordsData.size() - 1, len = Math.max(i - maxDeep - 1, 0); i >= len; i--) {
            nameForUrl = keywordsData.get(i) + "_" + nameForUrl;
        }
        return nameForUrl;
    }

    private static String getMethodNameForUrl(String url, String excudeStr, int maxDeep) {
        if (!url.contains("/") || url.startsWith("http")) return "";
        String keywords[] = url.split("/");
        excudeStr = excudeStr.toLowerCase();
        LinkedHashSet<String> keywordsSet = new LinkedHashSet<>();
        for (String keyword : keywords) {
            String lowerCase = keyword.toLowerCase();
            if (excudeStr.contains(lowerCase)) continue;
            String[] keys = lowerCase.split("_");
            String newKeyWords = "";
            for (String key : keys) {
                newKeyWords = newKeyWords + upperCaseFirst(key);
            }
            keywordsSet.add(newKeyWords);
        }
        List<String> keywordsData = new ArrayList<>(keywordsSet);
        String nameForUrl = "";
        for (int i = keywordsData.size() - 1, len = Math.max(i - maxDeep - 1, 0); i >= len; i--) {
            nameForUrl = keywordsData.get(i) + nameForUrl;
        }
        return nameForUrl;
    }

    /**
     * 首字母大写
     *
     * @param str
     * @return
     */
    public static String upperCaseFirst(String str) {
        if (str == null || str.length() == 0) return str;
        String first = str.substring(0, 1).toUpperCase();
        return first + str.substring(1, str.length());
    }

    /**
     * 首字母小写
     *
     * @param str
     * @return
     */
    public static String lowerCaseFirst(String str) {
        if (str == null || str.length() == 0) return str;
        String first = str.substring(0, 1).toUpperCase();
        return first + str.substring(1, str.length());
    }

    /**
     * 保存字符串到文件
     *
     * @param filename
     * @param content
     * @return
     */
    public static boolean saveToFile(String filename, String content) {
        try {
            File saveFile = new File(filename).getAbsoluteFile();
            saveFile.getParentFile().mkdirs();
            System.out.println(new File(filename).getAbsolutePath());
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(saveFile), "UTF-8"));
            bufferedWriter.write(content);
            bufferedWriter.flush();
            bufferedWriter.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static Element $(Element element, String select) {
        Element first = element.select(select).first();
        return first == null ? new Element(Tag.valueOf("div"), "") : first;
    }

    public static Element $(Element element, String select, int index) {
        return element.select(select).get(index);
    }

    public static Params createParams(Element tableTr) {
        Elements tds = tableTr.select("td");
        if (tds == null || tds.size() == 0) return null;
        return new Params(tds.get(0).text(), tds.get(1).text(), tds.get(4).text());
    }
}
