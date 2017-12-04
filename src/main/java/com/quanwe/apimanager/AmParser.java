package com.quanwe.apimanager;

import com.quanwe.bean.ApiInfoBean;
import com.quanwe.intf.IApiParser;
import com.quanwe.intf.Setting;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 基于ApiManager 的解析器
 * Created by WQ on 2017/12/4.
 */

public class AmParser implements IApiParser {
    protected String apiUrl = "";
    protected String apiBaseUrl;

    public AmParser(String apiUrl, String apiBaseUrl) {
        this.apiUrl = apiUrl;
        this.apiBaseUrl = apiBaseUrl;
    }

    @Override
    public List<ApiInfoBean> parser() {
        List<ApiInfoBean> apiInfos = new ArrayList<>();
        try {
            Document jsoup = Jsoup.parse(new URL(apiUrl), 5000);
//				Elements doc=jsoup.select("div.de-desc");
            Elements doc = jsoup.select("ul.list-unstyled > li");
            jsoup.setBaseUri(apiBaseUrl);
            //搜集左侧导航所有Api接口名称
            for (Element element : doc) {
//                    解析创建导航菜单并存放
                Element linkEle = $(element, "a");
                String hrefAttr = linkEle.attr("href").split("#")[1];
                MenuBean menuBean = new MenuBean(linkEle.text(), hrefAttr);

//                    解析接口信息数据 并存放
                ApiInfoBean apiInfoBean = new ApiInfoBean();

                Element apiInfo = $(jsoup, "div#" + menuBean.apiId);
                apiInfoBean.name = $(apiInfo, "h4.textshadow").text();
                apiInfoBean.no = $(apiInfo, "div > p > b > span").text();
                apiInfoBean.type = $(apiInfo, "div > div > kbd").text();
                apiInfoBean.url = $(apiInfo, "div > div > kbd", 1).text();
                apiInfoBean.returnValue = $(apiInfo, "div:has(pre) > pre").text();
                apiInfoBean.remark = $(apiInfo, "div:has(pre[style=background:honeydew]) > pre").text();
                apiInfoBean.url = removeExculdeKeys(apiInfoBean.url);
                Elements tbody = apiInfo.select("table.table > tbody > tr");
                for (int i = 0; i < tbody.size(); i++) {
                    Element tableTr = tbody.get(i);
                    ApiInfoBean.Params params = createParams(tableTr);
                    apiInfoBean.addParams(params);
                }
                apiInfos.add(apiInfoBean);
                System.out.println(apiInfoBean);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return apiInfos;
    }

    public String removeExculdeKeys(String url){
        url= String.valueOf(url);
        HashSet<String> urlExcludeKeywords = Setting.getDefault().getUrlExcludeKeywords();
        for (String urlExcludeKeyword : urlExcludeKeywords) {
            url=  url.replaceFirst(urlExcludeKeyword, "");
        }
        return url;
    }

    public Element $(Element element, String select) {
        Element first = element.select(select).first();
        return first == null ? new Element(Tag.valueOf("div"), "") : first;
    }

    public Element $(Element element, String select, int index) {
        return element.select(select).get(index);
    }

    public ApiInfoBean.Params createParams(Element tableTr) {
        Elements tds = tableTr.select("td");
        if (tds == null || tds.size() == 0) return null;
        return new ApiInfoBean.Params(tds.get(0).text(), tds.get(1).text(), tds.get(4).text());
    }
}
