package com.inspur.cloud.console.device.dao;

import com.inspur.cloud.console.device.entity.Device;
import com.inspur.cloud.console.device.entity.DeviceVO;
import com.inspur.cloud.console.device.entity.DownLoad;
import com.inspur.cloud.console.device.entity.UploadData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DeviceDao {
    int selectTotalCount(@Param("accountId")String accountId, @Param("type")String type);

    List<DownLoad> selectDevicesByPage(@Param("accountId")String accountId, @Param("page") int page, @Param("size") int size,
                                       @Param("productName")String productName, @Param("code")String code, @Param("status")Integer status, @Param("type")String type);

    int insert(Device record);

    int insertForList(List<Device> list);

    List<String> selectProductId(String accountId);

    List<String> selectCodeByData(@Param("list") List<UploadData> list,@Param("accountId")String accountId);

    Integer selectDeviceByCode(@Param("code") String code,@Param("accountId")String accountId);

    List<DeviceVO> querySecretKeyList(@Param("userId") String userId, @Param("productName") String productName,
                                      @Param("code") String code, @Param("status") Integer status,@Param("type") String type);

    String queryProductNameById(@Param("productId") String productId);

    Device queryDeviceById(@Param("deviceId")String deviceId, @Param("userId")String userId);

    Integer selectDeviceById(@Param("deviceId")String deviceId);

    Integer setDeviceStatus(@Param("deviceId")String deviceId, @Param("deviceSwitch")String deviceSwitch);

    Integer selectUserByEnterpriseId(@Param("enterpriseId") String enterpriseId);

    String selectDeviceStatus(@Param("enterpriseId")String enterpriseId, @Param("code")String code);
}