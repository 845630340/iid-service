package com.inspur.cloud.common;

import com.inspur.cloud.common.exception.handler.BaseExceptionHandler;
import com.inspur.cloud.common.exception.model.ErrorCodeEntity;
import com.inspur.cloud.common.exception.model.ParameterValidationMessage;
import com.inspur.cloud.common.exception.model.ServiceException;
import com.inspur.cloud.common.response.util.ReturnMessageUtil;
import com.inspur.cloud.console.BaseTest;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

/**
 * @Author: JiangYP
 * @Date: 2020/6/17 10:56
 */
public class UtilsTest extends BaseTest {
    @Test
    public void test01() {
        ReturnMessageUtil.success();
        ReturnMessageUtil.success("test");
        ReturnMessageUtil.error("400");
        ReturnMessageUtil.error("400", "bad request");
    }

    @Test
    public void test03() {
        EntityTestUtil.testEntity(ErrorCodeEntity.class);
        EntityTestUtil.testEntity(ParameterValidationMessage.class);
        EntityTestUtil.testEntity(ServiceException.class);
    }



    @Test
    public void testParameterValidationMessage() {
        FieldError fieldError = new FieldError("objectName", "field", "defaultMessage");
        ParameterValidationMessage p01 = new ParameterValidationMessage(fieldError);
        ParameterValidationMessage p02 = new ParameterValidationMessage("fieldError", "description");
    }

    @Test
    public void testServiceException() {
      ServiceException s07 = new ServiceException(HttpStatus.OK, "200");
        s07.setMessage("test");
        s07.setParams(new Object[]{"o"} );
        System.out.println(s07.toString());
    }

    @Test
    public void testBaseExceptionHandler() {
        BaseExceptionHandler.getStackTrace(new Exception());
    }

}
