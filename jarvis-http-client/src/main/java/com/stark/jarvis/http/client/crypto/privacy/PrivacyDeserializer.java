package com.stark.jarvis.http.client.crypto.privacy;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.stark.jarvis.cipher.core.AsymmetricAlgorithm;
import com.stark.jarvis.cipher.core.privacy.PrivacyDecryptor;
import com.stark.jarvis.cipher.rsa.RSAPemUtils;
import com.stark.jarvis.cipher.rsa.privacy.RSAPrivacyDecryptor;
import com.stark.jarvis.cipher.sm.SMPemUtils;
import com.stark.jarvis.cipher.sm.privacy.SM2PrivacyDecryptor;
import com.stark.jarvis.http.client.constant.SystemPropertyConsts;
import com.stark.jarvis.http.client.util.JacksonUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;

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

        String clientPrivateKeyPath = System.getProperty(SystemPropertyConsts.CLIENT_PRIVATE_KEY_PATH);
        String clientPrivateKeyString;
        if (clientPrivateKeyPath.startsWith("classpath:")) {
            clientPrivateKeyPath = StringUtils.substringAfter(clientPrivateKeyPath, "classpath:");
            InputStream in = getClass().getClassLoader().getResourceAsStream(clientPrivateKeyPath);
            clientPrivateKeyString = IOUtils.toString(in, StandardCharsets.UTF_8);
        } else {
            clientPrivateKeyString = FileUtils.readFileToString(new File(clientPrivateKeyPath), StandardCharsets.UTF_8);
        }
        PrivacyDecryptor privacyDecryptor;
        AsymmetricAlgorithm algorithm = AsymmetricAlgorithm.valueOf(System.getProperty(SystemPropertyConsts.CLIENT_ASYMMETRIC_ALGORITHM));
        if (AsymmetricAlgorithm.SM2.equals(algorithm)) {
            PrivateKey clientPrivateKey = SMPemUtils.loadPrivateKeyFromString(clientPrivateKeyString);
            privacyDecryptor = new SM2PrivacyDecryptor(clientPrivateKey);
        } else {
            PrivateKey clientPrivateKey = RSAPemUtils.loadPrivateKeyFromString(clientPrivateKeyString);
            privacyDecryptor = new RSAPrivacyDecryptor(clientPrivateKey);
        }

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
