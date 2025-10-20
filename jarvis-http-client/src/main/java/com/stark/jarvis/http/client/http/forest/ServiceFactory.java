package com.stark.jarvis.http.client.http.forest;

import com.dtflys.forest.Forest;
import com.dtflys.forest.config.ForestConfiguration;
import com.dtflys.forest.converter.json.ForestJacksonConverter;
import com.dtflys.forest.converter.json.ForestJsonConverter;
import com.dtflys.forest.interceptor.Interceptor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.stark.jarvis.http.client.config.Config;
import com.stark.jarvis.http.client.util.ForestUtils;
import com.stark.jarvis.http.client.util.JacksonUtils;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

/**
 * 接口工厂
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2025/8/22
 */
@Builder
public class ServiceFactory {

    /**
     * json 转换器
     */
    private static final ForestJsonConverter forestJsonConverter;

    /**
     * 配置类
     */
    private static final Config clientConfig;

    /**
     * 连接超时时间，单位毫秒
     */
    private Integer connectTimeout;

    /**
     * 读取超时时间，单位毫秒
     */
    private Integer readTimeout;

    /**
     * 最大重试次数
     */
    private Integer maxRetryCount;

    /**
     * 最大请求重试之间的时间间隔，单位为毫秒
     */
    private Long maxRetryInterval;

    static {
        forestJsonConverter = new ForestJacksonConverter(JacksonUtils.createObjectMapper(true)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false));

        clientConfig = ForestUtils.getClientConfig();
    }

    /**
     * 创建请求接口的动态代理实例
     *
     * @param clazz 请求接口类
     * @param <T>   请求接口类泛型
     * @return 动态代理实例
     */
    public <T> T client(Class<T> clazz) {
        ForestConfiguration config = Forest.config(clazz.getName());
        if (connectTimeout != null) {
            config.setConnectTimeout(connectTimeout);
        }
        if (readTimeout != null) {
            config.setReadTimeout(readTimeout);
        }
        if (maxRetryCount != null) {
            config.setMaxRetryCount(maxRetryCount);
        }
        if (maxRetryInterval != null) {
            config.setMaxRetryInterval(maxRetryInterval);
        }
        config.setJsonConverter(forestJsonConverter);

        List<Class<? extends Interceptor>> interceptors = new ArrayList<>();
        interceptors.add(SetVariablesInterceptor.class);
        interceptors.add(SignRequestInterceptor.class);
        interceptors.add(ValidateResponseInterceptor.class);
        config.setInterceptors(interceptors);

        return config.client(clazz);
    }

}
