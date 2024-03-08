package com.cpy.model.dto.interfaceInfo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import java.io.Serializable;

/**
 * 前端请求数据封装类
 */
@Data
public class InterfaceInformationUploadSDKFile implements Serializable {
    /**
     *
     */

    private Long id;


    private MultipartFile file;

    private static final long serialVersionUID = 1L;

}
