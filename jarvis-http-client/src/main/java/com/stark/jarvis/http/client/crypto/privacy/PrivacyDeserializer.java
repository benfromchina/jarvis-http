package com.stark.jarvis.http.client.crypto.privacy;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.stark.jarvis.cipher.core.AsymmetricAlgorithm;
import com.stark.jarvis.cipher.core.privacy.PrivacyDecryptor;
import com.stark.jarvis.cipher.rsa.privacy.RSAPrivacyDecryptor;
import com.stark.jarvis.cipher.sm.privacy.SM2PrivacyDecryptor;
import com.stark.jarvis.http.client.constant.SystemConsts;
import com.stark.jarvis.http.client.util.JacksonUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.IOException;

/**
 * 非对称加密反序列化器
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2025/8/26
 */
@NoArgsConstructor
@AllArgsConstructor
public class PrivacyDeserializer extends JsonDeserializer<Object> implements ContextualDeserializer {

    private JavaType type;

    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        String ciphertext = node.textValue();

        PrivacyDecryptor privacyDecryptor = AsymmetricAlgorithm.SM2.equals(SystemConsts.CLIENT_ASYMMETRIC_ALGORITHM)
                ? new SM2PrivacyDecryptor(SystemConsts.CLIENT_PRIVATE_KEY)
                : new RSAPrivacyDecryptor(SystemConsts.CLIENT_PRIVATE_KEY);
        String plaintext = privacyDecryptor.decrypt(ciphertext);

        return JacksonUtils.deserialize(plaintext, type);
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
        if (property != null) {
            PrivacyEncrypt privacyEncrypt = property.getAnnotation(PrivacyEncrypt.class);
            if (privacyEncrypt == null) {
                privacyEncrypt = property.getContextAnnotation(PrivacyEncrypt.class);
            }
            if (privacyEncrypt != null) {
                return new PrivacyDeserializer(property.getType());
            }
            return ctxt.findContextualValueDeserializer(property.getType(), property);
        }
        return null;
    }

}
