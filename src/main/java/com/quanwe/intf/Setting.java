package com.quanwe.intf;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;

/**
 * 设置
 * Created by WQ on 2017/12/4.
 */

public class Setting {
    private static Setting _SETTING = new Setting();
    public static Setting getDefault() {
        return _SETTING;
    }
    private HashSet<String> urlExcludeKeywords=new HashSet<>();
    private HashSet<String> fieldExcludeKeywords=new HashSet<>();
    private File saveDir=new File("");
    public void addFieldExcludeKeywords(String...keywords){
        Collections.addAll(fieldExcludeKeywords, keywords);
    }

    public HashSet<String> getFieldExcludeKeywords() {
        return fieldExcludeKeywords;
    }
    public void addUrlExcludeKeywords(String...keywords){
        Collections.addAll(urlExcludeKeywords, keywords);
    }

    public File getSaveDir() {
        return saveDir;
    }

    public void setSaveDir(File saveDir) {
        if(saveDir==null)return;
        this.saveDir = saveDir;
    }

    public HashSet<String> getUrlExcludeKeywords() {
        return urlExcludeKeywords;
    }
}
