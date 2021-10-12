package com.inspur.cloud.common.exception.handler;

import com.inspur.cloud.common.bean.ResponseBean;
import com.inspur.cloud.common.exception.UnauthorizedException;
import com.inspur.cloud.common.exception.model.ParameterValidationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.*;

/**
 * 处理注解验证异常
 */
@Slf4j
@RestControllerAdvice
public class ClientExceptionHandler extends BaseExceptionHandler {
    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseBean<List<ParameterValidationMessage>> handleBindException(Exception exception) {
        ResponseBean<List<ParameterValidationMessage>> errorResult = new ResponseBean<>();
        errorResult.setCode(HttpStatus.BAD_REQUEST.toString());
        errorResult.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());

        List<ParameterValidationMessage> validationMessages = new ArrayList<>();
        for (FieldError fieldError : parseFieldError(exception)) {
            validationMessages.add(new ParameterValidationMessage(fieldError));
        }
        errorResult.setResult(validationMessages);
        return errorResult;
    }

    private List<FieldError> parseFieldError(Exception exception) {
        List<FieldError> fieldErrors;
        if (exception instanceof BindException) {
            fieldErrors = ((BindException) exception).getFieldErrors();
        } else {
            fieldErrors = ((MethodArgumentNotValidException) exception).getBindingResult().getFieldErrors();
        }
        return fieldErrors;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResponseBean<?>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return wrapper(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ResponseBean<?>> noHandlerFoundException(NoHandlerFoundException e) {
        return wrapper(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ResponseBean<?>> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        return wrapper(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseBean<ParameterValidationMessage> missingServletRequestParameterException(MissingServletRequestParameterException e) {
        ResponseBean<ParameterValidationMessage> errorResult = new ResponseBean<>();
        errorResult.setCode(HttpStatus.BAD_REQUEST.toString());
        errorResult.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        ParameterValidationMessage parameterValidationMessage = new ParameterValidationMessage(e.getParameterName(), e.getMessage());
        errorResult.setResult(parameterValidationMessage);
        return errorResult;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseBean<ParameterValidationMessage> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String description = e.getRequiredType() == null ? "" : e.getRequiredType().getName();
        ParameterValidationMessage validationMessage = new ParameterValidationMessage(e.getName(), description);
        validationMessage.setValue(e.getValue());
        validationMessage.setCode(HttpStatus.BAD_REQUEST.toString());

        ResponseBean<ParameterValidationMessage> response = getDefaultValidationMessage();
        response.setResult(validationMessage);
        return response;
    }

    private ResponseBean<ParameterValidationMessage> getDefaultValidationMessage() {
        ResponseBean<ParameterValidationMessage> response = new ResponseBean<>();
        response.setCode(HttpStatus.BAD_REQUEST.toString());
        response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        return response;
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseBean<?> unauthorizedException(UnauthorizedException e){
        ResponseBean<?> errorResult = new ResponseBean<>();
        errorResult.setCode(e.getCode());
        errorResult.setMessage(e.getMessage());
        return errorResult;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseBean<List<ParameterValidationMessage>> constraintViolationException(ConstraintViolationException e) {
        ResponseBean<List<ParameterValidationMessage>> response = new ResponseBean<>();
        response.setCode(HttpStatus.BAD_REQUEST.toString());
        response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());

        List<ParameterValidationMessage> validationMessages = new ArrayList<>();
        List<String> messageList = parseConstraintViolationMessage(e.getMessage());
        Set<ConstraintViolation<?>> constraintSet = e.getConstraintViolations();
        if (messageList.size() != constraintSet.size()) {
            return response;
        }

        Iterator<String> messageIterator = messageList.iterator();
        for (ConstraintViolation<?> constraint : constraintSet) {
            ParameterValidationMessage parameterValidationMessage = new ParameterValidationMessage(
                    parseConstraintViolationParameter(constraint.getPropertyPath()), messageIterator.next());
            validationMessages.add(parameterValidationMessage);
        }

        response.setResult(validationMessages);
        return response;
    }

    private String parseConstraintViolationParameter(Path path) {
        String parameter = Objects.toString(path);
        if (parameter != null && parameter.contains(".")) {
            return parameter.substring(parameter.lastIndexOf(".") + 1);
        }
        return parameter;
    }

    private List<String> parseConstraintViolationMessage(String violationMessage) {
        List<String> messageList = new ArrayList<>();
        if (violationMessage != null) {
            String[] rawMessages = violationMessage.split(",");
            for (String rawMessage : rawMessages) {
                if (rawMessage.contains(":")) {
                    messageList.add(rawMessage.substring(rawMessage.lastIndexOf(":") + 1).trim());
                }
                else {
                    messageList.add(rawMessage);
                }
            }
        }
        return messageList;
    }
}
