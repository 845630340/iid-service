package com.inspur.cloud.common.exception.handler;

import com.inspur.cloud.common.bean.ErrorResponseBean;
import com.inspur.cloud.common.exception.model.ErrorCodeEntity;
import com.inspur.cloud.common.exception.model.ServiceException;
import com.inspur.cloud.common.exception.service.ErrorCodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

/**
 * @author mysterious guest
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends BaseExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final HttpStatus defaultHttpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    private final ErrorCodeService errorCodeService;

    @Autowired
    public GlobalExceptionHandler(ErrorCodeService errorCodeService) {
        this.errorCodeService = errorCodeService;
    }

    /**
     * The standard error code is in the form of  'xxx.xxxxxx',
     * such as '201.002001'.
     *
     * @param exception business exception
     * @return response of exception
     */
    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponseBean> handleServiceException(Exception exception) {
        if (exception instanceof ServiceException) {
            ServiceException serviceException = (ServiceException) exception;
            if (StringUtils.isEmpty(serviceException.getMessage())) {
                String code = serviceException.getCode();
                String projectKey = code.substring(0, 3);
                String moduleKey = code.substring(4, 7);
                String errorKey = code.substring(7, 10);

                // if the error code does not comply with the specification mentioned above, or
                // an database exception occurred when translating the error code, there will
                // also throws an exception.
                String defaultMessage = defaultHttpStatus.getReasonPhrase();
                serviceException.setMessage(defaultMessage);
                try {
                    ErrorCodeEntity errorCodeEntity = errorCodeService.getErrorCodeEntity(projectKey, moduleKey, errorKey);
                    if (Objects.isNull(errorCodeEntity)) {
                        return errorWrapper(serviceException);
                    }
                    if (Objects.isNull(serviceException.getParams())) {
                        serviceException.setMessage(errorCodeEntity.getErrorMessage());
                    } else {
                        serviceException.setMessage(String.format(errorCodeEntity.getErrorMessage(), serviceException.getParams()));
                    }
                } catch (Exception e) {
                    LOGGER.error("fail in parsing error code, cause {}", getStackTrace(e));
                }
            }
            return errorWrapper(serviceException);
        }
        return errorWrapper();

    }
}
