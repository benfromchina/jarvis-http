package com.stark.jarvis.http.client.exception;

/**
 * 验证签名失败时抛出
 */
public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = -3193191439500609966L;

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

}
