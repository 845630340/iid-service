package com.inspur.cloud.console.rootcert.controller;

import com.inspur.cloud.common.page.Page;
import com.inspur.cloud.common.response.annotation.ResponseResult;
import com.inspur.cloud.common.response.constant.ApiConstants;
import com.inspur.cloud.console.rootcert.constant.RootCertConstant;
import com.inspur.cloud.console.rootcert.entity.CreateRootCertParams;
import com.inspur.cloud.console.rootcert.entity.ImportRootCertParams;
import com.inspur.cloud.console.rootcert.entity.RootCert;
import com.inspur.cloud.console.rootcert.filetools.FileUtils;
import com.inspur.cloud.console.rootcert.service.RootCertService;
import com.inspur.common.operationlog.annotation.OperationLog;
import com.inspur.iam.adapter.annotation.PermissionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * @Author: JiangYP
 * @Description:
 */

@ResponseResult
@RestController
@RequestMapping("/iid/v1/root-cert")
public class RootCertController {
    private final RootCertService rootCertService;

    @Autowired
    public RootCertController(RootCertService rootCertService) {
        this.rootCertService = rootCertService;
    }


    @PermissionContext(loginAccess = true)
    @PostMapping("/create-cert")
    public void createCA(@RequestBody CreateRootCertParams createRootCertParams) {
        rootCertService.createCA(createRootCertParams);
    }


    @PermissionContext(loginAccess = true)
    @PostMapping("/import-cert")
    public void importCA(ImportRootCertParams importRootCertParams) throws IOException {
        rootCertService.importCA(importRootCertParams);
    }


    @PermissionContext(loginAccess = true)
    @GetMapping("/download/{certId}/crt")
    public void downloadCaCrt(HttpServletResponse response, @PathVariable("certId") String certId) throws IOException {
        rootCertService.downloadCA(response, certId, "CRT");
    }


    @PermissionContext(loginAccess = true)
    @GetMapping("/download/{certId}/key")
    public void downloadCaKey(HttpServletResponse response, @PathVariable("certId") String certId) throws IOException {
        rootCertService.downloadCA(response, certId, "KEY");
    }


    /**
     * 打包下载根证书两个文件（备用）
     */
    @PermissionContext(loginAccess = true)
    @GetMapping("/download/{certId}/crt-key")
    public void downloadCaCrtAndCaKey(HttpServletResponse response, @PathVariable("certId") String certId) throws IOException {
        rootCertService.downloadZip(response, certId);
    }


    @PermissionContext(loginAccess = true)
    @GetMapping("/{certId}/detail")
    public RootCert getDetail(@PathVariable("certId") String certId) {
        return rootCertService.getRootCertDetail(certId);
    }


    /**
     *
     * @param rootCert: id, status, certName
     */
    @PermissionContext(loginAccess = true)
    @PatchMapping("/edit")
    public void edit(@RequestBody RootCert rootCert) {
        rootCertService.editCA(rootCert);
    }


    /**
     * 证书名称查重
     */
    @PermissionContext(loginAccess = true)
    @GetMapping("/nameValid")
    public void nameValid(@RequestParam String certName) {
        rootCertService.nameValid(certName);
    }


    /**
     *  查询全部开启的根证书列表
     * @return
     */
    @PermissionContext(loginAccess = true)
    @OperationLog(resourceType = ApiConstants.RESOURCETYPE,
            eventName = "queryRootCertList")
    @GetMapping("/rootCertList")
    public List<RootCert> queryRootCertList() {
        return rootCertService.queryRootCertList();
    }

    /**
     *  根证书列表，支持分页
     * @param pageNo
     * @param pageSize
     * @param certName
     * @return
     */
    @PermissionContext(loginAccess = true)
    @OperationLog(resourceType = ApiConstants.RESOURCETYPE,
            eventName = "queryRootCertList")
    @GetMapping("/{pageNo}/{pageSize}")
    public Page queryRootCertList(@PathVariable("pageNo") int pageNo, @PathVariable("pageSize") int pageSize,
                                 @RequestParam(value = "certName", required = false)String certName){
        return rootCertService.queryProductList(pageNo, pageSize, certName);
    }
}
