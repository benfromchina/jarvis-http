package com.stark.jarvis.http.client.crypto.aead;

import com.stark.jarvis.cipher.core.AeadAlgorithm;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * 对称加密数据
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2025/8/26
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class EncryptData {

    /**
     * 加密算法
     */
    @NonNull
    private AeadAlgorithm algorithm;

    /**
     * 随机串
     */
    @NonNull
    private String nonce;

    /**
     * 附加数据
     */
    private String associatedData;

    /**
     * 密文
     */
    @NonNull
    private String ciphertext;

}
