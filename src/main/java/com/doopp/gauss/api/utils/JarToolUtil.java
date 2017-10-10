package com.doopp.gauss.api.utils;

import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 获取打包后jar的路径信息
 *
 */
@Component
public class JarToolUtil
{
    /**
     * 获取jar绝对路径
     *
     * @return String
     */
    public static String getJarPath()
    {
        File file = getFile();
        return (file == null) ? null : file.getAbsolutePath();
    }

    /**
     * 获取jar目录
     *
     * @return String
     */
    public static String getJarDir()
    {
        File file = getFile();
        return (file == null) ? null : file.getParent();
    }

    /**
     * 获取jar包名
     *
     * @return String
     */
    public static String getJarName()
    {
        File file = getFile();
        return (file == null) ? null : file.getName();
    }

    /**
     * 获取当前Jar文件
     *
     * @return File
     */
    private static File getFile()
    {
        String path = JarToolUtil.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        try {
            path = java.net.URLDecoder.decode(path, "UTF-8"); // 转换处理中文及空格
        }
        catch (java.io.UnsupportedEncodingException e) {
            return null;
        }
        return new File(path);
    }
}
