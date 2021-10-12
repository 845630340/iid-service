package com.inspur.cloud.common.response;

import com.inspur.cloud.common.response.annotation.ResponseResult;
import com.inspur.cloud.common.response.entity.ResponseBean;
import com.inspur.cloud.common.response.util.ReturnMessageUtil;
import com.inspur.cloud.common.utils.JsonUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author LisonCheung
 * @version 1.0
 * @date 2020/5/20
 * @since 2020/5/20
 */
@RestControllerAdvice
@Component
public class ResponseResultBodyAdvisor implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.hasMethodAnnotation(ResponseResult.class) || returnType.getDeclaringClass().isAnnotationPresent(ResponseResult.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof ResponseBean) {
            return body;
        }
        if (selectedConverterType.getTypeName().equals(StringHttpMessageConverter.class.getTypeName())) {
            ResponseBean<String> success = ReturnMessageUtil.success((String) body);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
            return JsonUtils.toJson(success);
        }
        return ReturnMessageUtil.success(body);
    }
}
