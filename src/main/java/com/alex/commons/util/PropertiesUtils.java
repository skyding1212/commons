package com.alex.commons.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Properties;

/**
 * @描述: 读取配置辅助类
 */
@Slf4j
public class PropertiesUtils {

    /**
     * 根据主键key读取主键的值value
     *
     * @param key 键名
     */
    public static String getKeyValue(Properties properties, String key) {
        return properties.getProperty(key);
    }

    public static Long getLong(Properties properties, String key) {
        return Long.valueOf(properties.getProperty(key));
    }


    public static Properties newProperties(String filePath) {
        Properties properties = new Properties();
        // 根据当前执行线程返回该线程的上下文 ClassLoader
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        String value = "";

        try {
            // 加载类获取源,读入输入流
            InputStream in = classLoader.getResource(filePath).openStream();

            // 从输入流读取属性列表
            properties.load(in);

        } catch (IOException e) {
            log.error("newProperties error",e);
        }
        return properties;
    }

    /**
     * 更新properties文件的键值对 如果该主键已经存在，更新该主键的值； 如果该主键不存在，则插件一对键值。
     *
     * @param key 键名
     * @param val 键值
     */
    public static void writeProperties(String fileName, Properties properties, String key, String val) {

        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();

        try {
            InputStream in = new BufferedInputStream(new FileInputStream(new File(path.replace("%20", " ") + fileName)));
            properties.load(in);
            // 调用 Hashtable 的方法 put。使用 getProperty 方法提供并行性。
            // 强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
            OutputStream fos = new FileOutputStream(new File(path.replace("%20", " ") + fileName));
            properties.setProperty(key, val);
            // 以适合使用 load 方法加载到 Properties 表中的格式，
            // 将此 Properties 表中的属性列表（键和元素对）写入输出流
            properties.store(fos, "Update '" + key + "' value");
        } catch (Exception e) {
            log.error("属性文件解析错误" + e);
        }
    }
}
