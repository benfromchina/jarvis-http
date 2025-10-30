package com.stark.jarvis.http.client.http.forest;

import com.dtflys.forest.config.ForestConfiguration;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.interceptor.ForestInterceptor;
import com.stark.jarvis.http.client.config.Config;
import com.stark.jarvis.http.client.util.ForestUtils;

/**
 * 设置参数拦截器
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2025/10/15
 */
public class SetVariablesInterceptor implements ForestInterceptor {

    @Override
    public boolean beforeExecute(ForestRequest request) {
        ForestConfiguration config = request.getConfiguration();
        Config clientConfig = ForestUtils.getClientConfig();
        config.setVariable("config", clientConfig);
        config.setVariable("userAgent", ForestUtils.getUserAgent(request.getMethod().getClass(), config, clientConfig));

        return true;
    }

}
