package com.inspur.cloud.console.rootcert.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author jiangll01
 * @Date: 2020/10/27 14:36
 * @Description:
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RootCert {
    private String id;

    /**
     * 用户名
     */
    private String name;

    /**
     * 统一社会信用代码（1.0.0版本赋值accountId）
     */
    private String code;

    /**
     * 主账号Id
     */
    private String accountId;

    /**
     * 子账号Id
     */
    private String creatorId;

    /**
     * 主加密公钥/根证书
     */
    private String mainPubEncKey;

    /**
     * 主加密私钥/根私钥
     */
    private String mainPriEncKey;

    /**
     * 主签名公钥
     */
    private String mainPubVfyKey;

    /**
     * 主签名私钥
     */
    private String mainPriVfyKey;

    /**
     * 证书类型：SM9 X509
     */
    private String type;

    /**
     * 证书名称（用户输入）
     */
    private String certName;

    /**
     * rsa位数（用户输入）
     */
    private String rsaCount;

    /**
     * 证书密码（用户输入）
     */
    private String password;

    /**
     * 证书有效期（用户输入）
     */
    private String days;

    /**
     * 证书状态：0禁用 1开启
     */
    private Integer status;

    /**
     * 证书来源
     */
    private String source;

    /**
     * dns数组（用户输入）
     */
    private String dnsList;

    /**
     * ip数组（用户输入）
     */
    private String ipList;

    /**
     * openssl命令参数（固定）
     */
    private String subject;

    /**
     * 创建时间
     */
    @JsonFormat
    private Date createdTime;

    /**
     * 更新时间
     */
    @JsonFormat
    private Date updatedTime;

}
