package com.gong.pojo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GateDevice{

    @ExcelProperty(value = { "id"}, index = 0)
    private int id;

    @ExcelProperty(value = { "网关ID"}, index = 1)
    private int gid;

    @ExcelProperty(value = { "设备型号ID"}, index = 2)
    private int did;

    @ExcelProperty(value = { "终端设备名"}, index = 3)
    private String clientdevicename;

    @ExcelProperty(value = { "终端设备代码"}, index = 4)
    private String clientdeviceid;

    @ExcelProperty(value = { "经度"}, index = 5)
    private String longitude;

    @ExcelProperty(value = { "纬度"}, index = 6)
    private String latitude;

    @ExcelIgnore
    private String secretkey;

    @ExcelIgnore
    private String clientdeviceconfig;

    @ExcelIgnore
    private String clientdevicestate;

    @ExcelIgnore
    private String clientdeviceenabled;

    @ExcelIgnore
    private String remark;

    @ExcelIgnore
    private Timestamp addtime;
}
