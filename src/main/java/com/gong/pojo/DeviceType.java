package com.gong.pojo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceType{

    @ExcelProperty(value = { "id"}, index = 0)
    private int id;

    @ExcelProperty(value = { "设备代码"}, index = 1)
    private String devicetypecode;

    @ExcelProperty(value = { "设备名称"}, index = 2)
    private String devicetypename;

    @ExcelIgnore
    private Timestamp addtime;
}
