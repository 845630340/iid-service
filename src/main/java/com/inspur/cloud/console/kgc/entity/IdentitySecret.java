package com.inspur.cloud.console.kgc.entity;

import lombok.Data;

@Data
public class IdentitySecret {
    /*
    加解密场景标识密钥
     */
    private String encryptIdentityPrivateKey;
    /*
     签名场景标识密钥
      */
    private String signIdentityPrivateKey;
}
