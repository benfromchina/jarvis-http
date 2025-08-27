package com.stark.jarvis.http.client.config;

import com.stark.jarvis.cipher.core.privacy.PrivacyDecryptor;
import com.stark.jarvis.cipher.core.privacy.PrivacyEncryptor;
import com.stark.jarvis.cipher.core.signer.Signer;
import com.stark.jarvis.cipher.rsa.privacy.RSAPrivacyDecryptor;
import com.stark.jarvis.cipher.rsa.privacy.RSAPrivacyEncryptor;
import com.stark.jarvis.cipher.rsa.signer.RSASigner;
import com.stark.jarvis.http.client.auth.ClientCredential;
import com.stark.jarvis.http.client.auth.ServerResponseValidator;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

/**
 * RSA配置抽象类
 *
 * @author <a href="mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2025/8/14
 */
public abstract class AbstractRSAConfig implements Config {

    private final String clientId;

    private final String clientSecret;

    private final X509Certificate clientCert;

    private final PrivateKey clientPrivateKey;

    private final PublicKey serverPublicKey;

    protected AbstractRSAConfig(String clientId, String clientSecret, X509Certificate clientCert, PrivateKey clientPrivateKey,
                                PublicKey serverPublicKey) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.clientCert = clientCert;
        this.clientPrivateKey = clientPrivateKey;
        this.serverPublicKey = serverPublicKey;
    }

    @Override
    public PrivacyEncryptor getEncryptor() {
        return new RSAPrivacyEncryptor(serverPublicKey);
    }

    @Override
    public PrivacyDecryptor getDecryptor() {
        return new RSAPrivacyDecryptor(clientPrivateKey);
    }

    @Override
    public ClientCredential getClientCredential() {
        return new ClientCredential(clientId, clientSecret, clientCert, clientPrivateKey, serverPublicKey);
    }

    @Override
    public Signer getClientRequestSigner() {
        return new RSASigner(clientPrivateKey);
    }

    @Override
    public ServerResponseValidator getServerResponseValidator() {
        return new ServerResponseValidator(serverPublicKey);
    }

}
