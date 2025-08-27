package com.stark.jarvis.http.sign.constant;

/**
 * 签名相关常量
 *
 * @author <a href="mengbin@eastsoft.com.cn">Ben</a>
 * @version 1.0.0
 * @since 2025/8/14
 */
public final class SignConsts {

    /**
     * 签名请求头前缀，如 "Jarvis-"，默认为空
     */
    public static final String SIGN_HEADER_PREFIX = System.getProperty(SystemPropertyConsts.SIGN_HEADER_PREFIX, "");

    /**
     * 签名算法前缀，如 "JARVIS-"，默认为空
     */
    public static final String SIGN_ALGORITHM_PREFIX = System.getProperty(SystemPropertyConsts.SIGN_ALGORITHM_PREFIX, "");

    /**
     * 签名随机字符串长度，默认32
     */
    public static final int SIGN_NONCE_LENGTH = Integer.parseInt(System.getProperty(SystemPropertyConsts.SIGN_NONCE_LENGTH, "32"));

    /**
     * 签名过期分钟数，默认5分钟
     */
    public static final int SIGN_EXPIRED_MINUTES = Integer.parseInt(System.getProperty(SystemPropertyConsts.SIGN_EXPIRED_MINUTES, "5"));

}
