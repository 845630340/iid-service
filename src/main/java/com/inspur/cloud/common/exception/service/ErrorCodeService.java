package com.inspur.cloud.common.exception.service;

import com.inspur.cloud.common.exception.dao.ErrorCodeMapper;
import com.inspur.cloud.common.exception.model.ErrorCodeEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author mysterious guest
 */
@Service
public class ErrorCodeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorCodeService.class);
    private final ErrorCodeMapper errorCodeMapper;

    @Autowired
    public ErrorCodeService(ErrorCodeMapper errorCodeMapper) {
        this.errorCodeMapper = errorCodeMapper;
    }

    @Cacheable(value = "errorCodeCache", key = "#projectCode+#moduleCode+#errorCode")
    public ErrorCodeEntity getErrorCodeEntity(String projectCode, String moduleCode, String errorCode) {
        LOGGER.debug("Cache miss.");
        return errorCodeMapper.getErrorCodeEntity(projectCode, moduleCode, errorCode);
    }
}
