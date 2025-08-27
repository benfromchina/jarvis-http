package com.stark.jarvis.http.client;

import com.stark.jarvis.http.client.crypto.aead.AeadEncrypt;
import com.stark.jarvis.http.client.util.JacksonUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

public class AeadEncryptTest {

    static {
        TestConfig.init();
    }

    @Test
    void serializeThenDeserialize() {
        CertificatePayload payload = new CertificatePayload()
                .setId("1")
                .setName("证书")
                .setCertificate(new Certificate()
                        .setId("1")
                        .setName("证书")
                        .setBytes("12345".getBytes(StandardCharsets.UTF_8)));
        String json = JacksonUtils.serializeNonNullNonEmpty(payload);
        System.out.println("-->serialize: " + json);

        CertificatePayload deserialize = JacksonUtils.deserialize(json, CertificatePayload.class);
        assert payload.equals(deserialize);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    static class CertificatePayload {

        private String id;

        private String name;

        @AeadEncrypt(associatedData = "certificate")
        Certificate certificate;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    static class Certificate {

        private String id;

        private String name;

        private byte[] bytes;

    }

}
