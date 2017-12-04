package com.quanwe.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 接口信息实体类
 * Created by WQ on 2017/11/23.
 */

public class ApiInfoBean {
    public String name;//接口名称
    public String url;//接口地址
    public String type;//接口请求类型
    public String no;//接口编号/序号
    public List<Params> params;//参数列表
    public String returnValue;//返回值
    public String remark;//备注
    public void addParams(Params param) {
        if(params==null)params=new ArrayList<>();
        if(param==null)return;
        params.add(param);
    }

    public static class Params{
      public   String name;     //参数名
        public String type;     ///参数类型
        public String descript; //参数描述

        public Params() {
        }

        public Params(String name, String type, String descript) {
            this.name = filterWord(name);
            this.type = type;
            this.descript = descript;
        }

        public String filterWord(String string){
            return String.valueOf(string).replaceAll("\\[","").replaceAll("]","");
        }

        @Override
        public String toString() {
            return "Params{" +
                    "name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    ", descript='" + descript + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ApiInfoBean{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", type='" + type + '\'' +
                ", no='" + no + '\'' +
                ", params=" + params +
                ", returnValue='" + returnValue + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
