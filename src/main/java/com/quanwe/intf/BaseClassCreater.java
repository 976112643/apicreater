package com.quanwe.intf;

import com.quanwe.bean.ApiInfoBean;

import java.io.File;
import java.util.List;

import static com.quanwe.utils.FileUtils.saveToUtf8File;


/**
 * 类生成器基类,实现保存类的内容到文件
 * Created by WQ on 2017/12/4.
 */

public abstract class BaseClassCreater implements IClassCreater {

    protected String CLASS_NAME,FILE_SUFFIX;

    public BaseClassCreater(String CLASS_NAME, String FILE_SUFFIX) {
        this.CLASS_NAME = CLASS_NAME;
        this.FILE_SUFFIX = FILE_SUFFIX;
    }

    @Override
    final public  void generateClassFile(List<ApiInfoBean> apiInfos) {
        String classContent = generateClassContent(apiInfos);
        File saveDir = Setting.getDefault().getSaveDir();
        String filaname=new File(saveDir,CLASS_NAME + FILE_SUFFIX).getAbsolutePath();
        saveToUtf8File(filaname, classContent);
    }

    public abstract String generateClassContent(List<ApiInfoBean> apiInfos) ;
}
