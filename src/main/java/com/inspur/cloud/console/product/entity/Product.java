package com.inspur.cloud.console.product.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private String id ;
    private String name ;
    private String description ;
    private String producer ;
    private String spec ;
    private String status ;
    private String accountId ;
    private String creatorId ;
    private String createdBy ;
    private String modifiedBy ;
    private String productSource ;
    private Date createdTime ;
    private Date updatedTime ;
    private Date deletedTime ;
    private int isDeleted ;
}
