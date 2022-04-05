package com.gong.mapper;

import com.gong.pojo.Device;

import java.util.List;

public interface DeviceMapper {
    //增加设备
    int addDevice(Device device);

    //删除设备
    int deleteDevice(int id);

    //修改设备
    int updateDevice(Device device);

    //根据设备id查询设备
    Device queryDeviceById(int id);

    //根据设备类型id查询设备
    List<Device> queryDeviceByDTId(int id);

    //通过设备名，模糊查询设备类型
    List<Device> getDeviceByNameLike(String value);

    //通过设备型号代码，模糊查询设备类型
    List<Device> getDeviceByCodeLike(String value);

    //查询全部设备
    List<Device> queryDeviceList();
}
