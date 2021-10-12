package com.inspur.cloud.console.usercert.service;

import com.inspur.bss.commonsdk.utils.IdWorker;
import com.inspur.cloud.common.exception.AssertUtils;
import com.inspur.cloud.common.exception.ExceptionCode;
import com.inspur.cloud.common.http.RequestHolder;
import com.inspur.cloud.console.kgc.entity.PrimarySecret;
import com.inspur.cloud.console.kgc.entity.ProductAndDeviceNum;
import com.inspur.cloud.console.kgc.service.KgcService;
import com.inspur.cloud.console.rootcert.constant.RootCertConstant;
import com.inspur.cloud.console.rootcert.dao.RootCertMapper;
import com.inspur.cloud.console.rootcert.entity.RootCert;
import com.inspur.cloud.console.usercert.dao.UserCertDao;
import com.inspur.cloud.console.usercert.entity.UserCert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author: JiangYP
 * @Description:
 */

@Slf4j
@Service
public class UserCertService {

    private final UserCertDao userCertDao;
    private final KgcService kgcService;
    private final RootCertMapper rootCertMapper;

    @Autowired
    public UserCertService(UserCertDao userCertDao, KgcService kgcService, RootCertMapper rootCertMapper) {
        this.userCertDao = userCertDao;
        this.kgcService = kgcService;
        this.rootCertMapper = rootCertMapper;
    }

    public UserCert getUserCert() {
        return userCertDao.getUserCert(RequestHolder.getUserId());
    }

    /**
     * 开通服务
     */
    public Integer openService() {
        String userId = RequestHolder.getUserId();
        String creatorId = RequestHolder.getCreatorId();
        String userName = RequestHolder.getUserName();
        AssertUtils.isNull(userCertDao.getUserCert(userId), ExceptionCode.ALREADY_OPEN_SERVICE, HttpStatus.OK);
        PrimarySecret primarySecret = kgcService.createPrimarySecret();
        UserCert userCert = UserCert.builder()
                .id(String.valueOf(IdWorker.getNextId())).accountId(userId).code(userId).creatorId(creatorId).name(userName).isDeleted(0).createdTime(new Date())
                .mainPriEncKey(primarySecret.getEncryptPrimaryPrivateKey()).mainPubEncKey(primarySecret.getEncryptPrimaryPublicKey())
                .mainPriVfyKey(primarySecret.getSignPrimaryPrivateKey()).mainPubVfyKey(primarySecret.getSignPrimaryPublicKey())
                .build();
        return userCertDao.insertUserCert(userCert);
    }

    /**
     * 根证书数量、产品数量、设备数量
     */
    public ProductAndDeviceNum getAllNumber() {
        return userCertDao.getAllNumber(RequestHolder.getUserId());
    }


    /**
     * 判断当前用户是否创建SM9证书
     */
    public int judgeSmNine() {
        return userCertDao.judgeSmNine(RequestHolder.getUserId());
    }


    /**
     * 创建SM9证书
     */
    public void createSM9(RootCert rootCert) {
        String id = String.valueOf(IdWorker.getNextId());
        String userId = RequestHolder.getUserId();
        String creatorId = RequestHolder.getCreatorId();
        String userName = RequestHolder.getUserName();
        AssertUtils.isFalse(userCertDao.judgeSmNine(userId) != 0, ExceptionCode.ALREADY_OPEN_SERVICE, HttpStatus.OK);
        PrimarySecret primarySecret = kgcService.createPrimarySecret();
        RootCert newRootCert = RootCert.builder().id(id).name(userName).code(userId).accountId(userId).creatorId(creatorId)
                .mainPubEncKey(primarySecret.getEncryptPrimaryPublicKey())
                .mainPriEncKey(primarySecret.getEncryptPrimaryPrivateKey())
                .mainPubVfyKey(primarySecret.getSignPrimaryPublicKey())
                .mainPriVfyKey(primarySecret.getSignPrimaryPrivateKey())
                .type(RootCertConstant.SM9).certName(rootCert.getCertName()).status(rootCert.getStatus())
                .createdTime(new Date()).updatedTime(new Date()).source(RootCertConstant.SOURCEINTER).build();
        rootCertMapper.insertRootCert(newRootCert);
    }
}
