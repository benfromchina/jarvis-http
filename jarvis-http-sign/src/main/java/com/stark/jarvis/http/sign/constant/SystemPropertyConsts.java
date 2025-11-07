package com.stark.jarvis.http.sign.constant;

/**
 * 系统参数常量
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2025/8/26
 */
public interface SystemPropertyConsts {

    /**
     * 签名请求头前缀，如 "Jarvis-"，默认为空
     */
    String SIGN_HEADER_PREFIX = "sign.header.prefix";

    /**
     * 签名算法前缀，如 "JARVIS-"，默认为空
     */
    String SIGN_ALGORITHM_PREFIX = "sign.algorithm.prefix";

    /**
     * 签名随机字符串长度，默认32
     */
    String SIGN_NONCE_LENGTH = "sign.nonce.length";

    /**
     * 签名过期分钟数，默认5分钟
     */
    String SIGN_EXPIRED_MINUTES = "sign.expired.minutes";

    /**
     * 控制台打印客户端请求签名信息
     */
    String STDOUT_SIGN_CLIENT_REQUEST = "stdout.sign_client_request";

    /**
     * 控制台打印服务端响应签名信息
     */
    String STDOUT_SIGN_SERVER_RESPONSE = "stdout.sign_server_response";

}
