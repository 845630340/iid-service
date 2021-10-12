package com.inspur.cloud.console.product.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author jiangll01
 * @Date: 2020/8/27 19:10
 * @Description:
 */
@Data
public class DeviceDo {
    private String code;
    private Integer status;
    @JsonFormat
    private Date createdTime;
}
