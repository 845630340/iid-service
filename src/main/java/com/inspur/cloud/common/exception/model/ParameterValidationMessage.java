package com.inspur.cloud.common.exception.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.FieldError;

/**
 * @author mysterious guest
 */
@Getter
@Setter
@ToString
public class ParameterValidationMessage {
    private String description;
    private String parameter;
    private Object value;
    private String code;

    public ParameterValidationMessage(FieldError fieldError) {
        this.description = fieldError.getDefaultMessage();
        this.parameter = fieldError.getField();
        this.code = fieldError.getCode();
        this.value = fieldError.getRejectedValue();
    }

    public ParameterValidationMessage(String parameter, String description) {
        this.parameter = parameter;
        this.description = description;
    }

}