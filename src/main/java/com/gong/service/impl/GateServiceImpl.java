package com.gong.service.impl;

import com.gong.mapper.GateMapper;
import com.gong.pojo.Gate;
import com.gong.service.GateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GateServiceImpl implements GateService {

    @Autowired
    private GateMapper gateMapper;

    public void setGateMapper(GateMapper gateMapper) {
        this.gateMapper = gateMapper;
    }

    @Override
    public int addGate(Gate gate) {
        return gateMapper.addGate(gate);
    }

    @Override
    public int deleteGate(int id) {
        return gateMapper.deleteGate(id);
    }

    @Override
    public int updateGate(Gate gate) {
        return gateMapper.updateGate(gate);
    }

    @Override
    public Gate queryGateById(int id) {
        return gateMapper.queryGateById(id);
    }

    @Override
    public List<Gate> getGateByNameLike(String value) {
        return gateMapper.getGateByNameLike(value);
    }

    @Override
    public List<Gate> getGateByCodeLike(String value) {
        return gateMapper.getGateByCodeLike(value);
    }

    @Override
    public List<Gate> queryGateList() {
        return gateMapper.queryGateList();
    }
}
