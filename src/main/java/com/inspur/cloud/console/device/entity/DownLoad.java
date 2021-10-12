package com.inspur.cloud.console.device.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.util.Date;

@Data
@HeadStyle(fillPatternType = FillPatternType.SOLID_FOREGROUND, fillForegroundColor = 1,
        wrapped=false,horizontalAlignment= HorizontalAlignment.LEFT,locked = false)
@HeadFontStyle(bold = false,fontHeightInPoints = 11)
public class DownLoad {
    /*
 id
  */
    @ExcelProperty("id")
    private String id;
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
    @ExcelProperty("设备加密私钥")
    private String devPriEncKey;
    /*
    设备签名私钥
     */
    @ExcelProperty("设备签名私钥")
    private String devPriVfyKey;
    /*
    创建时间
     */
    @ExcelProperty("创建时间")
    private Date createdTime;
    /*
   是否被启用
    */
    @ExcelProperty("状态 启用/禁用 0 未启用 1 启用")
    private int status;
}
