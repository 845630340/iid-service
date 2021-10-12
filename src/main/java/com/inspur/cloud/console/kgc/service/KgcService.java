package com.inspur.cloud.console.kgc.service;

import com.alibaba.fastjson.JSONObject;
import com.inspur.cloud.common.exception.AssertUtils;
import com.inspur.cloud.common.exception.ExceptionCode;
import com.inspur.cloud.common.exception.model.ServiceException;
import com.inspur.cloud.common.utils.JsonUtils;
import com.inspur.cloud.console.device.entity.Device;
import com.inspur.cloud.console.kgc.entity.CommonResponse;
import com.inspur.cloud.console.kgc.entity.IdentitySecret;
import com.inspur.cloud.console.kgc.entity.PrimarySecret;
import com.inspur.cloud.console.usercert.entity.UserCert;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: JiangYP
 * @Description: 对接SM9微服务接口
 */

@Slf4j
@Service
public class KgcService {
    @Value("${qualink.url}")
    private String qualink;

    private final RestTemplate restTemplate;

    @Autowired
    public KgcService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public PrimarySecret createPrimarySecret() {
        String url = qualink + "/secret/createPrimarySecret";
        log.info("============== url: {}", url);
        try {
            CommonResponse commonResponse = restTemplate.postForObject(url, null, CommonResponse.class);
            AssertUtils.isTrue(commonResponse != null && commonResponse.getSuccess(), ExceptionCode.QUA_LINK_ERROR, HttpStatus.OK);
            PrimarySecret primarySecret = JsonUtils.fromJson(JsonUtils.toJson(commonResponse.getData()), PrimarySecret.class);
            log.info("========== body : {}", commonResponse.toString());
            return primarySecret;
        } catch (Exception e) {
            log.error("=========== 生成主密钥错误: ", e);
            throw new ServiceException(HttpStatus.OK, ExceptionCode.QUA_LINK_ERROR);
        }
    }

    public void createIdentitySecret(UserCert userCert, Device device){
        String url = qualink + "/secret/createIdentitySecret";
        log.info("============== createIdentitySecret url: {}", url);
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.add(HTTP.CONTENT_TYPE, "application/json");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("encryptPrimaryPrivateKey",userCert.getMainPriEncKey());
        jsonObject.put("signPrimaryPrivateKey",userCert.getMainPriVfyKey());
        jsonObject.put("identification",device.getCode());
        try {
            CommonResponse response = restTemplate.postForObject(url, new HttpEntity<>(jsonObject, httpHeaders), CommonResponse.class);
            AssertUtils.isTrue(response.getSuccess(), ExceptionCode.CREAT_DEVICE_SECRET_FAIL, HttpStatus.OK);
            IdentitySecret identitySecret = JsonUtils.fromJson(JsonUtils.toJson(response.getData()),IdentitySecret.class);
            device.setDevPriEncKey(identitySecret.getEncryptIdentityPrivateKey());
            device.setDevPriVfyKey(identitySecret.getSignIdentityPrivateKey());
        } catch (Exception e) {
            log.error("=========== 生成标识密钥错误: ", e);
            throw new ServiceException(ExceptionCode.CREAT_DEVICE_SECRET_FAIL, HttpStatus.OK);
        }
    }

}
