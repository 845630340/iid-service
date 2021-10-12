package com.inspur.cloud.common.exception.dao;

import com.inspur.cloud.common.exception.model.ErrorCodeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * ErrorCodeMapper
 * @author mysterious guest
 */
@Mapper
@Repository
public interface ErrorCodeMapper {
    /**
     * xxxxxx.xxx
     * @param projectCode 项目代码
     * @param moduleCode 模块代码
     * @param errorCode 错误码
     * @return ErrorCodeEntity
     */
    @Select("SELECT * FROM common_error_code WHERE project_code = #{projectCode} AND module_code=#{moduleCode} AND error_code=#{errorCode}")
    ErrorCodeEntity getErrorCodeEntity(@Param("projectCode") String projectCode,
                                       @Param("moduleCode") String moduleCode,
                                       @Param("errorCode") String errorCode);
}
