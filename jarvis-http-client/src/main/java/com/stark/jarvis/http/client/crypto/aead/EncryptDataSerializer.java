package com.stark.jarvis.http.client.crypto.aead;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.stark.jarvis.cipher.core.AeadAlgorithm;
import com.stark.jarvis.cipher.core.aead.AeadCipher;
import com.stark.jarvis.cipher.rsa.aead.AeadAesCipher;
import com.stark.jarvis.cipher.sm.aead.AeadSM4Cipher;
import com.stark.jarvis.http.client.constant.SystemConsts;
import com.stark.jarvis.http.client.util.JacksonUtils;
import com.stark.jarvis.http.sign.util.NonceUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 对称加密数据序列化器
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2025/8/26
 */
@NoArgsConstructor
@AllArgsConstructor
public class EncryptDataSerializer extends JsonSerializer<Object> implements ContextualSerializer {

    private String associatedData;

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String plaintext = JacksonUtils.serializeNonNullNonEmpty(value);

        AeadCipher cipher = AeadAlgorithm.SM4.equals(SystemConsts.CLIENT_AEAD_ALGORITHM)
                ? new AeadSM4Cipher(SystemConsts.CLIENT_AEAD_KEY.getBytes(StandardCharsets.UTF_8))
                : new AeadAesCipher(SystemConsts.CLIENT_AEAD_KEY.getBytes(StandardCharsets.UTF_8));
        String nonce = NonceUtils.createNonce(16);
        String ciphertext = cipher.encrypt(
                associatedData != null ? associatedData.getBytes(StandardCharsets.UTF_8) : null,
                nonce.getBytes(StandardCharsets.UTF_8),
                plaintext.getBytes(StandardCharsets.UTF_8));
        EncryptData encryptData = new EncryptData(SystemConsts.CLIENT_AEAD_ALGORITHM, nonce, associatedData, ciphertext);
        gen.writeObject(encryptData);
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        if (property != null) {
            AeadEncrypt aeadEncrypt = property.getAnnotation(AeadEncrypt.class);
            if (aeadEncrypt != null) {
                return new EncryptDataSerializer(StringUtils.trimToNull(aeadEncrypt.associatedData()));
            }
            return prov.findValueSerializer(property.getType(), property);
        }
        return prov.findNullValueSerializer(null);
    }

}
