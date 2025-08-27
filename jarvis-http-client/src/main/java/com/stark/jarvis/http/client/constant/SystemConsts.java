package com.stark.jarvis.http.client.constant;

import com.stark.jarvis.http.sign.constant.SignConsts;

/**
 * 系统常量
 *
 * @author <a href="mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2025/8/14
 */
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

}
