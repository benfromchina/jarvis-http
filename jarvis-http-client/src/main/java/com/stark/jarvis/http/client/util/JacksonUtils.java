package com.stark.jarvis.http.client.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Jackson 工具类
 *
 * @author <a href="mengbin@eastsoft.com.cn">Ben</a>
 * @version 1.0.0
 * @since 2025/8/15
 */
public class JacksonUtils {

    private static final Module LONG_TO_STRING_MODULE = new SimpleModule()
            .addSerializer(Long.class, ToStringSerializer.instance)
            .addSerializer(Long.TYPE, ToStringSerializer.instance);

    private static final ObjectMapper OBJECT_MAPPER = JacksonUtils.createObjectMapper(true)
            .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
            .registerModule(LONG_TO_STRING_MODULE);

    private static final ObjectMapper OBJECT_MAPPER_NON_NULL_NON_EMPTY = JacksonUtils.createObjectMapper(true)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
            .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
            .registerModule(LONG_TO_STRING_MODULE);

    /**
     * 创建 {@link ObjectMapper} 对象
     *
     * @param findModules 是否自动加载模块
     * @return {@link ObjectMapper} 对象
     */
    public static ObjectMapper createObjectMapper(boolean findModules) {
        ObjectMapper objectMapper = new ObjectMapper();
        if (findModules) {
            List<Module> modules = ObjectMapper.findModules()
                    .stream()
                    .filter(module -> !"com.fasterxml.jackson.module.afterburner.AfterburnerModule".equals(module.getClass().getName()))
                    .collect(Collectors.toList());
            objectMapper.registerModules(modules);

            try {
                Class.forName("org.springframework.data.domain.Pageable");

                try {
                    Class<?> clazz = Class.forName("org.springframework.cloud.openfeign.support.PageJacksonModule");
                    objectMapper.registerModules((Module) clazz.getConstructor().newInstance());
                } catch (ClassNotFoundException e) {
                    // do nothing
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                try {
                    Class<?> clazz = Class.forName("org.springframework.cloud.openfeign.support.SortJacksonModule");
                    objectMapper.registerModules((Module) clazz.getConstructor().newInstance());
                } catch (ClassNotFoundException e) {
                    // do nothing
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } catch (ClassNotFoundException e) {
                // do nothing
            }
        }
        return objectMapper;
    }

    /**
     * 序列化对象为 json 字符串
     *
     * @param object 对象
     * @return json 字符串
     */
    public static String serialize(Object object) {
        if (object == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("序列化失败: " + e.getMessage(), e);
        }
    }

    /**
     * 序列化对象为 json 字符串，忽略 null 和 empty 字段
     *
     * @param object 对象
     * @return json 字符串
     */
    public static String serializeNonNullNonEmpty(Object object) {
        if (object == null) {
            return "";
        }
        try {
            return OBJECT_MAPPER_NON_NULL_NON_EMPTY.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("序列化失败: " + e.getMessage(), e);
        }
    }

    /**
     * 反序列化 json 字符串为对象
     *
     * @param json  json 字符串
     * @param clazz 对象类
     * @param <T>   对象类型
     * @return 对象
     */
    public static <T> T deserialize(String json, Class<T> clazz) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        if (clazz == null) {
            throw new IllegalArgumentException("未指定反序列化类型");
        }
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("反序列化失败: " + e.getMessage(), e);
        }
    }

