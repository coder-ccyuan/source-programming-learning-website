package com.cpy.api_system.model.dto.interfaceInfo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Date;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}
