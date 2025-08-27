package com.stark.jarvis.http.client.auth;

import com.stark.jarvis.cipher.core.AsymmetricAlgorithm;
import com.stark.jarvis.cipher.core.PemUtils;
import com.stark.jarvis.cipher.core.privacy.PrivacyEncryptor;
import com.stark.jarvis.cipher.core.signer.Signer;
import com.stark.jarvis.cipher.rsa.privacy.RSAPrivacyEncryptor;
import com.stark.jarvis.cipher.rsa.signer.RSASigner;
import com.stark.jarvis.cipher.sm.privacy.SM2PrivacyEncryptor;
import com.stark.jarvis.cipher.sm.signer.SM2Signer;
import com.stark.jarvis.http.core.HttpMethod;
import com.stark.jarvis.http.sign.constant.SignConsts;
import com.stark.jarvis.http.sign.util.SignUtils;
import lombok.Getter;

import java.net.URI;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

import static java.util.Objects.requireNonNull;

/**
 * 客户端认证凭据生成器
 *
 * @author <a href="mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2025/8/14
 */
@Getter
public final class ClientCredential {

    /**
     * 客户端标识
     */
    private final String clientId;

    /**
     * 客户端秘钥
     */
    private final String clientSecret;

    /**
     * 客户端证书
     */
    private final X509Certificate clientCert;

    /**
     * 客户端私钥
     */
    private final PrivateKey clientPrivateKey;

    /**
     * 服务端公钥
     */
    private final PublicKey serverPublicKey;

    /**
     * 客户端请求签名器
     */
    private final Signer clientRequestSigner;

    /**
     * 服务端响应验签器
     */
    private final PrivacyEncryptor serverPrivacyEncryptor;

    public ClientCredential(String clientId, String clientSecret, X509Certificate clientCert, PrivateKey clientPrivateKey,
                            PublicKey serverPublicKey) {
        this.clientId = requireNonNull(clientId);
        this.clientSecret = requireNonNull(clientSecret);
        this.clientCert = requireNonNull(clientCert);
        this.clientPrivateKey = requireNonNull(clientPrivateKey);
        this.serverPublicKey = requireNonNull(serverPublicKey);
        this.clientRequestSigner = AsymmetricAlgorithm.RSA.getKeyAlgorithm().equals(clientPrivateKey.getAlgorithm())
                ? new RSASigner(clientPrivateKey)
                : new SM2Signer(clientPrivateKey);
        this.serverPrivacyEncryptor = AsymmetricAlgorithm.RSA.getKeyAlgorithm().equals(serverPublicKey.getAlgorithm())
                ? new RSAPrivacyEncryptor(serverPublicKey)
                : new SM2PrivacyEncryptor(serverPublicKey);
    }

    /**
     * 获取自定义算法名称
     *
     * @return 自定义算法名称
     */
    public String getAlgorithm() {
        return SignConsts.SIGN_ALGORITHM_PREFIX + clientPrivateKey.getAlgorithm();
    }

    /**
     * 获取客户端请求认证信息
     *
     * @param httpMethod  请求方式
     * @param uri         请求uri
     * @param requestBody 请求体
     * @return 客户端请求认证信息
     */
    public String getAuthorization(HttpMethod httpMethod, URI uri, String requestBody) {
        requireNonNull(httpMethod);
        requireNonNull(uri);

        return SignUtils.createClientRequestAuthorization(clientId, clientSecret, PemUtils.getSerialNumber(clientCert), clientRequestSigner,
                httpMethod, uri, requestBody, serverPrivacyEncryptor);
    }

}
