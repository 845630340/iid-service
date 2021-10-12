package com.inspur.cloud.common.exception;

/**
 * @Author: JiangYP
 * @Date: 2020/3/3 19:46
 */
public class ExceptionCode {
    /*产品名称过长或者为空*/
    public static final String LENGTH_OUT_OF_BOUNDS = "809.001001";
    /*证书不存在*/
    public static final String CA_NOT_EXIST = "809.001002";
    /*证书名称重复*/
    public static final String CA_NAME_EXIST = "809.001003";
    /*IP或DNA数量超过20*/
    public static final String NUMBER_TOO_LONG = "809.001004";
    /*IP地址输入错误*/
    public static final String IP_FORMAT_ERROR = "809.001005";

    /*三方接口报错*/
    public static final String QUA_LINK_ERROR = "809.002001";
    /*用户已开通服务*/
    public static final String ALREADY_OPEN_SERVICE = "809.002002";

    /*设备密钥生成失败*/
    public static final String CREAT_DEVICE_SECRET_FAIL = "809.003001";
    /*请勿重复导出*/
    public static final String LOAD_AGAIN = "809.003002";
    /*设备标识重复*/
    public static final String CODE_AGAIN = "809.003003";
    /*产品id不可用*/
    public static final String PRODUCT_ID_IS_INVALID = "809.003004";
    /*生成设备失败*/
    public static final String CREATE_DEVICE_IS_INVALID = "809.003005";
    /*设备不存在*/
    public static final String DEVICE_NOT_EXIST = "809.003006";
    /*设备状态异常*/
    public static final String DEVICE_STATUS_ERROR = "809.003006";
    /*企业不存在*/
    public static final String ENTERPRISE_NOT_EXIST = "809.003007";
}
