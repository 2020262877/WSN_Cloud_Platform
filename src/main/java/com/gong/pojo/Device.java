package com.gong.pojo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Device{

    @ExcelProperty(value = { "id"}, index = 0)
    private int id;

    @ExcelProperty(value = { "设备类型id"}, index = 1)
    private int dtid;

    @ExcelProperty(value = { "设备代码"}, index = 2)
    private String devicecode;

    @ExcelProperty(value = { "设备名称"}, index = 3)
    private String devicename;

    @ExcelIgnore
    private String devicephoto;

    @ExcelIgnore
    private String deviceconfig;

    @ExcelIgnore
    private Timestamp addtime;

    @ExcelIgnore
    private Integer deviceenabled;
}
