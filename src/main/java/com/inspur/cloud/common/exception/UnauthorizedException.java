package com.inspur.cloud.common.exception;

/**
 * @author hexinyu
 */
public class UnauthorizedException extends BaseException {
    public UnauthorizedException(String code, String message)
    {
        super(code, message);
    }
}
