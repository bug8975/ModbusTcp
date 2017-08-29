package com.visenergy.utils;

import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;


public class FileUtils {
    public static Properties loadPropFile(String filePath) throws Exception{
        String rabbitMqPath = Thread.currentThread().getContextClassLoader().getResource("").toString().replace("file:/","");
        FileInputStream fis = new FileInputStream(rabbitMqPath + filePath);
        Properties properties = new Properties();
        try {
            properties.load(fis);
        }catch (IOException ex){
            ex.printStackTrace();
        }finally {
            fis.close();
        }
        return properties;
    }

    public static JSONObject loadJsonFile(String filePath) throws Exception{
        String rabbitMqPath = Thread.currentThread().getContextClassLoader().getResource("").toString().replace("file:/","");
        BufferedReader br = new BufferedReader(new FileReader(rabbitMqPath + filePath));
        StringBuffer jsonStrBuff = new StringBuffer();
        String brStr = null;
        while ((brStr =br.readLine()) != null){
            jsonStrBuff.append(brStr);
        }
        return JSONObject.fromObject(jsonStrBuff.toString());
    }
}
