package com.stark.jarvis.http.client.http.forest;

import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.body.MultipartRequestBody;
import com.dtflys.forest.interceptor.ForestInterceptor;
import com.dtflys.forest.multipart.ForestMultipart;
import com.dtflys.forest.utils.ForestDataType;
import com.stark.jarvis.http.client.config.Config;
import com.stark.jarvis.http.client.http.MediaType;
import com.stark.jarvis.http.client.util.JacksonUtils;
import com.stark.jarvis.http.core.FileMeta;
import com.stark.jarvis.http.core.HttpMethod;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 请求签名拦截器
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2025/8/21
 */
public class SignRequestInterceptor implements ForestInterceptor {

    @Override
    public boolean beforeExecute(ForestRequest request) {
        // 1. 请求方式
        String httpMethod = request.getType().getName();
        // 2. 请求体
        String requestBody = "";
        String contentType = StringUtils.trimToEmpty(request.getContentType());
        if (MediaType.APPLICATION_JSON.getValue().equals(contentType) || ForestDataType.JSON.equals(request.getDataType())) {
            requestBody = request.getBody().encodeToString();
            // 重新设置请求体，防止被 Forest 重复序列化，造成签名错误
            request.replaceBody(requestBody);
        } else if (contentType.contains(MediaType.MULTIPART_FORM_DATA.getValue())) {
            List<MultipartRequestBody> multipartItems = request.getBody().getMultipartItems();
            if (multipartItems.size() > 1) {
                throw new IllegalStateException("仅支持单文件上传");
            }
            ForestMultipart<?, ?> multipart = multipartItems.get(0).getMultipart();
            String filename = multipart.getOriginalFileName();
            String sha256 = DigestUtils.sha256Hex(multipart.getBytes());
            FileMeta meta = new FileMeta(filename, sha256);
            requestBody = JacksonUtils.serialize(meta);
            request.addBody("meta", requestBody);
        }

        Config config = (Config) request.getConfiguration().getVariable("config").getValue(request);
        String authorization = config.getClientCredential().getAuthorization(HttpMethod.valueOf(httpMethod), request.getURI(), requestBody);
        request.addHeader("Authorization", authorization);

        String userAgent = (String) request.getConfiguration().getVariable("userAgent").getValue(request);
        request.setUserAgent(userAgent);
        return true;
    }

}
