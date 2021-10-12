package com.inspur.cloud.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @author mysterious guest
 */
@WebFilter(urlPatterns = "/*")
@Slf4j
public class RequestIdUtil
{

    public static final String REQUEST_ID_KEY = "requestId";

    public static final ThreadLocal<String> REQUEST_ID_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * get request id
     *
     * @param request request
     */
    public static String getRequestId(HttpServletRequest request)
    {
        String requestId;
        String headerRequestId = request.getHeader(REQUEST_ID_KEY);
        if (StringUtils.isEmpty(headerRequestId))
        {
            requestId = UUID.randomUUID().toString();
            log.info(
                    "request header has no requestId,we generate a new request id:{}",
                    requestId);
        }
        else
        {
            requestId = headerRequestId;
            log.info("request header has requestId:{}", requestId);
        }
        REQUEST_ID_THREAD_LOCAL.set(requestId);
        log.info("requestId:{}", requestId);
        return requestId;
    }



}

