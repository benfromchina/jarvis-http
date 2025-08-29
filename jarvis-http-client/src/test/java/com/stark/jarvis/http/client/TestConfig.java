package com.stark.jarvis.http.client;

import com.stark.jarvis.cipher.core.AeadAlgorithm;
import com.stark.jarvis.cipher.core.AsymmetricAlgorithm;
import com.stark.jarvis.http.client.constant.SystemPropertyConsts;

/**
 * 测试配置
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2025/8/22
 */
public class TestConfig {

    public static final String SIGN_HEADER_PREFIX = "Das-";

    public static final String SIGN_ALGORITHM_PREFIX = "DAS-";

    public static final String CLIENT_ID = "microgrid";

    public static final String CLIENT_SECRET = "@le3mfJn76duA1LD8i3THq7YM9";

    public static final String RESOURCES_DIR = System.getProperty("user.dir") + "/src/test/resources";

    public static final String CLIENT_CERT_PATH = RESOURCES_DIR + "/client_crt.pem";

    public static final String CLIENT_PRIVATE_KEY_PATH = RESOURCES_DIR + "/client_private_key.pem";

    public static final String CLIENT_PUBLIC_KEY_PATH = RESOURCES_DIR + "/client_public_key.pem";

    public static final AeadAlgorithm AEAD_ALGORITHM = AeadAlgorithm.AES;

    public static final String AEAD_KEY = "c8gMjNPcY6PHO574hD8JbjdRqOYIu7Ko";

    public static final AsymmetricAlgorithm ASYMMETRIC_ALGORITHM = AsymmetricAlgorithm.RSA;

    public static void init() {
        System.setProperty(SystemPropertyConsts.SIGN_HEADER_PREFIX, SIGN_HEADER_PREFIX);
        System.setProperty(SystemPropertyConsts.SIGN_ALGORITHM_PREFIX, SIGN_ALGORITHM_PREFIX);

        System.setProperty(SystemPropertyConsts.CLIENT_ID, CLIENT_ID);
        System.setProperty(SystemPropertyConsts.CLIENT_SECRET, CLIENT_SECRET);
        System.setProperty(SystemPropertyConsts.CLIENT_AEAD_ALGORITHM, AEAD_ALGORITHM.name());
        System.setProperty(SystemPropertyConsts.CLIENT_AEAD_KEY, AEAD_KEY);
        System.setProperty(SystemPropertyConsts.CLIENT_ASYMMETRIC_ALGORITHM, ASYMMETRIC_ALGORITHM.name());
        System.setProperty(SystemPropertyConsts.CLIENT_CERT_PATH, CLIENT_CERT_PATH);
        System.setProperty(SystemPropertyConsts.CLIENT_PRIVATE_KEY_PATH, CLIENT_PRIVATE_KEY_PATH);
        System.setProperty(SystemPropertyConsts.SERVER_PUBLIC_KEY_PATH, CLIENT_PUBLIC_KEY_PATH);
    }

}
