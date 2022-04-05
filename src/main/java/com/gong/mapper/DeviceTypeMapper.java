package com.gong.mapper;

import com.gong.pojo.DeviceType;

import java.util.List;

public interface DeviceTypeMapper {

    //增加设备类型
    int addDeviceType(DeviceType deviceType);

    //删除设备类型
    int deleteDeviceType(int id);

    //修改设备类型
    int updateDeviceType(DeviceType deviceType);

    //通过id查询设备类型
    DeviceType queryDeviceTypeById(int id);
    
    //查询全部设备类型
    List<DeviceType> queryDeviceTypeList();

    //通过设备类型名，模糊查询设备类型
    List<DeviceType> getDeviceTypeByNameLike(String value);

    //通过设备类型代码，模糊查询设备类型
    List<DeviceType> getDeviceTypeByCodeLike(String value);
}
