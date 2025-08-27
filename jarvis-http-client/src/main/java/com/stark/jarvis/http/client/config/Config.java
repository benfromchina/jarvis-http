package com.stark.jarvis.http.client.config;

import com.stark.jarvis.cipher.core.privacy.PrivacyDecryptor;
import com.stark.jarvis.cipher.core.privacy.PrivacyEncryptor;
import com.stark.jarvis.cipher.core.signer.Signer;
import com.stark.jarvis.http.client.auth.ClientCredential;
import com.stark.jarvis.http.client.auth.ServerResponseValidator;

/**
 * 配置
 *
 * @author <a href="mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2025/8/14
 */
public interface Config {

    /**
     * 获取公钥加密器
     *
     * @return 公钥加密器
     */
    PrivacyEncryptor getEncryptor();

    /**
     * 获取私钥解密器
     *
     * @return 敏感私钥解密器
     */
    PrivacyDecryptor getDecryptor();

    /**
     * 获取客户端凭证
     *
     * @return 客户端凭证
     */
    ClientCredential getClientCredential();

    /**
     * 获取客户端请求签名器
     *
     * @return 客户端请求签名器
     */
    Signer getClientRequestSigner();

    /**
     * 获取服务端响应验签器
     *
     * @return 服务端响应验签器
     */
    ServerResponseValidator getServerResponseValidator();

}
