package com.inspur.cloud.console.kgc.entity;

import lombok.Data;

/**
 * @Author: JiangYP
 * @Description:
 */

@Data
public class PrimarySecret {
    private String encryptPrimaryPublicKey;

    private String encryptPrimaryPrivateKey;

    private String signPrimaryPublicKey;

    private String signPrimaryPrivateKey;
}
