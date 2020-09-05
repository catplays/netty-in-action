package com.catplay.netty.util;

import com.google.gson.Gson;

/**
 * @Author wangyong
 * @Date 2020-04-01
 */
public class JsonUtil {
    private static final Gson GSON = new Gson();
    public static <T> T fromJson(String jsonStr, Class<T> clazz){
        return GSON.fromJson(jsonStr, clazz);
    }

    public static String toJson(Object object){
        return GSON.toJson(object);
    }
}
