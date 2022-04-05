package com.gong.service.impl;

import com.gong.mapper.GateDeviceMapper;
import com.gong.pojo.GateDevice;
import com.gong.service.GateDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GateDeviceServiceImpl implements GateDeviceService {

    @Autowired
    private GateDeviceMapper gateDeviceMapper;

    public void setGateDeviceMapper(GateDeviceMapper gateDeviceMapper) {
        this.gateDeviceMapper = gateDeviceMapper;
    }

    @Override
    public int addGateDevice(GateDevice gateDevice) {
        return gateDeviceMapper.addGateDevice(gateDevice);
    }

    @Override
    public int deleteGateDevice(int id) {
        return gateDeviceMapper.deleteGateDevice(id);
    }

    @Override
    public int updateGateDevice(GateDevice gateDevice) {
        return gateDeviceMapper.updateGateDevice(gateDevice);
    }

    @Override
    public GateDevice queryGateDeviceById(int id) {
        return gateDeviceMapper.queryGateDeviceById(id);
    }

    @Override
    public List<GateDevice> queryGateDeviceByDId(int id) {
        return gateDeviceMapper.queryGateDeviceByDId(id);
    }

    @Override
    public List<GateDevice> queryGateDeviceByGId(int id) {
        return gateDeviceMapper.queryGateDeviceByGId(id);
    }


    @Override
    public List<GateDevice> getGateDeviceByNameLike(String value) {
        return gateDeviceMapper.getGateDeviceByNameLike(value);
    }

    @Override
    public List<GateDevice> getGateDeviceByCodeLike(String value) {
        return gateDeviceMapper.getGateDeviceByCodeLike(value);
    }

    @Override
    public List<GateDevice> queryGateDeviceList() {
        return gateDeviceMapper.queryGateDeviceList();
    }
}
