package com.catplay.mqtt2.msg;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;


/**
 * Created by sunji on 16/8/18.
 */
public class WbJSON {
    private static final Logger logger = Logger.getLogger(WbJSON.class);

    private final static ObjectMapper defaultMapper = new ObjectMapper();

    // 如果有null字段的话,就不输出,用在节省流量的情况
    private final static ObjectMapper pureMapper = new ObjectMapper();

    static {
        // 初始化
        defaultMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        defaultMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        pureMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    // ------------- defaultMapper start -------------

    /**
     * json字符串到对象,默认配置
     * @param json
     * @param valueType
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String json, Class<T> valueType) {
        try {
            return defaultMapper.readValue(json, valueType);
        } catch (Exception e) {
            logger.error("WbJSON fromJson " + json , e);
            return null;
        }
    }

    /**
     * json字符串到对象,默认配置
     * @param json
     * @param valueTypeRef 例如:new TypeReference<Map<String, Att>>(){}
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String json, TypeReference valueTypeRef) {
        try {
            return defaultMapper.readValue(json, valueTypeRef);
        } catch (Exception e) {
            logger.error("WbJSON fromJson", e);
            return null;
        }
    }

    /**
     * 对象到json字符串,默认配置
     * @param value
     * @return
     */
    public static String toJson(Object value) {
        try {
            return defaultMapper.writeValueAsString(value);
        } catch (Exception e) {
            logger.error("WbJSON toJson", e);
            return null;
        }
    }

    // ------------- defaultMapper end -------------


    // ------------- pureMapper start -------------

    /**
     * json字符串到对象,如果有null字段的话,就不输出,用在节省流量的情况
     * @param json
     * @param valueType
     * @param <T>
     * @return
     */
    public static <T> T fromJsonPure(String json, Class<T> valueType) {
        try {
            return pureMapper.readValue(json, valueType);
        } catch (Exception e) {
            logger.error("WbJSON fromJsonPure", e);
            return null;
        }
    }

    /**
     * json字符串到对象,如果有null字段的话,就不输出,用在节省流量的情况
     * @param json
     * @param valueTypeRef 例如:new TypeReference<Map<String, Att>>(){}
     * @param <T>
     * @return
     */
    public static <T> T fromJsonPure(String json, TypeReference valueTypeRef) {
        try {
            return pureMapper.readValue(json, valueTypeRef);
        } catch (Exception e) {
            logger.error("WbJSON fromJsonPure", e);
            return null;
        }
    }

    /**
     * 对象到json字符串,如果有null字段的话,就不输出,用在节省流量的情况
     * @param value
     * @return
     */
    public static String toJsonPure(Object value) {
        try {
            return pureMapper.writeValueAsString(value);
        } catch (Exception e) {
            logger.error("WbJSON toJsonPure", e);
            return null;
        }
    }
    // ------------- pureMapper end -------------

}
