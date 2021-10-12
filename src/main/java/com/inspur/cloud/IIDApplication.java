package com.inspur.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @Author: JiangYP
 */

@SpringBootApplication
@ComponentScan(value = {"com.inspur.iam", "com.inspur.cloud", "com.inspur.common"})
public class IIDApplication {
    public static void main(String[] args) {
        SpringApplication.run(IIDApplication.class, args);
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_THREADLOCAL);
    }
}
