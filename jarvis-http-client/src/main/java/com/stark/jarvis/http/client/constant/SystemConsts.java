package com.stark.jarvis.http.client.constant;

import com.stark.jarvis.cipher.core.AeadAlgorithm;
import com.stark.jarvis.cipher.core.AsymmetricAlgorithm;
import com.stark.jarvis.cipher.rsa.RSAPemUtils;
import com.stark.jarvis.cipher.sm.SMPemUtils;
import com.stark.jarvis.http.sign.constant.SignConsts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

import static java.util.Objects.requireNonNull;

/**
 * 系统常量
 *
 * @author <a href="mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2025/8/14
 */
@Slf4j
public final class SystemConsts {

    /**
     * User-Agent头部值格式：Ems-Java/版本 操作系统/版本 Java/版本 Credential/Credential信息 Validator/Validator信息
     * HttpClient信息 示例： Ems-Java/0.0.1 (Linux/3.10.0-957.el7.x86_64) Java/1.8.0_222 Crendetial/MyCrendetial Validator/MyValidator
     */
    public static final String USER_AGENT_FORMAT = SignConsts.SIGN_HEADER_PREFIX + "Java/%s (%s) Java/%s Credential/%s Validator/%s %s";

    /**
     * 操作系统
     */
    public static final String OS = System.getProperty("os.name") + "/" + System.getProperty("os.version");

    /**
     * Java版本
     */
    public static final String JAVA_VERSION = System.getProperty("java.version");

    /**
     * 客户端ID
     */
    public static final String CLIENT_ID;

    /**
     * 客户端秘钥
     */
    public static final String CLIENT_SECRET;

    /**
     * 客户端对称加密算法
     */
    public static final AeadAlgorithm CLIENT_AEAD_ALGORITHM;

    /**
     * 客户端对称加密密钥
     */
    public static final String CLIENT_AEAD_KEY;

    /**
     * 客户端非对称加密算法
     */
    public static final AsymmetricAlgorithm CLIENT_ASYMMETRIC_ALGORITHM;

    /**
     * 客户端证书路径
     */
    public static final X509Certificate CLIENT_CERT;

    /**
     * 客户端私钥路径
     */
    public static final PrivateKey CLIENT_PRIVATE_KEY;

    /**
     * 服务端公钥路径
     */
    public static final PublicKey SERVER_PUBLIC_KEY;

    /**
     * 签名请求头前缀
     */
    public static final String SIGN_HEADER_PREFIX;

    /**
     * 签名算法前缀
     */
    public static final String SIGN_ALGORITHM_PREFIX;

    /**
     * 签名随机字符串长度
     */
    public static final int SIGN_NONCE_LENGTH;

    /**
     * 签名过期分钟数
     */
    public static final int SIGN_EXPIRED_MINUTES;

