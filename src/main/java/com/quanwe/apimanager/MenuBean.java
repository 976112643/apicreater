package com.quanwe.apimanager;

/**
 * 左侧导航实体类
 * Created by WQ on 2017/11/23.
 */

public class MenuBean {
    public String title;
    public String apiId;

    public MenuBean(String title, String apiId) {
        this.title = title;
        this.apiId = apiId;
    }

    public MenuBean() {
    }

    @Override
    public String toString() {
        return "MenuBean{" +
                "title='" + title + '\'' +
                ", apiId='" + apiId + '\'' +
                '}';
    }
}
