package com.quanwe.utils;

import com.quanwe.bean.ApiInfoBean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by WQ on 2017/12/4.
 */

public class StringUtils {

    /**
     * 默认不处理http开头的url.
     * 根据Url生成名称,(逻辑是移除'/',搜集关键字以'_'连接的命名方式进行拼接)
     * @param url   url路径
     * @param excudeSet 排除的关键字集合
     * @param maxDeep 深度 解析的'/'个数,url 防止url过长,导致方法名很长
     * @return
     */
    public static String getNamrForUrl(String url, HashSet<String> excudeSet, int maxDeep) {
        if (!url.contains("/") || url.startsWith("http")) return "";
        String keywords[] = url.split("/");
        LinkedHashSet<String> keywordsSet = new LinkedHashSet<>();
        for (String keyword : keywords) {
            String lowerCase = keyword.toLowerCase();
            if (excudeSet.contains(lowerCase)) continue;
            keywordsSet.add(lowerCase);
        }
        List<String> keywordsData = new ArrayList<>(keywordsSet);
        String nameForUrl = "";
        for (int i = keywordsData.size() - 1, len = Math.max(i - maxDeep - 1, 0); i >= len; i--) {
            nameForUrl = keywordsData.get(i) + "_" + nameForUrl;
        }
        return nameForUrl;
    }

    /**
     * 默认不处理http开头的url.
     * 根据Url生成名称,(逻辑是移除'/',搜集关键字以驼峰命名方式进行拼接)
     * @param url   url路径
     * @param excudeSet 排除的关键字集合
     * @param maxDeep 深度 解析的'/'个数,url 防止url过长,导致方法名很长
     * @return
     */
    public static String getMethodNameForUrl(String url, HashSet<String> excudeSet, int maxDeep) {
        if (!url.contains("/") || url.startsWith("http")) return "";
        String keywords[] = url.split("/");
        LinkedHashSet<String> keywordsSet = new LinkedHashSet<>();
        for (String keyword : keywords) {
            String lowerCase = keyword.toLowerCase();
            if (excudeSet.contains(lowerCase)) continue;
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
     * 生成参数描述
     *
     * @param params
     * @return
     */
    public static String getParamsDesc(List<ApiInfoBean.Params> params) {
        if (params == null || params.size() == 0) return "";
        String paramsDesc = "";
        for (ApiInfoBean.Params param : params) {
            paramsDesc = paramsDesc +"* @param "+ String.format("%s %s", param.name, param.descript) + "\n";
        }
        return paramsDesc;
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
}
