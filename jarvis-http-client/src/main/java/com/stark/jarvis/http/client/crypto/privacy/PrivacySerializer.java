package com.stark.jarvis.http.client.crypto.privacy;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.stark.jarvis.cipher.core.AsymmetricAlgorithm;
import com.stark.jarvis.cipher.core.privacy.PrivacyEncryptor;
import com.stark.jarvis.cipher.rsa.RSAPemUtils;
import com.stark.jarvis.cipher.rsa.privacy.RSAPrivacyEncryptor;
import com.stark.jarvis.cipher.sm.SMPemUtils;
import com.stark.jarvis.cipher.sm.privacy.SM2PrivacyEncryptor;
import com.stark.jarvis.http.client.constant.SystemPropertyConsts;
import com.stark.jarvis.http.client.util.JacksonUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;

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

        String serverPublicKeyPath = System.getProperty(SystemPropertyConsts.SERVER_PUBLIC_KEY_PATH);
        String serverPublicKeyString;
        if (serverPublicKeyPath.startsWith("classpath:")) {
            serverPublicKeyPath = StringUtils.substringAfter(serverPublicKeyPath, "classpath:");
            InputStream in = getClass().getClassLoader().getResourceAsStream(serverPublicKeyPath);
            serverPublicKeyString = IOUtils.toString(in, StandardCharsets.UTF_8);
        } else {
            serverPublicKeyString = FileUtils.readFileToString(new File(serverPublicKeyPath), StandardCharsets.UTF_8);
        }
        PrivacyEncryptor privacyEncryptor;
        AsymmetricAlgorithm algorithm = AsymmetricAlgorithm.valueOf(System.getProperty(SystemPropertyConsts.CLIENT_ASYMMETRIC_ALGORITHM));
        if (AsymmetricAlgorithm.SM2.equals(algorithm)) {
            PublicKey serverPublicKey = SMPemUtils.loadPublicKeyFromString(serverPublicKeyString);
            privacyEncryptor = new SM2PrivacyEncryptor(serverPublicKey);
        } else {
            PublicKey serverPublicKey = RSAPemUtils.loadPublicKeyFromString(serverPublicKeyString);
            privacyEncryptor = new RSAPrivacyEncryptor(serverPublicKey);
        }

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
