package com.stark.jarvis.http.client.constant;

/**
 * 系统参数常量
 *
 * @author <a href="mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2025/8/26
 */
public interface SystemPropertyConsts extends com.stark.jarvis.http.sign.constant.SystemPropertyConsts {

    /**
     * 客户端ID
     */
    String CLIENT_ID = "client.client_id";

    /**
     * 客户端秘钥
     */
    String CLIENT_SECRET = "client.client_secret";

    /**
     * 客户端对称加密算法
     */
    String CLIENT_AEAD_ALGORITHM = "client.aead_algorithm";

    /**
     * 客户端对称加密密钥
     */
    String CLIENT_AEAD_KEY = "client.aead_key";

    /**
     * 客户端非对称加密算法
     */
    String CLIENT_ASYMMETRIC_ALGORITHM = "client.asymmetric_algorithm";

    /**
     * 客户端证书路径
     */
    String CLIENT_CERT_PATH = "client.client_cert_path";

    /**
     * 客户端私钥路径
     */
    String CLIENT_PRIVATE_KEY_PATH = "client.private_key.path";

    /**
     * 服务端公钥路径
     */
    String SERVER_PUBLIC_KEY_PATH = "server.public_key.path";

}
