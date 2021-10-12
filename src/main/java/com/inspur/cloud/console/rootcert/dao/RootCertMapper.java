package com.inspur.cloud.console.rootcert.dao;

import com.inspur.cloud.console.product.entity.DeviceDo;
import com.inspur.cloud.console.rootcert.entity.RootCert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jiangll01
 * @Date: 2020/10/27 14:25
 * @Description:
 */
@Repository
@Mapper
public interface RootCertMapper {
    public List<RootCert> queryRootCertList(String userId);

    Integer insertRootCert(@Param("rootCert") RootCert rootCert);

    RootCert getRootCertDetail(@Param("userId") String userId, @Param("id") String id);

    void editRootCert(@Param("rootCert") RootCert rootCert);

    Integer nameValid(@Param("userId") String userId, @Param("certName") String certName);

    List<RootCert> qryProductListForPage(@Param("userId") String userId, @Param("certName") String certName);
}
