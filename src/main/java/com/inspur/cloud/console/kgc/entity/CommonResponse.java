package com.inspur.cloud.console.kgc.entity;

import lombok.Data;

/**
 * @Author: JiangYP
 * @Description: 三方接口公共响应值
 */

@Data
public class CommonResponse<T> {
    private Boolean success;

    private String msg;

    private T data;

}
