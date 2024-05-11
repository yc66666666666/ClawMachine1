package com.doll.test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import org.springframework.util.DigestUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class sah {
    public static void main(String[] args) {
//        String jsonString = "{\"msg\":\"操作成功!\",\"code\":\"200\",\"data\":{\"accessToken\":\"at.8zzbvf8n3qav47l249crhv2jbevttlw3-6pnby3jwi5-1qbledq-6bykqgfyf\",\"expireTime\":1715869894221}}";
//
//        try {
//            // 创建 ObjectMapper 实例
//            ObjectMapper mapper = new ObjectMapper();
//
//            // 将 JSON 字符串转换为 Map 对象
//            Map<String, Object> jsonMap = mapper.readValue(jsonString, new TypeReference<Map<String, Object>>() {});
//
//            // 输出结果
//            System.out.println(jsonMap);
//            System.out.println(jsonMap.keySet());
//            System.out.println(jsonMap.values());
//            System.out.println(jsonMap.get("data"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }



//        String jsonString = "{\"msg\":\"操作成功!\",\"code\":\"200\",\"data\":{\"accessToken\":\"at.8zzbvf8n3qav47l249crhv2jbevttlw3-6pnby3jwi5-1qbledq-6bykqgfyf\",\"expireTime\":1715869894221}}";
//
//        // 创建 Gson 实例
//        Gson gson = new Gson();
//
//        // 定义 Map 的类型
//        Type type = new TypeToken<Map<String, Object>>() {}.getType();
//
//        // 将 JSON 字符串转换为 Map 对象
//        Map<String, Object> jsonMap = gson.fromJson(jsonString, type);
//
//        // 输出结果
//        System.out.println(jsonMap);
//        System.out.println(jsonMap.keySet());
//        System.out.println(jsonMap.values());
//        System.out.println(jsonMap.get("data"));





        String inputString = "{msg=操作成功!, code=200, data={accessToken=at.8zzbvf8n3qav47l249crhv2jbevttlw3-6pnby3jwi5-1qbledq-6bykqgfyf, expireTime=1.715869894221E12}}";

        // 创建一个用于查找 accessToken 值的正则表达式
        Pattern pattern = Pattern.compile("accessToken=([^,]+)");
        Matcher matcher = pattern.matcher(inputString);

        // 查找匹配项
        if (matcher.find()) {
            String accessToken = matcher.group(1);
            System.out.println("Access Token: " + accessToken);
        } else {
            System.out.println("Access Token not found.");
        }

    }
}


