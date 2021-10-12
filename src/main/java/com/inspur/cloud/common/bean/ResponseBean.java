package com.inspur.cloud.common.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author mysterious guest
 */
@Getter
@Setter
@ToString
public class ResponseBean<T> {
    public static final String SUCCESS_CODE = "200";

    public static final String DEFAULT_SUCCESS_MESSAGE = "操作成功！";

    public static final String DEFAULT_RESULT = "";
    /**
     * 返回码 0成功，其他失败
     */
    private String code;

    /**
     * 描述信息
     */
    private String message;

    /**
     * 返回结果
     */
    private T result;

    public ResponseBean(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.result = data;
    }

    public ResponseBean(String code, String message) {
        this.code = code;
        this.message = message;
    }
    public ResponseBean() {
        // why this is not allowed.
        //this(ApiConstants.DEFAULT_RESULT);
    }
    public ResponseBean(T data) {
        this.setResult(data);
        this.code = SUCCESS_CODE;
        this.message = DEFAULT_SUCCESS_MESSAGE;
    }
}