    /**
     * 反序列化 json 字符串为指定的 java 格式
     *
     * @param json         json 字符串
     * @param valueTypeRef 指定 java 格式
     * @return 指定 java 格式的对象
     */
    public static <T> T deserialize(String json, TypeReference<T> valueTypeRef) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        if (valueTypeRef == null) {
            throw new IllegalArgumentException("未指定反序列化类型");
        }
        try {
            return OBJECT_MAPPER.readValue(json, valueTypeRef);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("反序列化失败: " + e.getMessage(), e);
        }
    }

    /**
     * 反序列化 json 字符串为指定的 java 格式
     *
     * @param json      json 字符串
     * @param valueType 指定 java 格式
     * @return 指定 java 格式的对象
     */
    public static <T> T deserialize(String json, JavaType valueType) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        if (valueType == null) {
            throw new IllegalArgumentException("未指定反序列化类型");
        }
        try {
            return OBJECT_MAPPER.readValue(json, valueType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("反序列化失败: " + e.getMessage(), e);
        }
    }

    /**
     * 反序列化 json 字符串为对象列表
     *
     * @param json  json 字符串
     * @param clazz 对象类
     * @param <T>   对象类型
     * @return 对象列表
     */
    public static <T> List<T> deserializeList(String json, Class<T> clazz) {
        if (StringUtils.isBlank(json)) {
            return Collections.emptyList();
        }
        JavaType valueType = OBJECT_MAPPER.getTypeFactory().constructParametricType(List.class, clazz);
        return deserialize(json, valueType);
    }

    /**
     * 反序列化 json 字符串为 {@link Map}
     *
     * @param json       json 字符串
     * @param keyClass   键类型
     * @param valueClass 值类型
     * @param <K>        键类型
     * @param <V>        值类型
     * @return {@link Map}
     */
    public static <K, V> Map<K, V> deserializeMap(String json, Class<K> keyClass, Class<V> valueClass) {
        if (StringUtils.isBlank(json)) {
            return Collections.emptyMap();
        }
        JavaType valueType = OBJECT_MAPPER.getTypeFactory().constructParametricType(Map.class, keyClass, valueClass);
        return deserialize(json, valueType);
    }

    /**
     * 构造泛型类型
     *
     * @param parametrized     泛型类
     * @param parameterClasses 泛型参数
     * @return 泛型类型
     */
    public static JavaType constructParametricType(Class<?> parametrized, Class<?>... parameterClasses) {
        return OBJECT_MAPPER.getTypeFactory().constructParametricType(parametrized, parameterClasses);
    }

    /**
     * 构造泛型类型
     *
     * @param rawType        泛型类
     * @param parameterTypes 泛型参数
     * @return 泛型类型
     */
    public static JavaType constructParametricType(Class<?> rawType, JavaType... parameterTypes) {
        return OBJECT_MAPPER.getTypeFactory().constructParametricType(rawType, parameterTypes);
    }

    /**
     * 将对象转换为指定类型
     *
     * @param fromValue   原对象
     * @param toValueType 目标类型
     * @param <T>         目标类型
     * @return 目标类型对象
     */
    public static <T> T convertValue(Object fromValue, Class<T> toValueType) {
        return OBJECT_MAPPER.convertValue(fromValue, toValueType);
    }

    /**
     * 将对象转换为指定类型
     *
     * @param fromValue      原对象
     * @param toValueTypeRef 目标类型
     * @param <T>            目标类型
     * @return 目标类型对象
     */
    public static <T> T convertValue(Object fromValue, TypeReference<T> toValueTypeRef) {
        return OBJECT_MAPPER.convertValue(fromValue, toValueTypeRef);
    }

    /**
     * 将对象转换为指定类型
     *
     * @param fromValue   原对象
     * @param toValueType 目标类型
     * @param <T>         目标类型
     * @return 目标类型对象
     */
    public static <T> T convertValue(Object fromValue, JavaType toValueType) {
        return OBJECT_MAPPER.convertValue(fromValue, toValueType);
    }

    /**
     * 读取 json 字符串为 {@link JsonNode}
     *
     * @param json json 字符串
     * @return {@link JsonNode}
     */
    public static JsonNode readTree(String json) {
        try {
            return OBJECT_MAPPER.readTree(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("反序列化失败: " + e.getMessage(), e);
        }
    }

    /**
     * 创建 {@link ObjectNode}
     *
     * @return {@link ObjectNode}
     */
    public static ObjectNode createObjectNode() {
        return OBJECT_MAPPER.createObjectNode();
    }

    /**
     * 创建 {@link ArrayNode}
     *
     * @return {@link ArrayNode}
     */
    public static ArrayNode createArrayNode() {
        return OBJECT_MAPPER.createArrayNode();
    }

}
