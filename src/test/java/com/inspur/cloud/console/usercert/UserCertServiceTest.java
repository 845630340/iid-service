package com.inspur.cloud.console.usercert;

import com.inspur.cloud.console.BaseTest;
import com.inspur.cloud.console.kgc.entity.PrimarySecret;
import com.inspur.cloud.console.kgc.service.KgcService;
import com.inspur.cloud.console.usercert.dao.UserCertDao;
import com.inspur.cloud.console.usercert.entity.UserCert;
import com.inspur.cloud.console.usercert.service.UserCertService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.parameters.P;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * @Author: JiangYP
 * @Description:
 */
public class UserCertServiceTest extends BaseTest {

    @Autowired
    private UserCertService userCertService;

    @MockBean
    private UserCertDao userCertDao;

    @MockBean
    private KgcService kgcService;

    @Test
    public void getUserCertTest() {
        when(userCertDao.getUserCert(any())).thenReturn(new UserCert());
        userCertService.getUserCert();
    }

    @Test
    public void openServiceTest() {
        when(userCertDao.getUserCert(any())).thenReturn(null);
        PrimarySecret primarySecret = new PrimarySecret();
        primarySecret.setSignPrimaryPublicKey("1");
        primarySecret.setSignPrimaryPrivateKey("2");
        primarySecret.setEncryptPrimaryPublicKey("3");
        primarySecret.setEncryptPrimaryPrivateKey("4");
        when(kgcService.createPrimarySecret()).thenReturn(primarySecret);
        when(userCertDao.insertUserCert(any(UserCert.class))).thenReturn(1);
        userCertService.openService();
    }

//    @Test
//    public void calculateNumberTest() {
//        when(userCertDao.productNumber(any())).thenReturn(13);
//        when(userCertDao.deviceNumber(any())).thenReturn(13);
//        userCertService.calculateNumber();
//    }
}
