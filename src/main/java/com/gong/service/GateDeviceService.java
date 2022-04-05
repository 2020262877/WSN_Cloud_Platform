package com.gong.service;

import com.gong.pojo.GateDevice;

import java.util.List;

public interface GateDeviceService {
    //增加监控设备
    int addGateDevice(GateDevice gateDevice);

    //删除监控设备
    int deleteGateDevice(int id);

    //修改监控设备
    int updateGateDevice(GateDevice gateDevice);

    //根据设备id查询监控设备
    GateDevice queryGateDeviceById(int id);

    //根据设备型号id查询监控设备
    List<GateDevice> queryGateDeviceByDId(int id);

    //根据网关id查询监控设备
    List<GateDevice> queryGateDeviceByGId(int id);

    //通过设备名，模糊查询监控设备
    List<GateDevice> getGateDeviceByNameLike(String value);

    //通过设备型号代码，模糊查询监控设备
    List<GateDevice> getGateDeviceByCodeLike(String value);

    //查询全部监控设备
    List<GateDevice> queryGateDeviceList();
}
