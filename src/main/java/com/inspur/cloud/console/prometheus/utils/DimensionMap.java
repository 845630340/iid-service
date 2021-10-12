package com.inspur.cloud.console.prometheus.utils;

import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mysterious guest
 */
public class DimensionMap {
    public static Map<String, String> setDimensions(String resourceId, String resourceName,
                                                    String account, String region, String service, String valueName, String status) {
        Map<String, String> map = new HashMap<>();
        map.put("resource_id", resourceId);
        map.put("resource_name", resourceName);
        map.put("account", account);
        map.put("region", region);
        map.put("service", service);
        map.put(valueName, status);
        return map;
    }


    public static Map<String, String> setDimensions(String resourceId, String resourceName,
                                                    String account, String region, String service, String status) {
        Map<String, String> map = new HashMap<>();
        map.put("resource_id", resourceId);
        map.put("resource_name", resourceName);
        map.put("account", account);
        map.put("region", region);
        map.put("service", service);
        map.put("status", status);
        return map;
    }

    public static Map<String, String> setDimensions(String resourceId, String resourceName,
                                                    String account, String region, String service) {
        Map<String, String> map = new HashMap<>();
        map.put("resource_id", resourceId);
        map.put("resource_name", resourceName);
        map.put("account", account);
        map.put("region", region);
        map.put("service", service);
        return map;
    }
}
