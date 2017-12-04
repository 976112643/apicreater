# apicreater
接口生成器. 根据接口文档自动生成接口及接口方法定义

代码结构说明:
```
com
    └─quanwe
        │  TestApiCreater.java
        │  
        ├─apimanager						基于apimanager这个接口文档和我使用的类的定义模板的相关示例
        │      AmConfigClassCreater.java	接口配置类生成器
        │      AmMethodClassCreater.java	接口请求方法类生成器
        │      AmParser.java				接口文档解析类
        │      ClassFileTemplate.java		类模板
        │      MenuBean.java				
        │      
        ├─bean
        │      ApiInfoBean.java				接口信息实体类
        │      
        ├─intf								接口
        │      ApiCreater.java				创建器
        │      BaseClassCreater.java		类生成器基类,实现保存类的内容到文件
        │      IApiParser.java				接口信息解析器, 实现接口文档->ApiInfoBean
        │      IClassCreater.java			类文件创建器, 实现类信息->文件
        │      Setting.java					配置
        │      
        └─utils								工具类
                FileUtils.java
                StringUtils.java
```
                


需要根据自己的接口文档和接口定义来创建自己的解析器和类生成器.

使用的示例代码如下:

```java
 String FILE_SUFFIX=".java";
        String API_CONFIG_CLASS_NAME = "BaseQuestConfig";
        String API_METHOD_CLASS_NAME = "BaseQuestStart";
        String apiUrl = "http://test.cnsunrun.com/ApiManager/index.php?act=api&tag=51#info_api_934b535800b1cba8f96a5d72f72f1611";
        String baseUrl = "http://test.cnsunrun.com/ApiManager";
        //创建接口生成器,配置接口解析器
        ApiCreater apiCreater=new ApiCreater(new AmParser(apiUrl,baseUrl));
        //添加类文件生成器
        apiCreater.addClassCreater(new AmConfigClassCreater(API_CONFIG_CLASS_NAME,FILE_SUFFIX));
        apiCreater.addClassCreater(new AmMethodClassCreater(API_METHOD_CLASS_NAME,API_CONFIG_CLASS_NAME,FILE_SUFFIX));
        //配置url排除的关键字
        Setting.getDefault().addUrlExcludeKeywords("Api/");
        //配置生成的字段/方法名中排除的关键字
        Setting.getDefault().addFieldExcludeKeywords("Api");
        //设置文件保存路径
        Setting.getDefault().setSaveDir(new File("./"));
        //开始生成
        apiCreater.generate();
```