package com.inspur.cloud.console.rootcert.entity;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImportRootCertParams extends CreateRootCertParams {
    private MultipartFile crtFile;

    private MultipartFile keyFile;
}
