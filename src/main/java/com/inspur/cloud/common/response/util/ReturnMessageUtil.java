package com.inspur.cloud.common.response.util;


import com.inspur.cloud.common.response.constant.ApiConstants;
import com.inspur.cloud.common.response.entity.ResponseBean;

/**
 * 返回消息工具类
 *
 * @author zhangweibin
 */
public class ReturnMessageUtil {

    public static ResponseBean success() {
        return ResponseBean.builder().code(ApiConstants.SUCCESS_CODE).message(ApiConstants.DEFAULT_SUCCESS_MESSAGE).build();
    }

    public static <T> ResponseBean<T> success(T t) {
        return new ResponseBean<>(t);
    }

    public static ResponseBean error(String errorCode,String message){
        return ResponseBean.builder().code(errorCode).message(message).build();
    }

    public static ResponseBean error(String errorCode){
        String message = ";";
        return ResponseBean.builder().code(errorCode).message(message).build();
    }

}
