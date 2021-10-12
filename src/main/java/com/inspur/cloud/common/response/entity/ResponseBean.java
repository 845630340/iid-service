package com.inspur.cloud.common.response.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.inspur.cloud.common.response.constant.ApiConstants;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ResponseBean<T> {
    /**
     * 返回码 0成功，其他失败
     */
    private String code;

    /**
     * 描述信息
     */
    private String message;

    private String requestId;
    /**
     * 返回结果
     */
    @JsonAlias("result")
    @JsonProperty("data")
    private T result;

    @Override
    public String toString() {
        return "ResponseBean{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
    }

    public ResponseBean(String code, String message, T result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }

    public ResponseBean(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseBean() {
        // why this is not allowed.
        //this(ApiConstants.DEFAULT_RESULT);
    }
    public ResponseBean(T result) {
        this.setResult(result);
        this.code = ApiConstants.SUCCESS_CODE;
        this.message = ApiConstants.DEFAULT_SUCCESS_MESSAGE;
    }
    public static class ResponseBeanBuilder {
        private ResponseBean<Object> responseBean = new ResponseBean<>();

        public ResponseBeanBuilder code(String code) {
            responseBean.setCode(code);
            return this;
        }

        public ResponseBeanBuilder message(String message) {
            responseBean.setMessage(message);
            return this;
        }

        public ResponseBeanBuilder result(Object result) {
            responseBean.setResult(result);
            return this;
        }

        public ResponseBeanBuilder requestId(String requestId) {
            responseBean.setRequestId(requestId);
            return this;
        }

        public ResponseBean build() {
            return responseBean;
        }
    }

    public static ResponseBeanBuilder builder() {
        return new ResponseBeanBuilder();
    }
}
