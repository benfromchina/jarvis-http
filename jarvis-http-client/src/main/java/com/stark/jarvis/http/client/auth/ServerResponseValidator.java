package com.stark.jarvis.http.client.auth;

import com.stark.jarvis.cipher.core.AsymmetricAlgorithm;
import com.stark.jarvis.cipher.core.verifier.Verifier;
import com.stark.jarvis.cipher.rsa.verifier.RSAVerifier;
import com.stark.jarvis.cipher.sm.verifier.SM2Verifier;
import com.stark.jarvis.http.client.http.HttpHeaders;
import com.stark.jarvis.http.sign.util.VerifyUtils;
import org.apache.commons.lang3.StringUtils;

import java.security.PublicKey;

import static java.util.Objects.requireNonNull;

/**
 * 服务端响应验签器
 *
 * @author <a href="mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2025/8/14
 */
public final class ServerResponseValidator {

    private final Verifier serverResponseVerifier;

    public ServerResponseValidator(PublicKey serverPublicKey) {
        requireNonNull(serverPublicKey);
        this.serverResponseVerifier = AsymmetricAlgorithm.RSA.getKeyAlgorithm().equals(serverPublicKey.getAlgorithm())
                ? new RSAVerifier(serverPublicKey)
                : new SM2Verifier(serverPublicKey);
    }

    /**
     * 验证服务端响应签名
     *
     * @param responseHeaders 响应头
     * @param responseBody    响应体
     * @return 是否验证通过
     */
    public boolean validateResponseSignature(HttpHeaders responseHeaders, String responseBody) {
        String timestamp = StringUtils.defaultIfBlank(responseHeaders.getHeader(HttpHeaders.TIMESTAMP), responseHeaders.getHeader(HttpHeaders.TIMESTAMP.toLowerCase()));
        String nonce = StringUtils.defaultIfBlank(responseHeaders.getHeader(HttpHeaders.NONCE), responseHeaders.getHeader(HttpHeaders.NONCE.toLowerCase()));
        String signature = StringUtils.defaultIfBlank(responseHeaders.getHeader(HttpHeaders.SIGNATURE), responseHeaders.getHeader(HttpHeaders.SIGNATURE.toLowerCase()));
        return VerifyUtils.verifyServerResponseSignature(serverResponseVerifier, responseBody, Long.parseLong(timestamp), nonce, signature);
    }

}
