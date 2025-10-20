package com.stark.jarvis.http.client.http.forest;

import com.dtflys.forest.backend.url.QueryableURLBuilder;
import com.dtflys.forest.backend.url.URLBuilder;
import com.dtflys.forest.config.ForestConfiguration;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.interceptor.ForestInterceptor;
import com.stark.jarvis.http.client.config.Config;
import com.stark.jarvis.http.client.util.ForestUtils;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * 设置参数拦截器
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2025/10/15
 */
public class SetVariablesInterceptor implements ForestInterceptor {

    private static final URLBuilder URL_BUILDER = new QueryableURLBuilder();

    @Override
    public boolean beforeExecute(ForestRequest request) {
        String url = URL_BUILDER.buildUrl(request);
        try {
            URI uri = new URI(url);
            String canonicalUrl = uri.getRawPath();
            if (uri.getQuery() != null) {
                canonicalUrl += "?" + uri.getRawQuery();
            }
            System.out.println("-->canonicalUrl=" + canonicalUrl);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        ForestConfiguration config = request.getConfiguration();
        Config clientConfig = ForestUtils.getClientConfig();
        config.setVariable("config", clientConfig);
        config.setVariable("userAgent", ForestUtils.getUserAgent(request.getMethod().getClass(), config, clientConfig));

        return true;
    }

}
