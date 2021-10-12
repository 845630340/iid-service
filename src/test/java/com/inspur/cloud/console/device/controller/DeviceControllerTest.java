package com.inspur.cloud.console.device.controller;

import com.inspur.cloud.common.page.Page;
import com.inspur.cloud.console.BaseTest;
import com.inspur.cloud.console.device.service.DeviceService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DeviceControllerTest extends BaseTest {
    @InjectMocks
    private DeviceController deviceController;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private DeviceService deviceService;

    private static final String AUTHORIZATION = "Authorization";


    @Before
    public void before() {
        mockMvc = MockMvcBuilders.standaloneSetup(deviceController).build();
    }


    @Test
    public void querySecretKeyList() throws Exception{
        Page page = new Page();
        Mockito.when(deviceController.querySecretKeyList(anyInt(), anyInt(), anyString() ,anyString(), any())).thenReturn(page);
        mockMvc.perform(MockMvcRequestBuilders.get("/iid/v1/device/1/10")
                .header(AUTHORIZATION, "123"))
                .andExpect(status().isOk());
    }
}