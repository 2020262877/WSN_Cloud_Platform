package com.gong.pojo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Gate {

    @ExcelProperty(value = { "id"}, index = 0)
    private int id;

    @ExcelProperty(value = { "gatename"}, index = 1)
    private String gatename;

    @ExcelProperty(value = { "gatecode"}, index = 2)
    private String gatecode;

    @ExcelIgnore
    private String gateenabled;

    @ExcelIgnore
    private Timestamp addtime;
}
