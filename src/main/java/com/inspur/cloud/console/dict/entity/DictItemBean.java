package com.inspur.cloud.console.dict.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

/**
 * 一句话功能简介
 *
 * @author: xushiqiang
 * @date: 2019/2/13
 */
@Data
public class DictItemBean
{
    @JsonIgnore
    private String id;
    /* 字典项ID */
    private String itemId;

    /* 字典组 */
    private String groupId;

    /* 字典项对应的值 */
    private String itemValue;

    /* 创建时间 */
    @JsonIgnore
    private Date createdTime;

    /* 描述 */
    private String itemDesc;

    /* 排序 */
    private String sort;

    /* 状态 */
    @JsonIgnore
    private String status;
}
