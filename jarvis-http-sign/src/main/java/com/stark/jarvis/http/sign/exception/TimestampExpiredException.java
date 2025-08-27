package com.stark.jarvis.http.sign.exception;


import lombok.Getter;

/**
 * 时间戳过期异常
 *
 * @author <a href="mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2025/8/14
 */
@Getter
public class TimestampExpiredException extends IllegalArgumentException {

    private static final long serialVersionUID = 12363928363361247L;

    private final int expiresInMinutes;

    public TimestampExpiredException(int expiresInMinutes) {
        super("时间戳已过期，有效期 " + expiresInMinutes + " 分钟");
        this.expiresInMinutes = expiresInMinutes;
    }

}
