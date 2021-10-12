package com.inspur.cloud.console.product.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Size;
import java.util.Date;

/**
 * @author jiangll01
 * @Date: 2020/8/27 14:49
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ProductVo {
    private String id;
    private String certId;
    private String name;
    private String producer;
    private String spec;
    private String accountId;
    private String creatorId;
    private String createdBy;
    private String modifiedBy;
    private Integer status;
    @JsonFormat
    private Date createdTime;
    @JsonFormat
    private Date updatedTime;

}
