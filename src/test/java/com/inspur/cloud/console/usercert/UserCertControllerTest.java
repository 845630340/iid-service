package com.inspur.cloud.console.usercert;

import com.inspur.cloud.console.BaseTest;
import com.inspur.cloud.console.kgc.entity.ProductAndDeviceNum;
import com.inspur.cloud.console.usercert.entity.UserCert;
import com.inspur.cloud.console.usercert.service.UserCertService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;


/**
 * @Author: JiangYP
 * @Description:
 */
public class UserCertControllerTest extends BaseTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserCertService userCertService;
    private static HttpHeaders headers = new HttpHeaders();

    @Test
    public void getUserCertTest() throws Exception {
        given(userCertService.getUserCert()).willReturn(new UserCert());
        mockMvc.perform(MockMvcRequestBuilders.get("/iid/v1/user-cert/info").headers(headers));
    }

    @Test
    public void isOpenServiceTest01() throws Exception {
        given(userCertService.getUserCert()).willReturn(new UserCert());
        mockMvc.perform(MockMvcRequestBuilders.get("/iid/v1/user-cert/is-open").headers(headers));
    }

    @Test
    public void isOpenServiceTest02() throws Exception {
        given(userCertService.getUserCert()).willReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/iid/v1/user-cert/is-open").headers(headers));
    }

    @Test
    public void openServiceTest() throws Exception {
        given(userCertService.openService()).willReturn(1);
        mockMvc.perform(MockMvcRequestBuilders.post("/iid/v1/user-cert/opening").headers(headers));
    }

    @Test
    public void calculateNumberTest() throws Exception {
        given(userCertService.calculateNumber()).willReturn(new ProductAndDeviceNum());
        mockMvc.perform(MockMvcRequestBuilders.get("/iid/v1/user-cert/number").headers(headers));
    }
}
