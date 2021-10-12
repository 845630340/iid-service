package com.inspur.cloud.console.device.service;

import com.inspur.cloud.console.BaseTest;
import com.inspur.cloud.console.device.dao.DeviceDao;
import com.inspur.cloud.console.device.entity.DeviceVO;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

public class DeviceServiceTest extends BaseTest {

    @InjectMocks
    private DeviceService deviceService;

    @Mock
    private DeviceDao deviceDao;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void querySecretKeyList() {
        List<DeviceVO> deviceVOS = new ArrayList<>();
        DeviceVO deviceVO = new DeviceVO();
        deviceVO.setId("11");
        deviceVO.setCode("11");
        deviceVOS.add(deviceVO);
        Mockito.when(deviceDao.querySecretKeyList(anyString(), anyString(), anyString(), any())).thenReturn(deviceVOS);
        Mockito.when(deviceDao.queryProductNameById(anyString())).thenReturn("aaa");
        deviceService.querySecretKeyList(1,10,"","",1);
    }
}