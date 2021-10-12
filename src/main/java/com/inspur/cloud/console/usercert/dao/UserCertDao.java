package com.inspur.cloud.console.usercert.dao;

import com.inspur.cloud.console.kgc.entity.ProductAndDeviceNum;
import com.inspur.cloud.console.usercert.entity.UserCert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author jiangyupeng
 */
@Mapper
@Repository
public interface UserCertDao {
    /**
     * 1、可用来判断当前用户是否开通服务
     * 2、可用来查看/下载主密钥
     * @param accountId
     */
    UserCert getUserCert(String accountId);

    Integer insertUserCert(@Param("userCert") UserCert userCert);

    int judgeSmNine(String accountId);

    ProductAndDeviceNum getAllNumber(String accountId);

    UserCert getUserCertByDeviceId(@Param("userId")String userId, @Param("deviceId")String deviceId);

    UserCert getUserCertByProductId(@Param("accountId")String accountId, @Param("productId")String productId);
}
