package com.inspur.cloud.console.product.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductsVO {
    private String id;
    private String name;
    private String producer;
    private String spec;
    private String source;
    private Integer status;
    @JsonFormat
    private Date createdTime;
}
