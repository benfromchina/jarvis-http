package com.stark.jarvis.http.client.http;

import lombok.Getter;

import static java.util.Objects.requireNonNull;

/**
 * 枚举HTTP媒体类型
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2025/8/22
 */
@Getter
public enum MediaType {

    TEXT_PLAIN("text/plain"),

    APPLICATION_JSON("application/json"),

    MULTIPART_FORM_DATA("multipart/form-data"),

    APPLICATION_X_GZIP("application/x-gzip"),

    APPLICATION_OCTET_STREAM("application/octet-stream");

    private final String value;

    MediaType(String value) {
        this.value = value;
    }

    public boolean equalsWith(String string) {
        requireNonNull(string);
        return string.startsWith(value);
    }

}
