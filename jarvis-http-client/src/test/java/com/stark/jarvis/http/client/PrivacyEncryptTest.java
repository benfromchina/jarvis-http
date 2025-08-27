package com.stark.jarvis.http.client;

import com.stark.jarvis.http.client.crypto.privacy.PrivacyEncrypt;
import com.stark.jarvis.http.client.util.JacksonUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.Test;

public class PrivacyEncryptTest {

    static {
        TestConfig.init();
    }

    @Test
    void encryptThenDecrypt() {
        User user = new User(1L, "123456789012345678", "6222020200041234567");
        String json = JacksonUtils.serialize(user);
        System.out.println("-->serialize: " + json);

        User deserialize = JacksonUtils.deserialize(json, User.class);
        assert user.equals(deserialize);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    static class User {

        private Long id;

        @PrivacyEncrypt
        private String idCardNumber;

        @PrivacyEncrypt
        private String bankCardNumber;

    }

}
