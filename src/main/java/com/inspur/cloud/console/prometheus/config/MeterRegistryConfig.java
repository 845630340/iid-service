package com.inspur.cloud.console.prometheus.config;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author mysterious guest
 */

@Configuration
public class MeterRegistryConfig {
    @Bean
    MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        //demo替换为产品简称
        return registry -> registry.config().commonTags("service", "iid_service");
    }


}
