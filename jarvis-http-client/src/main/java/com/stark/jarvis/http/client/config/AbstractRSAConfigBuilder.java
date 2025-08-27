package com.stark.jarvis.http.client.config;

import com.stark.jarvis.cipher.rsa.RSAPemUtils;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

/**
 * RSA配置构造器抽象类
 *
 * @author <a href="mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2025/8/14
 */
public abstract class AbstractRSAConfigBuilder<T extends AbstractRSAConfigBuilder<T>> {

    protected String clientId;

    protected String clientSecret;

    protected X509Certificate clientCert;

    protected PrivateKey clientPrivateKey;

    protected PublicKey serverPublicKey;

    protected abstract T self();

    public T clientId(String clientId) {
        this.clientId = clientId;
        return self();
    }

    public T clientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return self();
    }

    public T clientCert(String clientCert) {
        this.clientCert = RSAPemUtils.loadX509FromString(clientCert);
        return self();
    }

    public T clientCertFromPath(String clientCertPath) {
        this.clientCert = RSAPemUtils.loadX509FromPath(clientCertPath);
        return self();
    }

    public T clientPrivateKey(String clientPrivateKey) {
        this.clientPrivateKey = RSAPemUtils.loadPrivateKeyFromString(clientPrivateKey);
        return self();
    }

    public T clientPrivateKeyFromPath(String clientPrivateKeyPath) {
        this.clientPrivateKey = RSAPemUtils.loadPrivateKeyFromPath(clientPrivateKeyPath);
        return self();
    }

    public T serverPublicKey(String serverPublicKey) {
        this.serverPublicKey = RSAPemUtils.loadPublicKeyFromString(serverPublicKey);
        return self();
    }

    public T serverPublicKeyFromPath(String serverPublicKeyPath) {
        this.serverPublicKey = RSAPemUtils.loadPublicKeyFromPath(serverPublicKeyPath);
        return self();
    }

}
