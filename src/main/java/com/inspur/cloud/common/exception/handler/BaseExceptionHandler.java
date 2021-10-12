package com.inspur.cloud.common.exception.handler;

import com.inspur.cloud.common.bean.ErrorResponseBean;
import com.inspur.cloud.common.bean.ResponseBean;
import com.inspur.cloud.common.exception.model.ServiceException;
import com.inspur.common.utils.ConnectorUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author mysterious guest
 */
public class BaseExceptionHandler
{
    private final ResponseBean<?> exceptionEntity = new ResponseBean<>();

    ResponseEntity<ResponseBean<?>> wrapper(ServiceException ex)
    {
        exceptionEntity.setCode(ex.getCode());
        exceptionEntity.setMessage(ex.getMessage());
        // TODO 前台展示原因改为200状态码
        return new ResponseEntity<>(exceptionEntity, HttpStatus.OK);
    }

    ResponseEntity<ResponseBean<?>> wrapper(HttpStatus httpStatus) {
        exceptionEntity.setMessage(httpStatus.name());
        exceptionEntity.setCode(httpStatus.toString());
        return new ResponseEntity<>(httpStatus);
    }

    ResponseEntity<ErrorResponseBean> errorWrapper(ServiceException ex)
    {
        // TODO 前台展示原因改为200状态码
        return new ResponseEntity<>(getErrorExceptionEntity(ex.getMessage(),ex.getCode()), HttpStatus.OK);
    }

    ResponseEntity<ErrorResponseBean> errorWrapper() {
        return new ResponseEntity<>(getErrorExceptionEntity(HttpStatus.INTERNAL_SERVER_ERROR.name(), HttpStatus.INTERNAL_SERVER_ERROR.toString()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ErrorResponseBean getErrorExceptionEntity(String message, String code){
        ErrorResponseBean errorExceptionEntity = new ErrorResponseBean();
        errorExceptionEntity.setMessage(message);
        errorExceptionEntity.setCode(code);
        errorExceptionEntity.setRequestId(ConnectorUtils.getRequestId());
        return errorExceptionEntity;
    }

    /**
     * see org.apache.commons.lang3.exception.ExceptionUtils;
     */
    public static String getStackTrace(Throwable throwable)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }
}
