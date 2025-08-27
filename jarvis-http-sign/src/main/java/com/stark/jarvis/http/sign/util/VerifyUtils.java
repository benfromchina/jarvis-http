package com.stark.jarvis.http.sign.util;

import com.stark.jarvis.cipher.core.verifier.Verifier;
import com.stark.jarvis.http.core.HttpMethod;
import com.stark.jarvis.http.sign.constant.SignConsts;
import com.stark.jarvis.http.sign.exception.TimestampExpiredException;
import com.stark.jarvis.http.sign.exception.TimestampInvalidException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 签名校验工具类
 *
 * @author <a href="mengbin@eastsoft.com.cn">Ben</a>
 * @version 1.0.0
 * @since 2025/8/14
 */
@Slf4j
public class VerifyUtils {

    /**
     * 校验客户端请求签名
     *
     * @param clientRequestVerifier 客户端请求验签器
     * @param httpMethod            请求方法
     * @param uri                   请求URI
     * @param authorization         请求头中的 Authorization 信息
     * @param requestBody           请求体内容
     * @return 签名是否合法
     */
    public static boolean verifyClientRequestSignature(Verifier clientRequestVerifier, HttpMethod httpMethod, URI uri, String authorization, String requestBody) {
        // EMS-SHA256-RSA2048 clientId="eastsoft",clientSecret="eastsoft.cn",nonce="a7XQwMoI12kZGDqfbNLByGtVnC24SN4w",timestamp="1697292484",signature="DfV4+wJ8R6FRdLBTLEKn11yyQnK
        String[] algorithm_authorization = authorization.split(" ");
        Map<String, String> elementMap = Stream.of(algorithm_authorization[1].split(","))
                .collect(Collectors.toMap(keyValue -> StringUtils.substringBefore(keyValue, "="), keyValue -> StringUtils.substringAfter(keyValue, "=").replace("\"", "")));
        String nonce = elementMap.get("nonce");
        String timestamp = elementMap.get("timestamp");
        String signature = elementMap.get("signature");

        String message = SignUtils.buildClientRequestSignMessage(httpMethod, uri, requestBody, Long.parseLong(timestamp), nonce);
        boolean result = clientRequestVerifier.verify(message, signature);
        if (!result) {
            if (log.isDebugEnabled()) {
                log.debug("客户端请求签名校验失败: message=\n{}", message);
            } else {
                System.out.println("-->客户端请求签名校验失败: message=\n" + message);
            }
        }
        return result;
    }

    /**
     * 校验服务端响应签名
     *
     * @param serverResponseVerifier 服务端响应验签器
     * @param responseBody           响应体内容
     * @param timestamp              时间戳
     * @param nonce                  随机字符串
     * @return 签名是否合法
     */
    public static boolean verifyServerResponseSignature(Verifier serverResponseVerifier, String responseBody, Long timestamp, String nonce, String signature) {
        try {
            Instant responseTime = Instant.ofEpochSecond(timestamp);
            // 拒绝过期请求
            if (Duration.between(responseTime, ZonedDateTime.now()).abs().toMinutes() >= SignConsts.SIGN_EXPIRED_MINUTES) {
                throw new TimestampExpiredException(SignConsts.SIGN_EXPIRED_MINUTES);
            }
        } catch (DateTimeException | NumberFormatException e) {
            throw new TimestampInvalidException();
        }

        String message = SignUtils.buildServerResponseSignMessage(responseBody, timestamp, nonce);
        boolean result = serverResponseVerifier.verify(message, signature);
        if (!result) {
            if (log.isDebugEnabled()) {
                log.debug("服务端响应签名校验失败: message=\n{}", message);
            } else {
                System.out.println("-->服务端响应签名校验失败: message=\n" + message);
            }
        }
        return result;
    }

}
