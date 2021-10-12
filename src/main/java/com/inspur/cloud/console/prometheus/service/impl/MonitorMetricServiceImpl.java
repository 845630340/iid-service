package com.inspur.cloud.console.prometheus.service.impl;

import com.inspur.cloud.console.prometheus.service.MonitorMetricService;
import com.inspur.cloud.console.prometheus.utils.DimensionMap;
import com.inspur.cloud.console.prometheus.utils.TagImpl;
import io.micrometer.core.instrument.Tag;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
@Service
@Slf4j
public class MonitorMetricServiceImpl implements MonitorMetricService {

    private static final Map<String, Double> STRONG_REF_GAUGE = new ConcurrentHashMap<>();
    @Override
    public String getMetrics() {
        STRONG_REF_GAUGE.clear();
        PrometheusMeterRegistry prometheusRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        // 声明dimensionMap
        Map<String, String> dimensionMap;
        double statusA;
        String valueA;
        List<Tag> dimensionsA = new ArrayList<>();
       //
        statusA = 0;
        valueA = "0";
        //
        List<Tag> serviceStatus = dimensionsService(valueA,"cn-north-3");
        STRONG_REF_GAUGE.put("service_status", statusA);
        prometheusRegistry.gauge("service_status", serviceStatus, STRONG_REF_GAUGE, g -> g.get("service_status"));
        return prometheusRegistry.scrape();
    }

    private List<Tag> dimensionsService (String value, String region){
        Map<String, String> dimensionMap;
        dimensionMap = DimensionMap.setDimensions("iid_service", "iid_service", "iid_service", region, "iid_service", value);
        List<Tag> dimensions = new LinkedList<>();
        // 填入dimension
        for (Map.Entry<String, String> entry : dimensionMap.entrySet()) {
            Tag tag = new TagImpl(entry.getKey(), entry.getValue());
            dimensions.add(tag);
        }
        return dimensions;
    }
    private List<Tag> dimensions (String value, String region){
        Map<String, String> dimensionMap;
        dimensionMap = DimensionMap.setDimensions("iid_service", "iid_service", "admin", region, "iid_service", value);
        List<Tag> dimensions = new LinkedList<>();
        // 填入dimension
        for (Map.Entry<String, String> entry : dimensionMap.entrySet()) {
            Tag tag = new TagImpl(entry.getKey(), entry.getValue());
            dimensions.add(tag);
        }
        return dimensions;
    }

    private List<Tag> dimensions (String value, String region, String valueName){
        Map<String, String> dimensionMap;
        dimensionMap = DimensionMap.setDimensions("iid_service", "iid_service", "admin", region, "iid_service", valueName, value);
        List<Tag> dimensions = new LinkedList<>();
        // 填入dimension
        for (Map.Entry<String, String> entry : dimensionMap.entrySet()) {
            Tag tag = new TagImpl(entry.getKey(), entry.getValue());
            dimensions.add(tag);
        }
        return dimensions;
    }

    private List<Tag> dimensions (String region){
        Map<String, String> dimensionMap;
        dimensionMap = DimensionMap.setDimensions("iid_service", "iid_service", "admin", region, "iid_service");
        List<Tag> dimensions = new LinkedList<>();
        // 填入dimension
        for (Map.Entry<String, String> entry : dimensionMap.entrySet()) {
            Tag tag = new TagImpl(entry.getKey(), entry.getValue());
            dimensions.add(tag);
        }
        return dimensions;
    }


}

