package com.stark.jarvis.http.client.config;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

import static java.util.Objects.requireNonNull;

/**
 * RSA相关配置
 *
 * @author <a href="mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2025/8/14
 */
public class RSAConfig extends AbstractRSAConfig {

    private RSAConfig(String clientId, String clientSecret, X509Certificate clientCert, PrivateKey clientPrivateKey, PublicKey serverPublicKey) {
        super(clientId, clientSecret, clientCert, clientPrivateKey, serverPublicKey);
    }

    public static class Builder extends AbstractRSAConfigBuilder<Builder> {

        @Override
        protected Builder self() {
            return this;
        }

        public RSAConfig build() {
            return new RSAConfig(
                    requireNonNull(clientId),
                    requireNonNull(clientSecret),
                    requireNonNull(clientCert),
                    requireNonNull(clientPrivateKey),
                    requireNonNull(serverPublicKey));
        }

    }

}
