package com.inspur.cloud.console.usercert.controller;

import com.inspur.cloud.common.response.annotation.ResponseResult;
import com.inspur.cloud.common.response.constant.ApiConstants;
import com.inspur.cloud.console.kgc.entity.ProductAndDeviceNum;
import com.inspur.cloud.console.rootcert.entity.RootCert;
import com.inspur.cloud.console.usercert.entity.UserCert;
import com.inspur.cloud.console.usercert.service.UserCertService;
import com.inspur.common.operationlog.annotation.OperationLog;
import com.inspur.iam.adapter.annotation.PermissionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: JiangYP
 * @Description:
 */


@ResponseResult
@RestController
@RequestMapping("/iid/v1/root-cert")
public class UserCertController {
    private final UserCertService userCertService;

    @Autowired
    public UserCertController(UserCertService userCertService) {
        this.userCertService = userCertService;
    }


    /**
     * 判断用户是否开发服务
     * 0-未开通   1-已开通
     */
    @OperationLog(resourceType = ApiConstants.RESOURCETYPE, eventName = "judgeOpenState")
    @PermissionContext(loginAccess = true)
    @GetMapping("/sm9/is-open")
    public int isOpenService() {
        return userCertService.judgeSmNine();
    }


    /**
     * 查询产品与设备数量
     */
    @OperationLog(resourceType = ApiConstants.RESOURCETYPE, eventName = "countProductAndDeviceNumbers")
    @PermissionContext(loginAccess = true)
    @GetMapping("/number")
    public ProductAndDeviceNum calulateNumber() {
        return userCertService.getAllNumber();
    }


    /**
     * 创建SM9证书: cert_name, status
     */
    @PermissionContext(loginAccess = true)
    @PostMapping("/create-sm9")
    public void createSM9(@RequestBody RootCert rootCert) {
        userCertService.createSM9(rootCert);
    }
}
