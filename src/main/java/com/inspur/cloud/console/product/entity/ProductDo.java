package com.inspur.cloud.console.product.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author jiangll01
 * @Date: 2020/8/27 17:39
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ProductDo {
    private String id;
    private String certId;
    private String name;
    private String producer;
    private String spec;
    private String secretKeyTotal;
    private Integer status;
    @JsonFormat
    private Date createdTime;

}
