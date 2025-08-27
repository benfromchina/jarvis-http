package com.stark.jarvis.http.sign.exception;

/**
 * 时间戳不合法异常
 *
 * @author <a href="mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2025/8/14
 */
public class TimestampInvalidException extends IllegalArgumentException {

    private static final long serialVersionUID = -2264431711999734787L;

    public TimestampInvalidException() {
        super("时间戳不合法");
    }

}
