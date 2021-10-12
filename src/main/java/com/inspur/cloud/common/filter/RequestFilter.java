package com.inspur.cloud.common.filter;

import com.inspur.cloud.common.http.RequestConstants;
import com.inspur.cloud.common.http.RequestHolder;
import com.inspur.iam.adapter.entity.Account;
import com.inspur.iam.adapter.entity.User;
import com.inspur.iam.adapter.util.SecurityContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

/**
 * 对于请求头requestId/token的处理
 * [请求与应答的Header设置相同的requestId]
 */
@Slf4j
@Component
@WebFilter(urlPatterns = "/*")
public class RequestFilter implements Filter {
    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.debug("RequestFilter.init()......");
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        log.debug("RequestFilter.doFilter()...");

        out:
        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse resp = (HttpServletResponse) response;

            String requestId = req.getHeader(RequestConstants.REQUEST_ID_KEY);
            if (StringUtils.isEmpty(requestId)) {
                requestId = UUID.randomUUID().toString();
            }
            // 将requestId放到请求Header中
            resp.setHeader(RequestConstants.REQUEST_ID_KEY, requestId);

            // 将requestId和userId值塞到attribute中，方便后续取值（也可以不用塞值，后面直接通过request的header取值）
            req.setAttribute(RequestConstants.REQUEST_ID_KEY, requestId);

            // 将requestId值加到日志打印中
            MDC.put(RequestConstants.REQUEST_ID_KEY, requestId);

            // 将应用名称加到日志打印中
            MDC.put(RequestConstants.APPLICATION_NAME, applicationName);
            User user = SecurityContextUtil.getLoginUser();
            if (Objects.isNull(user)) {
                //如果无验证,直接跳出
                break out;
            }
            log.info("是否为主账号{}", user.getIsRootUser());
            Account account = user.getAccount();
            String userId = account.getId();  // 这里获取到的是主账号ID
            req.setAttribute("userId", userId);
            // Bind user identifier to thread. We do not recommend you to bind an object to threadLocal instance out of
            // the class. Otherwise you can not control the lifecycle of the object. To do that you must make some effort
            // in all class this object bound to, which is cumbersome.
            //清空本地线程信息
            RequestHolder.remove();
            //主账号ID
            RequestHolder.setUserName(user.getName());
            RequestHolder.setUserId(userId);
            RequestHolder.setPhone(user.getPhone());
            //登陆用户ID
            RequestHolder.setCreatorId(user.getId());
            log.info("user id: {},Creator Id: {}, request url: {}, request id: {}", userId, user.getId(), req.getRequestURI(), requestId);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        log.debug("RequestFilter.destroy()...");
    }

}