    static {
        String logmsg = "-->加载环境变量参数：";

        CLIENT_ID = System.getProperty(SystemPropertyConsts.CLIENT_ID);
        assertNotBlank("客户端ID", CLIENT_ID, SystemPropertyConsts.CLIENT_ID);
        logmsg += "\n   客户端ID: " + CLIENT_ID;

        CLIENT_SECRET = System.getProperty(SystemPropertyConsts.CLIENT_SECRET);
        assertNotBlank("客户端秘钥", CLIENT_SECRET, SystemPropertyConsts.CLIENT_SECRET);
        logmsg += "\n   客户端秘钥: " + desensitize(CLIENT_SECRET) + "（sha256: " + DigestUtils.sha256Hex(CLIENT_SECRET) + "）";

        String clientAeadAlgorithm = System.getProperty(SystemPropertyConsts.CLIENT_AEAD_ALGORITHM);
        assertNotBlank("客户端对称加密算法", clientAeadAlgorithm, SystemPropertyConsts.CLIENT_AEAD_ALGORITHM);
        CLIENT_AEAD_ALGORITHM = AeadAlgorithm.valueOf(clientAeadAlgorithm);
        logmsg += "\n   客户端对称加密算法: " + clientAeadAlgorithm;

        CLIENT_AEAD_KEY = System.getProperty(SystemPropertyConsts.CLIENT_AEAD_KEY);
        assertNotBlank("客户端对称加密密钥", CLIENT_AEAD_KEY, SystemPropertyConsts.CLIENT_AEAD_KEY);
        logmsg += "\n   客户端对称加密密钥: " + desensitize(CLIENT_AEAD_KEY) + "（sha256: " + DigestUtils.sha256Hex(CLIENT_AEAD_KEY) + "）";

        String clientAsymmetricAlgorithm = System.getProperty(SystemPropertyConsts.CLIENT_ASYMMETRIC_ALGORITHM);
        assertNotBlank("客户端非对称加密算法", clientAsymmetricAlgorithm, SystemPropertyConsts.CLIENT_ASYMMETRIC_ALGORITHM);
        CLIENT_ASYMMETRIC_ALGORITHM = AsymmetricAlgorithm.valueOf(clientAsymmetricAlgorithm);
        logmsg += "\n   客户端非对称加密算法: " + clientAsymmetricAlgorithm;

        String clientCertPath = System.getProperty(SystemPropertyConsts.CLIENT_CERT_PATH);
        assertNotBlank("客户端证书路径", clientCertPath, SystemPropertyConsts.CLIENT_CERT_PATH);
        String clientCertPem = readFileToString(clientCertPath);
        CLIENT_CERT = loadX509Certificate(clientCertPem);
        logmsg += "\n   客户端证书路径: " + clientCertPath;

        String clientPrivateKeyPath = System.getProperty(SystemPropertyConsts.CLIENT_PRIVATE_KEY_PATH);
        assertNotBlank("客户端私钥路径", clientPrivateKeyPath, SystemPropertyConsts.CLIENT_PRIVATE_KEY_PATH);
        String clientPrivateKeyPem = readFileToString(clientPrivateKeyPath);
        CLIENT_PRIVATE_KEY = loadPrivateKey(clientPrivateKeyPem);
        logmsg += "\n   客户端私钥路径: " + clientPrivateKeyPath;

        String serverPublicKeyPath = System.getProperty(SystemPropertyConsts.SERVER_PUBLIC_KEY_PATH);
        assertNotBlank("服务端公钥路径", serverPublicKeyPath, SystemPropertyConsts.SERVER_PUBLIC_KEY_PATH);
        String serverPublicKeyPem = readFileToString(serverPublicKeyPath);
        SERVER_PUBLIC_KEY = loadPublicKey(serverPublicKeyPem);
        logmsg += "\n   服务端公钥路径: " + serverPublicKeyPath;

        SIGN_HEADER_PREFIX = SignConsts.SIGN_HEADER_PREFIX;
        logmsg += "\n   签名请求头前缀: " + SIGN_HEADER_PREFIX;

        SIGN_ALGORITHM_PREFIX = SignConsts.SIGN_ALGORITHM_PREFIX;
        logmsg += "\n   签名算法前缀: " + SIGN_ALGORITHM_PREFIX;

        SIGN_NONCE_LENGTH = SignConsts.SIGN_NONCE_LENGTH;
        logmsg += "\n   签名随机字符串长度: " + SIGN_NONCE_LENGTH;

        SIGN_EXPIRED_MINUTES = SignConsts.SIGN_EXPIRED_MINUTES;
        logmsg += "\n   签名过期分钟数: " + SIGN_EXPIRED_MINUTES;

        log.warn("\n{}\n", logmsg);
    }

    private static void assertNotBlank(String property, String value, String key) {
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("未配置环境变量【" + property + "】: key=" + key);
        }
    }

    private static String readFileToString(String path) {
        try {
            String content;
            if (path.startsWith("classpath:")) {
                path = StringUtils.substringAfter(path, "classpath:");
                InputStream in = SystemConsts.class.getClassLoader().getResourceAsStream(path);
                requireNonNull(in);
                content = IOUtils.toString(in, StandardCharsets.UTF_8);
            } else {
                content = FileUtils.readFileToString(new File(path), StandardCharsets.UTF_8);
            }
            return content;
        } catch (Exception e) {
            log.error("读取文件失败: path={}", path, e);
            throw new RuntimeException("读取文件失败: path=" + path, e);
        }
    }

    private static X509Certificate loadX509Certificate(String x509CertificatePem) {
        X509Certificate cert;
        if (AsymmetricAlgorithm.SM2.equals(CLIENT_ASYMMETRIC_ALGORITHM)) {
            cert = SMPemUtils.loadX509FromString(x509CertificatePem);
        } else {
            cert = RSAPemUtils.loadX509FromString(x509CertificatePem);
        }
        return cert;
    }

    private static PublicKey loadPublicKey(String publicKeyPem) {
        PublicKey publicKey;
        if (AsymmetricAlgorithm.SM2.equals(CLIENT_ASYMMETRIC_ALGORITHM)) {
            publicKey = SMPemUtils.loadPublicKeyFromString(publicKeyPem);
        } else {
            publicKey = RSAPemUtils.loadPublicKeyFromString(publicKeyPem);
        }
        return publicKey;
    }

    private static PrivateKey loadPrivateKey(String privateKeyPem) {
        PrivateKey privateKey;
        if (AsymmetricAlgorithm.SM2.equals(CLIENT_ASYMMETRIC_ALGORITHM)) {
            privateKey = SMPemUtils.loadPrivateKeyFromString(privateKeyPem);
        } else {
            privateKey = RSAPemUtils.loadPrivateKeyFromString(privateKeyPem);
        }
        return privateKey;
    }

    private static String desensitize(String str) {
        if (str.length() <= 2) {
            return StringUtils.repeat("*", str.length());
        }

        int len;
        if (str.length() < 6) {
            len = 2;
        } else if (str.length() < 8) {
            len = 3;
        } else {
            len = 4;
        }
        return StringUtils.left(str, len) +
                StringUtils.repeat("*", Integer.max(str.length() - len * 2, 0)) +
                StringUtils.right(str, len);
    }

}
