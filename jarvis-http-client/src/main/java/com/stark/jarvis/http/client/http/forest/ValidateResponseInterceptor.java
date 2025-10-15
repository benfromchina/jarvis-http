package com.stark.jarvis.http.client.http.forest;

import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestResponse;
import com.dtflys.forest.interceptor.ForestInterceptor;
import com.dtflys.forest.interceptor.ResponseError;
import com.dtflys.forest.interceptor.ResponseResult;
import com.stark.jarvis.http.client.auth.ServerResponseValidator;
import com.stark.jarvis.http.client.config.Config;
import com.stark.jarvis.http.client.exception.ValidationException;
import com.stark.jarvis.http.client.http.HttpHeaders;
import org.apache.commons.lang3.StringUtils;

/**
 * 响应校验拦截器
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2025/8/21
 */
public class ValidateResponseInterceptor implements ForestInterceptor {

    @Override
    public ResponseResult onResponse(ForestRequest request, ForestResponse response) {
        if (response.isSuccess()) {
            Config config = (Config) request.getConfiguration().getVariable("config").getValue(request);
            String responseBody = response.readAsString();
            ServerResponseValidator serverResponseValidator = config.getServerResponseValidator();
            boolean isValid = serverResponseValidator.validateResponseSignature(new HttpHeaders(response.getHeaders()), responseBody);
            if (!isValid) {
                String requestId = StringUtils.defaultIfBlank(response.getHeaderValue(HttpHeaders.REQUEST_ID), response.getHeaderValue(HttpHeaders.REQUEST_ID.toLowerCase()));
                return new ResponseError(new ValidationException(String.format("校验响应失败，服务端签名错误。%n" +
                                "Request-ID[%s]\tresponseHeader[%s]\tresponseBody[%.1024s]",
                        requestId, response.getHeaders(), responseBody)));
            }
            return ResponseResult.success(responseBody);
        }
        return ResponseResult.RESPONSE_RESULT_PROCEED;
    }

}
