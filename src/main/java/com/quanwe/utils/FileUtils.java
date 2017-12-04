package com.quanwe.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by WQ on 2017/12/4.
 */

public class FileUtils {
    /**
     * 保存字符串到文件
     *
     * @param filename
     * @param content
     * @return
     */
    public static boolean saveToFile(String filename, String content,String charsetName) {
        try {
            File saveFile = new File(filename).getAbsoluteFile();
            saveFile.getParentFile().mkdirs();
            System.out.println(new File(filename).getAbsolutePath());
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(saveFile), charsetName));
            bufferedWriter.write(content);
            bufferedWriter.flush();
            bufferedWriter.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 保存内容为utf8格式的文件
     * @param filename
     * @param content
     * @return
     */
    public static boolean saveToUtf8File(String filename, String content) {
        return saveToFile(filename,content,"UTF-8");
    }
}
