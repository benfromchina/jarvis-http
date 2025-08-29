package com.stark.jarvis.http.client.config;

import com.stark.jarvis.cipher.core.privacy.PrivacyDecryptor;
import com.stark.jarvis.cipher.core.privacy.PrivacyEncryptor;
import com.stark.jarvis.cipher.core.signer.Signer;
import com.stark.jarvis.cipher.sm.SMPemUtils;
import com.stark.jarvis.cipher.sm.privacy.SM2PrivacyDecryptor;
import com.stark.jarvis.cipher.sm.privacy.SM2PrivacyEncryptor;
import com.stark.jarvis.cipher.sm.signer.SM2Signer;
import com.stark.jarvis.http.client.auth.ClientCredential;
import com.stark.jarvis.http.client.auth.ServerResponseValidator;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

import static java.util.Objects.requireNonNull;

/**
 * 国密SM2配置
 *
 * @author <a href="mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2025/8/14
 */
public final class SM2Config implements Config {

    private final String clientId;

    private final String clientSecret;

    private final X509Certificate clientCert;

    private final PrivateKey clientPrivateKey;

    private final PublicKey serverPublicKey;

    private SM2Config(Builder builder) {
        this.clientId = requireNonNull(builder.clientId);
        this.clientSecret = requireNonNull(builder.clientSecret);
        this.clientCert = requireNonNull(builder.clientCert);
        this.clientPrivateKey = requireNonNull(builder.clientPrivateKey);
        this.serverPublicKey = requireNonNull(builder.serverPublicKey);
    }

    @Override
    public PrivacyEncryptor getEncryptor() {
        return new SM2PrivacyEncryptor(serverPublicKey);
    }

    @Override
    public PrivacyDecryptor getDecryptor() {
        return new SM2PrivacyDecryptor(clientPrivateKey);
    }

    @Override
    public ClientCredential getClientCredential() {
        return new ClientCredential(clientId, clientSecret, clientCert, clientPrivateKey, serverPublicKey);
    }

    @Override
    public ServerResponseValidator getServerResponseValidator() {
        return new ServerResponseValidator(serverPublicKey);
    }

    @Override
    public Signer getClientRequestSigner() {
        return new SM2Signer(clientPrivateKey);
    }

    public static class Builder {

        private String clientId;

        private String clientSecret;

        private X509Certificate clientCert;

        private PrivateKey clientPrivateKey;

        private PublicKey serverPublicKey;

        public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder clientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        public Builder clientCert(String clientCertPem) {
            this.clientCert = SMPemUtils.loadX509FromString(clientCertPem);
            return this;
        }

        public Builder clientCertFromPath(String clientCertPath) {
            this.clientCert = SMPemUtils.loadX509FromPath(clientCertPath);
            return this;
        }

        public Builder clientCert(X509Certificate clientCert) {
            this.clientCert = clientCert;
            return this;
        }

        public Builder clientPrivateKey(String clientPrivateKeyPem) {
            this.clientPrivateKey = SMPemUtils.loadPrivateKeyFromString(clientPrivateKeyPem);
            return this;
        }

        public Builder clientPrivateKeyFromPath(String clientPrivateKeyPath) {
            this.clientPrivateKey = SMPemUtils.loadPrivateKeyFromPath(clientPrivateKeyPath);
            return this;
        }

        public Builder clientPrivateKey(PrivateKey clientPrivateKey) {
            this.clientPrivateKey = clientPrivateKey;
            return this;
        }

        public Builder serverPublicKey(String serverPublicKeyPem) {
            this.serverPublicKey = SMPemUtils.loadPublicKeyFromString(serverPublicKeyPem);
            return this;
        }

        public Builder serverPublicKeyFromPath(String serverPublicKeyPath) {
            this.serverPublicKey = SMPemUtils.loadPublicKeyFromPath(serverPublicKeyPath);
            return this;
        }

        public Builder serverPublicKey(PublicKey serverPublicKey) {
            this.serverPublicKey = serverPublicKey;
            return this;
        }

        public SM2Config build() {
            return new SM2Config(this);
        }
    }

}
