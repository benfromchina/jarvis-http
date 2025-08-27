package com.stark.jarvis.http.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 文件元信息
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2025/8/22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class FileMeta implements Serializable {

    private static final long serialVersionUID = -1921541031417149110L;

    /**
     * 文件名
     */
    private String filename;

    /**
     * 文件 SHA256 签名
     */
    private String sha256;

}
