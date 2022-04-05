package com.gong.pojo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User{

    @ExcelProperty(value = { "id"}, index = 0)
    private int id;

    @ExcelProperty(value = { "用户名"}, index = 1)
    private String username;

    @ExcelIgnore
    private String password;

    @ExcelProperty(value = { "用户类型"}, index = 2)
    private String usertype;

    @ExcelIgnore
    private String mail;
}
