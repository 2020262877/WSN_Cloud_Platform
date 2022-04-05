package com.gong.service.impl;

import com.gong.mapper.DeviceMapper;
import com.gong.mapper.DeviceTypeMapper;
import com.gong.pojo.Device;
import com.gong.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private DeviceTypeMapper deviceTypeMapper;

    public void setDeviceMapper(DeviceMapper deviceMapper) {
        this.deviceMapper = deviceMapper;
    }

    public void setDeviceTypeMapper(DeviceTypeMapper deviceTypeMapper) {
        this.deviceTypeMapper = deviceTypeMapper;
    }

    @Override
    public int addDevice(Device device) {
        return deviceMapper.addDevice(device);
    }

    @Override
    public int deleteDevice(int id) {
        return deviceMapper.deleteDevice(id);
    }

    @Override
    public int updateDevice(Device device) {
        return deviceMapper.updateDevice(device);
    }

    @Override
    public Device queryDeviceById(int id) {
        return deviceMapper.queryDeviceById(id);
    }

    @Override
    public List<Device> queryDeviceByDTId(int id) {
        return deviceMapper.queryDeviceByDTId(id);
    }

    @Override
    public List<Device> getDeviceByNameLike(String value) {
        return deviceMapper.getDeviceByNameLike(value);
    }

    @Override
    public List<Device> getDeviceByCodeLike(String value) {
        return deviceMapper.getDeviceByCodeLike(value);
    }


    @Override
    public List<Device> queryDeviceList() {
        return deviceMapper.queryDeviceList();
    }
}
