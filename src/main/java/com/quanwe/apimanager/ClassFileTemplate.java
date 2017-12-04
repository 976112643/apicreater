package com.quanwe.apimanager;

/**
 * 代码文件模板
 * Created by WQ on 2017/11/23.
 */

public class ClassFileTemplate {
    public final static String API_URL_TM="//%3$s\npublic static String %1$s=HTTP_API+\"%2$s\";";
    public final static String API_URL_CODE_TM="public static final int %1$s=%2$s;";
    public final static String API_METHOD_TM="" +
            " /**\n" +
            "     * %1$s\n" +
            "      %2$s\n" +
            "     */\n" +
            "    public static void %3$s(NetRequestHandler netRequestHandler %4$s){\n" +
            "        netRequestHandler.%5$s(Config.UAction().setUrl(%6$s)\n" +
            "                .setRequestCode(%7$s)\n%8$s" +
            "        );\n" +
            "    }";
    public final static String API_PUT_PARAM_TM=".put(\"%1$s\",%1$s)";
    public final static String API_ARG_TM=",%1$s %2$s";

    public final static String API_CONFIG_CLASS_TM=" public class %1$s implements NetQuestConfig {\n %2$s \n}";
    public final static String API_METHOD_CLASS_TM=" public class %1$s extends %2$s {\n%3$s\n}";




}
