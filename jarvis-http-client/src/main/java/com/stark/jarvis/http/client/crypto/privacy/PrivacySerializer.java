package com.stark.jarvis.http.client.crypto.privacy;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.stark.jarvis.cipher.core.AsymmetricAlgorithm;
import com.stark.jarvis.cipher.core.privacy.PrivacyEncryptor;
import com.stark.jarvis.cipher.rsa.privacy.RSAPrivacyEncryptor;
import com.stark.jarvis.cipher.sm.privacy.SM2PrivacyEncryptor;
import com.stark.jarvis.http.client.constant.SystemConsts;
import com.stark.jarvis.http.client.util.JacksonUtils;

import java.io.IOException;

/**
 * 非对称加密序列化器
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2025/8/26
 */
public class PrivacySerializer extends JsonSerializer<Object> implements ContextualSerializer {

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String plaintext = JacksonUtils.serializeNonNullNonEmpty(value);

        PrivacyEncryptor privacyEncryptor = AsymmetricAlgorithm.SM2.equals(SystemConsts.CLIENT_ASYMMETRIC_ALGORITHM)
                ? new SM2PrivacyEncryptor(SystemConsts.SERVER_PUBLIC_KEY)
                : new RSAPrivacyEncryptor(SystemConsts.SERVER_PUBLIC_KEY);
        String ciphertext = privacyEncryptor.encrypt(plaintext);

        gen.writeString(ciphertext);
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        if (property != null) {
            PrivacyEncrypt privacyEncrypt = property.getAnnotation(PrivacyEncrypt.class);
            if (privacyEncrypt == null) {
                privacyEncrypt = property.getContextAnnotation(PrivacyEncrypt.class);
            }
            if (privacyEncrypt != null) {
                return new PrivacySerializer();
            }
            return prov.findValueSerializer(property.getType(), property);
        }
        return prov.findNullValueSerializer(null);
    }

}
