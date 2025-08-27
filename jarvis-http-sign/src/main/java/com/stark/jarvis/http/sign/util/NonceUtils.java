package com.stark.jarvis.http.sign.util;

import java.security.SecureRandom;

/**
 * 随机串生成工具
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2025/8/14
 */
public class NonceUtils {

    private NonceUtils() {
    }

    private static final char[] SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * 使用SecureRandom生成随机串
     *
     * @param length 随机串长度
     * @return nonce 随机串
     */
    public static String createNonce(int length) {
        char[] buf = new char[length];
        for (int i = 0; i < length; ++i) {
            buf[i] = SYMBOLS[RANDOM.nextInt(SYMBOLS.length)];
        }
        return new String(buf);
    }

}
