package com.inspur.cloud.console.usercert.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: JiangYP
 * @Description: 企业用户信息
 */


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCert {
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
     * 主加密公钥
     */
    private String mainPubEncKey;

    /**
     * 主加密私钥
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
     * 创建者
     */
    private String createdBy;

    /**
     * 更改者
     */
    private String modifiedBy;

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

    /**
     * 删除时间
     */
    @JsonFormat
    private Date deletedTime;

    /**
     * 是否被软删除
     */
    private Integer isDeleted;
    /**
     * rsa位数
     */
    private String rsaCount;

    /**
     * dns数组
     */
    private String dnsList;

    /**
     * ip数组
     */
    private String ipList;

    private String password;

    private String days;


}
