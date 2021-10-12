package com.inspur.cloud.common.exception.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

/**
 * @author mysterious guest
 */
@Getter
@Setter
@ToString
public class ServiceException extends RuntimeException {
    private String code;
    private String message;
    private Object[] params;
    private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    public ServiceException(String code) {
        super();
        this.code = code;
    }

    public ServiceException(String code, String message) {
        this(code);
        this.message = message;
    }

    public ServiceException(String code, Object... params) {
        this(code);
        this.params = params;
    }

    public ServiceException(HttpStatus httpStatus, String code) {
        this(code);
        this.httpStatus = httpStatus;
    }

    public ServiceException(HttpStatus httpStatus, String code, Object... params) {
        this(code, params);
        this.httpStatus = httpStatus;
    }

    /**
     * http码+错误信息
     * @param httpStatus http状态码
     * @param message 错误信息
     */
    public ServiceException(HttpStatus httpStatus,String message,String code)
    {   this.code=code;
        this.message = message;
        this.httpStatus = httpStatus;
    }


}
