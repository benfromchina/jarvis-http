package com.stark.jarvis.http.client.util;

import com.dtflys.forest.backend.HttpBackend;
import com.dtflys.forest.backend.httpclient.HttpclientBackend;
import com.dtflys.forest.backend.okhttp3.OkHttp3Backend;
import com.dtflys.forest.backend.url.QueryableURLBuilder;
import com.dtflys.forest.backend.url.URLBuilder;
import com.dtflys.forest.config.ForestConfiguration;
import com.dtflys.forest.http.ForestRequest;
import com.stark.jarvis.cipher.core.AsymmetricAlgorithm;
import com.stark.jarvis.http.client.config.Config;
import com.stark.jarvis.http.client.config.RSAConfig;
import com.stark.jarvis.http.client.config.SM2Config;
import com.stark.jarvis.http.client.constant.SystemConsts;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;

import java.net.URI;

/**
 * Forest 工具类
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2025/10/16
 */
public class ForestUtils {

    private static final Config CLIENT_CONFIG;

    private static final URLBuilder URL_BUILDER = new QueryableURLBuilder();

    static {
        if (AsymmetricAlgorithm.SM2.equals(SystemConsts.CLIENT_ASYMMETRIC_ALGORITHM)) {
            CLIENT_CONFIG = new SM2Config.Builder()
                    .clientId(SystemConsts.CLIENT_ID)
                    .clientSecret(SystemConsts.CLIENT_SECRET)
                    .clientCert(SystemConsts.CLIENT_CERT)
                    .clientPrivateKey(SystemConsts.CLIENT_PRIVATE_KEY)
                    .serverPublicKey(SystemConsts.SERVER_PUBLIC_KEY)
                    .build();
        } else {
            CLIENT_CONFIG = new RSAConfig.Builder()
                    .clientId(SystemConsts.CLIENT_ID)
                    .clientSecret(SystemConsts.CLIENT_SECRET)
                    .clientCert(SystemConsts.CLIENT_CERT)
                    .clientPrivateKey(SystemConsts.CLIENT_PRIVATE_KEY)
                    .serverPublicKey(SystemConsts.SERVER_PUBLIC_KEY)
                    .build();
        }
    }

    /**
     * 重新构建 URI 对象，防止查询参数丢失
     *
     * @param request 请求对象
     * @return URI 对象
     */
    @SneakyThrows
    public static URI getURI(ForestRequest<?> request) {
        String url = URL_BUILDER.buildUrl(request);
        return new URI(url);
    }

    /**
     * 获取客户端配置
     *
     * @return 客户端配置
     */
    public static Config getClientConfig() {
        return CLIENT_CONFIG;
    }

    /**
     * 组织 User-Agent 请求头
     *
     * @param clazz        接口类
     * @param config       Forest 配置
     * @param clientConfig 客户端配置
     * @return User-Agent 请求头
     */
    public static String getUserAgent(Class<?> clazz, ForestConfiguration config, Config clientConfig) {
        return String.format(
                SystemConsts.USER_AGENT_FORMAT,
                StringUtils.defaultIfBlank(clazz.getPackage().getImplementationVersion(), "Unknown"),
                SystemConsts.OS,
                StringUtils.defaultIfBlank(SystemConsts.JAVA_VERSION, "Unknown"),
                clientConfig.getClientCredential().getClass().getSimpleName(),
                clientConfig.getServerResponseValidator().getClass().getSimpleName(),
                getHttpClientInfo(config.getBackend()));
    }

    /**
     * 组织 HTTP 客户端信息
     *
     * @param backend HTTP 底层实现
     * @return HTTP 客户端信息
     */
    public static String getHttpClientInfo(HttpBackend backend) {
        String clientName = backend.getName();
        String clientVersion;
        if (HttpclientBackend.NAME.equals(clientName)) {
            clientVersion = HttpClient.class.getPackage().getImplementationVersion();
        } else if (OkHttp3Backend.NAME.equals(clientName)) {
            clientVersion = OkHttpClient.class.getPackage().getImplementationVersion();
        } else {
            clientVersion = "Unknown";
        }
        return clientName + "/" + clientVersion;
    }

}
