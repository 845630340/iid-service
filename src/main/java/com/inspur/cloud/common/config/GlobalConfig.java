package com.inspur.cloud.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;


/**
 * @author mysterious guest
 */
@Configuration
public class GlobalConfig {

    @Autowired
    private RestTemplateBuilder builder;

    @Bean
    public RestTemplate restTemplate()
    {
        return builder.build();
    }

    @Bean("serviceRequest")
    public RestTemplate restTemplateServiceRequest(){
        return builder.errorHandler(new ResponseErrorHandler() {
                    @Override
                    public boolean hasError(@NonNull ClientHttpResponse response) {
                        return false;
                    }

                    @Override
                    public void handleError(@NonNull ClientHttpResponse response) {

                    }
                })
                .build();
    }

}
