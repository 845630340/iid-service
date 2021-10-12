package com.inspur.cloud.console.device.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.inspur.cloud.common.http.RequestHolder;
import com.inspur.cloud.common.page.Page;
import com.inspur.cloud.common.response.annotation.ResponseResult;
import com.inspur.cloud.common.response.constant.ApiConstants;
import com.inspur.cloud.console.device.entity.Device;
import com.inspur.cloud.console.device.entity.DownLoad;
import com.inspur.cloud.console.device.service.DeviceService;
import com.inspur.common.operationlog.annotation.OperationLog;
import com.inspur.iam.adapter.annotation.PermissionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;


@RequestMapping("/iid/v1/device")
@RestController
@ResponseResult
public class DeviceController {
    private final DeviceService deviceService;

    @Autowired
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    /**
     * 批量下载
     *
     * @param response
     * @throws IOException
     */
    @GetMapping("/download")
    @PermissionContext(loginAccess = true)
    @OperationLog(resourceType = ApiConstants.RESOURCETYPE,
            eventName = "downloadDevices")
    public void downloadDevices(HttpServletResponse response, String type,
                                String productName, String code, Integer status) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("data", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), DownLoad.class).build();
        deviceService.download(excelWriter, type, productName, code, status, response);
    }

    /**
     * 单个生成设备密钥
     *
     * @param productId
     * @param code
     */
    @GetMapping("/create/secret")
    @OperationLog(resourceType = ApiConstants.RESOURCETYPE,
            eventName = "createSecret",
            resourceIds = "#code")
    @PermissionContext(loginAccess = true)
    public String createSecret(String productId, String code, String type) {
        deviceService.create(productId, code, type);
        return "ok";
    }

    /**
     * 批量生成设备密钥
     *
     * @param file
     */
    @PostMapping("/create/secrets")
    @OperationLog(resourceType = ApiConstants.RESOURCETYPE,
            eventName = "createSecrets")
    @PermissionContext(loginAccess = true)
    public String createSecrets(MultipartFile file, String type) throws IOException {
        InputStream inputStream = file.getInputStream();
        String userId = RequestHolder.getUserId();
        String creatorId = RequestHolder.getCreatorId();
        deviceService.createSecrets(inputStream, userId, creatorId, type);
        return "ok";
    }

    /**
     * 查询设备密钥列表
     *
     * @param pageNo
     * @param pageSize
     * @param productName
     * @param code
     * @param status
     * @return
     */
    @PermissionContext(loginAccess = true)
    @OperationLog(resourceType = ApiConstants.RESOURCETYPE,
            eventName = "querySecretKeyList")
    @GetMapping("/{pageNo}/{pageSize}")
    public Page querySecretKeyList(@PathVariable("pageNo") int pageNo, @PathVariable("pageSize") int pageSize,
                                   String productName, String code, Integer status, String type) {
        return deviceService.querySecretKeyList(pageNo, pageSize, productName, code, status, type);
    }

    /**
     * 单个下载
     *
     * @param response
     * @param deviceId
     * @throws IOException
     */
    @GetMapping("/downloadDevice")
    @PermissionContext(loginAccess = true)
    public void downloadDevice(HttpServletResponse response,
                               String deviceId) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String userId = RequestHolder.getUserId();
        deviceService.downloadDevice(deviceId, response, userId);
    }

    /**
     * 修改设备状态
     *
     * @param deviceId
     * @param deviceSwitch
     */
    @GetMapping("/status/{deviceId}/switch/{switch}")
    @PermissionContext(loginAccess = true)
    public void deviceSwitch(@PathVariable("deviceId") String deviceId, @PathVariable("switch") String deviceSwitch) {
        String userId = RequestHolder.getUserId();
        deviceService.setDeviceStatus(deviceId, deviceSwitch, userId);
    }

    /**
     * 获取设备状态
     *
     * @param enterpriseId
     * @param code
     * @return
     */
    @GetMapping("/status")
    @PermissionContext(loginAccess = true)
    public String getDeviceStatus(String enterpriseId, String code) {
        return deviceService.getDeviceStatus(enterpriseId, code);
    }

}

