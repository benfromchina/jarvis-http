package com.stark.jarvis.http.client.crypto.aead;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 对称加密注解
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2025/8/26
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = EncryptDataSerializer.class)
@JsonDeserialize(using = EncryptDataDeserializer.class)
public @interface AeadEncrypt {

    /**
     * 额外的认证加密数据
     *
     * @return 额外的认证加密数据
     */
    String associatedData() default "";

}
