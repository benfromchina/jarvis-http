package com.stark.jarvis.http.sign.util;

import com.stark.jarvis.cipher.core.privacy.PrivacyEncryptor;
import com.stark.jarvis.cipher.core.signer.Signer;
import com.stark.jarvis.http.core.HttpMethod;
import com.stark.jarvis.http.sign.constant.SignConsts;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.time.ZonedDateTime;

import static java.util.Objects.requireNonNull;

/**
 * 签名工具类
 *
 * @author <a href="mengbin@eastsoft.com.cn">Ben</a>
 * @version 1.0.0
 * @since 2025/8/14
 */
@Slf4j
public class SignUtils {

    /**
     * 获取签名时间戳
     *
     * @return 签名时间戳
     */
    public static long getTimestamp() {
        return ZonedDateTime.now().toInstant().getEpochSecond();
    }

    /**
     * 生成随机字符串
     *
     * @return 随机字符串
     */
    public static String createNonce() {
        return NonceUtils.createNonce(SignConsts.SIGN_NONCE_LENGTH);
    }

    /**
     * 生成客户端请求授权信息
     *
     * @param clientId               客户端标识
     * @param clientSecret           客户端秘钥
     * @param serialNumber           客户端证书序列号
     * @param clientRequestSigner    客户端请求签名器
     * @param httpMethod             请求方法
     * @param uri                    请求URI
     * @param requestBody            请求体
     * @param serverPrivacyEncryptor 服务端隐私加密器
     * @return 客户端请求授权信息
     */
    public static String createClientRequestAuthorization(String clientId, String clientSecret, String serialNumber, Signer clientRequestSigner,
                                                          HttpMethod httpMethod, URI uri, String requestBody, PrivacyEncryptor serverPrivacyEncryptor) {
        requireNonNull(clientId);
        requireNonNull(clientSecret);
        requireNonNull(serialNumber);
        requireNonNull(clientRequestSigner);
        requireNonNull(httpMethod);
        requireNonNull(uri);
        requireNonNull(serverPrivacyEncryptor);

        String clientSecretEncrypted = serverPrivacyEncryptor.encrypt(clientSecret);

        long timestamp = ZonedDateTime.now().toInstant().getEpochSecond();
        String nonce = NonceUtils.createNonce(SignConsts.SIGN_NONCE_LENGTH);
        String signature = signClientRequest(clientRequestSigner, httpMethod, uri, requestBody, timestamp, nonce);
        // EMS-SHA256-RSA2048 clientId="eastsoft",clientSecret="eastsoft.cn",nonce="a7XQwMoI12kZGDqfbNLByGtVnC24SN4w",timestamp="1697292484",signature="DfV4+wJ8R6FRdLBTLEKn11yyQnK
        return SignConsts.SIGN_ALGORITHM_PREFIX + clientRequestSigner.getAlgorithm() + " " +
                "clientId=\"" + clientId + "\"," +
                "clientSecret=\"" + clientSecretEncrypted + "\"," +
                "serialNumber=\"" + serialNumber + "\"," +
                "nonce=\"" + nonce + "\"," +
                "timestamp=\"" + timestamp + "\"," +
                "signature=\"" + signature + "\"";
    }

    /**
     * 生成客户端请求签名
     *
     * @param clientRequestSigner 客户端请求签名器
     * @param httpMethod          请求方法
     * @param uri                 请求URI
     * @param requestBody         请求体
     * @param timestamp           时间戳
     * @param nonce               随机字符串
     * @return 客户端请求签名
     */
    public static String signClientRequest(Signer clientRequestSigner, HttpMethod httpMethod, URI uri, String requestBody, Long timestamp, String nonce) {
        requireNonNull(clientRequestSigner);

        String message = buildClientRequestSignMessage(httpMethod, uri, requestBody, timestamp, nonce);
        if (log.isDebugEnabled()) {
            log.debug("客户端请求签名: message=\n{}", message);
        } else {
            System.out.println("-->客户端请求签名: message=\n" + message);
        }
        return clientRequestSigner.sign(message);
    }

    /**
     * 生成服务端响应签名
     *
     * @param serverResponseSigner 服务端响应签名器
     * @param responseBody         请求体
     * @param timestamp            时间戳
     * @param nonce                随机字符串
     * @return 服务端请求签名
     */
    public static String signServerResponse(Signer serverResponseSigner, String responseBody, Long timestamp, String nonce) {
        requireNonNull(serverResponseSigner);

        String message = buildServerResponseSignMessage(responseBody, timestamp, nonce);
        if (log.isDebugEnabled()) {
            log.debug("服务端响应签名: message=\n{}", message);
        } else {
            System.out.println("-->服务端响应签名: message=\n" + message);
        }
        return serverResponseSigner.sign(message);
    }

    /**
     * 构建客户端请求签名信息
     *
     * @param httpMethod  请求方法
     * @param uri         请求URI
     * @param requestBody 请求体
     * @param timestamp   时间戳
     * @param nonce       随机字符串
     * @return 客户端请求签名信息
     */
    public static String buildClientRequestSignMessage(HttpMethod httpMethod, URI uri, String requestBody, Long timestamp, String nonce) {
        requireNonNull(uri);
        requireNonNull(nonce);
        requireNonNull(timestamp);

        String canonicalUrl = uri.getRawPath();
        if (uri.getQuery() != null) {
            canonicalUrl += "?" + uri.getRawQuery();
        }
        return httpMethod +
                "\n" +
                canonicalUrl +
                "\n" +
                timestamp +
                "\n" +
                nonce +
                "\n" +
                (requestBody == null ? "" : requestBody) +
                "\n";
    }

    /**
     * 构建服务端响应签名信息
     *
     * @param responseBody 响应体内容
     * @param timestamp    时间戳
     * @param nonce        随机字符串
     * @return 服务端响应签名信息
     */
    public static String buildServerResponseSignMessage(String responseBody, Long timestamp, String nonce) {
        requireNonNull(timestamp);
        requireNonNull(nonce);

        return timestamp +
                "\n" +
                nonce +
                "\n" +
                (responseBody == null ? "" : responseBody) +
                "\n";
    }

}
