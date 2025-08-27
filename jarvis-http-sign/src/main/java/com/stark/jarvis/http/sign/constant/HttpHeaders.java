package com.stark.jarvis.http.sign.constant;

import java.util.Map;

/**
 * 签名相关的 HTTP 请求头
 *
 * @author <a href="mengbin@eastsoft.com.cn">Ben</a>
 * @version 1.0.0
 * @since 2025/8/14
 */
public class HttpHeaders extends com.stark.jarvis.http.core.HttpHeaders {

    public static final String SIGNATURE = SignConsts.SIGN_HEADER_PREFIX + "Signature";

    public static final String TIMESTAMP = SignConsts.SIGN_HEADER_PREFIX + "Timestamp";

    public static final String NONCE = SignConsts.SIGN_HEADER_PREFIX + "Nonce";

    public HttpHeaders(Map<String, String> headers) {
        super(headers);
    }

}
