package com.inspur.cloud.common.exception.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @author mysterious guest
 */
@Getter
@Setter
@ToString
public class ErrorCodeEntity implements Serializable {
    private static final long serialVersionUID = -6330110029448471080L;
    /**
     * 记录id
     */
    private Long id;

    /**
     * 项目code码
     */
    private String projectCode;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 模块码
     */
    private String moduleCode;

    /**
     * 模块名称
     */
    private String moduleName;

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 创建时间
     */
    private Date createdTime;

}
