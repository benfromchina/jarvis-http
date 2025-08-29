package com.stark.jarvis.http.client.crypto.aead;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.stark.jarvis.cipher.core.AeadAlgorithm;
import com.stark.jarvis.cipher.core.aead.AeadCipher;
import com.stark.jarvis.cipher.rsa.aead.AeadAesCipher;
import com.stark.jarvis.cipher.sm.aead.AeadSM4Cipher;
import com.stark.jarvis.http.client.constant.SystemConsts;
import com.stark.jarvis.http.client.util.JacksonUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 对称加密数据反序列化器
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2025/8/26
 */
@NoArgsConstructor
@AllArgsConstructor
public class EncryptDataDeserializer extends JsonDeserializer<Object> implements ContextualDeserializer {

    private JavaType type;

    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        String ciphertext = node.toPrettyString();
        EncryptData encryptData = JacksonUtils.deserialize(ciphertext, EncryptData.class);
        AeadCipher cipher = AeadAlgorithm.SM4.equals(encryptData.getAlgorithm())
                ? new AeadSM4Cipher(SystemConsts.CLIENT_AEAD_KEY.getBytes(StandardCharsets.UTF_8))
                : new AeadAesCipher(SystemConsts.CLIENT_AEAD_KEY.getBytes(StandardCharsets.UTF_8));
        String plaintext = cipher.decrypt(
                encryptData.getAssociatedData() != null ? encryptData.getAssociatedData().getBytes(StandardCharsets.UTF_8) : null,
                encryptData.getNonce().getBytes(StandardCharsets.UTF_8),
                Base64.getDecoder().decode(encryptData.getCiphertext()));
        return JacksonUtils.deserialize(plaintext, type);
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
        if (property != null) {
            AeadEncrypt aeadEncrypt = property.getAnnotation(AeadEncrypt.class);
            if (aeadEncrypt != null) {
                return new EncryptDataDeserializer(property.getType());
            }
            return ctxt.findContextualValueDeserializer(property.getType(), property);
        }
        return null;
    }

}
