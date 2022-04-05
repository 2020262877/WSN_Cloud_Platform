package com.gong.service.impl;

import com.gong.mapper.DeviceTypeMapper;
import com.gong.pojo.DeviceType;
import com.gong.service.DeviceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceTypeServiceImpl implements DeviceTypeService {

    @Autowired
    private DeviceTypeMapper deviceTypeMapper;

    public void setDeviceTypeMapper(DeviceTypeMapper deviceTypeMapper) {
        this.deviceTypeMapper = deviceTypeMapper;
    }

    @Override
    public int addDeviceType(DeviceType deviceType) {
        return deviceTypeMapper.addDeviceType(deviceType);
    }

    @Override
    public int deleteDeviceType(int id) {
        return deviceTypeMapper.deleteDeviceType(id);
    }

    @Override
    public int updateDeviceType(DeviceType deviceType) {
        return deviceTypeMapper.updateDeviceType(deviceType);
    }

    @Override
    public DeviceType queryDeviceTypeById(int id) {
        return deviceTypeMapper.queryDeviceTypeById(id);
    }

    @Override
    public List<DeviceType> queryDeviceTypeList() {
        return deviceTypeMapper.queryDeviceTypeList();
    }

    @Override
    public List<DeviceType> getDeviceTypeByNameLike(String value) {
        return deviceTypeMapper.getDeviceTypeByNameLike(value);
    }

    @Override
    public List<DeviceType> getDeviceTypeByCodeLike(String value) {
        return deviceTypeMapper.getDeviceTypeByCodeLike(value);
    }
}
