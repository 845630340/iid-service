package com.inspur.cloud.common.generateid;

/**
 * @author zhangrui
 */

public enum IdType {
    /**
     * 用户自己输入
     */
    NONE,
    /**
     * 雪花算法
     */
    ASSIGN_ID,
    /**
     * UUID
     */
    ASSIGN_UUID
}
