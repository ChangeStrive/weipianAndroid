package com.platform.util;

import java.util.Properties;

/**
 * 读取配置文件工具类
 * @author Saindy Su
 *
 */
public class PropertiesUtil {

    /**
     * 获取配置文件
     * @param fileName
     * @return
     * @author Saindy Su
     */
    public static Properties getProperties(String fileName) {
        Properties prop = new Properties();
        try {
            prop.load(PropertiesUtil.class.getResourceAsStream("/"+fileName));
        } catch (Exception e) {
            return null;
        }
        return prop;
    }
    
    /**
     * 读取配置文件的值（默认是config.properties文件）
     * @param propertiesKey 配置文件的Key
     * @return
     * @author Saindy Su
     */
    public static String getPropertiesValue(String propertiesKey){
        PropertiesUtil mPropertiesUnit = new PropertiesUtil();
        Properties mProperties = mPropertiesUnit.getProperties("Config.properties");
        String value = null;
        value = mProperties.getProperty(propertiesKey);
        return value;
    }
    /**
     * 读取指定配置文件名称内的key 的值
     * @param propertiesName
     * @param propertiesKey
     * @return
     */
    public static String getPropertiesValue(String propertiesName, String propertiesKey){
    	PropertiesUtil mPropertiesUnit = new PropertiesUtil();
    	Properties mProperties = mPropertiesUnit.getProperties(propertiesName);
    	 String value = null;
         value = mProperties.getProperty(propertiesKey);
         return value;
    }

}