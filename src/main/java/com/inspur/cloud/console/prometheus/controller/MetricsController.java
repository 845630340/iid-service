package com.inspur.cloud.console.prometheus.controller;

import com.inspur.cloud.console.prometheus.service.MonitorMetricService;
import com.inspur.iam.adapter.annotation.PermissionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author mysterious guest
 */

@Controller
public class MetricsController {
    @Autowired
    MonitorMetricService monitorMetricService;

    @PermissionContext(whitelist = true)
    @RequestMapping("/metrics")
    public void metrics(HttpServletResponse response) throws IOException {
        response.getWriter().write(monitorMetricService.getMetrics());

    }
}