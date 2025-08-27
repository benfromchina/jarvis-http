package com.stark.jarvis.http.sign.model;

import com.stark.jarvis.http.sign.constant.SignConsts;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 认证信息
 *
 * @author <a href="mengbin@eastsoft.com.cn">Ben</a>
 * @version 1.0.0
 * @since 2025/8/14
 */
@Data
@Accessors(chain = true)
public class AuthorizationInfo implements Serializable {

    private static final long serialVersionUID = 3603231668243083625L;

    /**
     * 签名算法
     */
    private String algorithm;

    /**
     * 客户端ID
     */
    private String clientId;

    /**
     * 客户端秘钥
     */
    private String clientSecret;

    /**
     * 客户端证书序列号
     */
    private String serialNumber;

    /**
     * 随机字符串
     */
    private String nonce;

    /**
     * 时间戳
     */
    private Long timestamp;

    /**
     * 签名
     */
    private String signature;

    /**
     * 解析 Authorization 请求头，生成认证信息对象
     *
     * @param authorization Authorization 请求头
     * @return 认证信息对象
     */
    public static AuthorizationInfo of(String authorization) {
        // EMS-SHA256-RSA2048 clientId="eastsoft",clientSecret="eastsoft.cn",serialNumber="A1B2C3D4",nonce="a7XQwMoI12kZGDqfbNLByGtVnC24SN4w",timestamp="1697292484",signature="DfV4+wJ8R6FRdLBTLEKn11yyQnK"
        String[] algorithm_authorization = authorization.split(" ");
        String algorithm = algorithm_authorization[0].substring(SignConsts.SIGN_ALGORITHM_PREFIX.length());
        Map<String, String> elementMap = Stream.of(algorithm_authorization[1].split(","))
                .collect(Collectors.toMap(keyValue -> StringUtils.substringBefore(keyValue, "="), keyValue -> StringUtils.substringAfter(keyValue, "=").replace("\"", "")));
        String clientId = elementMap.get("clientId");
        String clientSecret = elementMap.get("clientSecret");
        String serialNumber = elementMap.get("serialNumber");
        String nonce = elementMap.get("nonce");
        String timestamp = elementMap.get("timestamp");
        String signature = elementMap.get("signature");

        return new AuthorizationInfo()
                .setAlgorithm(algorithm)
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setSerialNumber(serialNumber)
                .setNonce(nonce)
                .setTimestamp(Long.parseLong(timestamp))
                .setSignature(signature);
    }

}
