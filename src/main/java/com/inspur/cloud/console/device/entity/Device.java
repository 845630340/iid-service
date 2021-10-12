package com.inspur.cloud.console.device.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import java.util.Date;

@Data
@HeadStyle(fillPatternType = FillPatternType.SOLID_FOREGROUND, fillForegroundColor = 1,
        wrapped=false,horizontalAlignment= HorizontalAlignment.LEFT,locked = false)
@HeadFontStyle(bold = false,fontHeightInPoints = 11)
public class Device {
    /*
    id
     */
    @ExcelProperty("id")
    private String id;
    /*
    用户id
     */
    @ExcelProperty("主账号id")
    private String accountId;
    /*
    用户id
     */
    @ExcelProperty("子账号id")
    private String creatorId;
    /*
    产品id
     */
    @ExcelProperty("产品id")
    private String productId;
    /*
    批次
     */
    @ExcelProperty("批次")
    private String batchNo;
    /*
    设备标识：设备加密签名公钥
     */
    @ExcelProperty("设备标识：设备加密签名公钥")
    private String code;
    /*
    设备加密私钥
     */
    @ExcelProperty("设备加密私钥/设备密钥")
    private String devPriEncKey;
    /*
    设备签名私钥
     */
    @ExcelProperty("设备签名私钥/设备证书")
    private String devPriVfyKey;
    /*
    创建者
     */
    @ExcelProperty("创建者")
    private String createdBy;
    /*
    更改者
     */
    @ExcelProperty("更改者")
    private String modifiedBy;
    /*
    创建时间
     */
    @ExcelProperty("创建时间")
    private Date createdTime;
    /*
    更新时间
     */
    @JsonFormat
    @ExcelProperty("更新时间")
    private Date updatedTime;
    /*
    删除时间
     */
    @ExcelProperty("删除时间")
    private Date deletedTime;
    /*
    是否被软删除
     */
    @ExcelProperty("是否被软删除，0未删除，1已删除")
    private int isDeleted;

    /*
   是否被软删除
    */
    @ExcelProperty("状态 启用/禁用 0 未启用 1 启用")
    private int status;

    /*
    证书类型
     */
    @ExcelProperty("证书类型，0为 SM9，1为 X509")
    private String type;


}
