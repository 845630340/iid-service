package com.inspur.cloud.common.page;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 分页实例
 * @author mysterious guest
 */
@Getter
@Setter
@ToString
public class Page
{
    /**
     * 第几页
     */
    private int pageNo;
    /**
     * 每页的数目
     */
    private int pageSize;
    /**
     * 记录总数
     */
    private int totalCount;

    /**
     * 存放查询的结果集
     */
    private List<?> data;

}
