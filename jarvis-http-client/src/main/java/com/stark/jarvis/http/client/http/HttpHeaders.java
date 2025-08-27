package com.stark.jarvis.http.client.http;

import java.util.Map;

/**
 * HTTP请求头
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2025/8/22
 */
public class HttpHeaders extends com.stark.jarvis.http.sign.constant.HttpHeaders {

    public static final String AUTHORIZATION = "Authorization";

    public static final String REQUEST_ID = "Request-ID";

    public static final String USER_AGENT = "User-Agent";

    public static final String ACCEPT = "Accept";

    public static final String CONTENT_TYPE = "Content-Type";

    public HttpHeaders(Map<String, String> headers) {
        super(headers);
    }

}
