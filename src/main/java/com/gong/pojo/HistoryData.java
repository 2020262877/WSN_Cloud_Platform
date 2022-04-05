package com.gong.pojo;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryData {
    private int id;
    private int gdid;
    private Timestamp recordtime;
    private String recorddata;
 }
