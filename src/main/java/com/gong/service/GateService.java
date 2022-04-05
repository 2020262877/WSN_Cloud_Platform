package com.gong.service;

import com.gong.pojo.Gate;

import java.util.List;

public interface GateService {

    //增加网关
    int addGate(Gate gate);

    //删除网关
    int deleteGate(int id);

    //修改网关
    int updateGate(Gate gate);

    //根据id查询网关
    Gate queryGateById(int id);

    List<Gate> getGateByNameLike(String value);

    List<Gate> getGateByCodeLike(String value);

    //查询全部网关
    List<Gate> queryGateList();
}
