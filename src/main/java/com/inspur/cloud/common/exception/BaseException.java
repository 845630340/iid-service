package com.inspur.cloud.common.exception;

/**
 * All self-defined extended exception must inherit BaseException class.
 *
 * @author mysterious guest
 */
public class BaseException extends RuntimeException
{
    protected String code;

    protected String message;

    public BaseException(){
    }

    public BaseException(String code, String message)
    {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
