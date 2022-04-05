package com.gong.controller;

import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gong.pojo.DeviceType;
import com.gong.service.DeviceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/devicetype")
public class DeviceTypeController {

    @Autowired
    private DeviceTypeService deviceTypeService;

    @RequestMapping("/alldevicetype")
    public String queryDeviceTypeList(HttpServletRequest request){
        String keyword = request.getParameter("keyword");
        String fieldname = request.getParameter("fieldname");
        String page = request.getParameter("page");
        if (page == null) {
            page = "1";
        }
        int pageNum = Integer.parseInt(page);
        int pageSize = 5;
        if (keyword != null && fieldname != null && fieldname.length() > 0 && keyword.length() > 0) {
            //通过id查询
            if(fieldname.equals("id")){
                int id = Integer.parseInt(keyword);
                DeviceType deviceType = deviceTypeService.queryDeviceTypeById(id);
                List<DeviceType> deviceTypes = new ArrayList<>();
                if(deviceType!=null){
                    deviceTypes.add(deviceType);
                }
                request.setAttribute("devicestype", deviceTypes);
                request.setAttribute("total", 1);// 计算总页数
                request.setAttribute("currentPage", 1);
            }else if("devicetypename".equals(fieldname)){
                //通过设备名称或设备代码查询
                PageHelper.startPage(pageNum,pageSize);
                List<DeviceType> deviceTypes = deviceTypeService.getDeviceTypeByNameLike(keyword);
                GetDeviceTypes(request, deviceTypes);
            }else if("devicetypecode".equals(fieldname)){
                PageHelper.startPage(pageNum,pageSize);
                List<DeviceType> deviceTypes = deviceTypeService.getDeviceTypeByCodeLike(keyword);
                GetDeviceTypes(request, deviceTypes);
            }
            request.setAttribute("fieldname", fieldname);
            request.setAttribute("keyword", keyword);
            return "devicetypelist";
        }
        //无条件查询
        PageHelper.startPage(pageNum,pageSize);
        List<DeviceType> deviceTypes = deviceTypeService.queryDeviceTypeList();
        GetDeviceTypes(request, deviceTypes);
        return "devicetypelist";
    }

    private void GetDeviceTypes(HttpServletRequest request, List<DeviceType> deviceTypes) {
        PageInfo pageInfo = new PageInfo(deviceTypes);
        List<DeviceType> devicestype = pageInfo.getList();
        request.setAttribute("devicestype", devicestype);
        int totalPages = (int) ((pageInfo.getTotal()-1)/pageInfo.getPageSize()+1);
        request.setAttribute("total", totalPages);// 计算总页数
        request.setAttribute("currentPage", pageInfo.getPageNum());
    }

    @RequestMapping("/toadd")
    public String toAddDeviceType(){
        return "devicetypeadd";
    }

    @RequestMapping("/add")
    public void addDeviceType(DeviceType deviceType,HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        deviceType.setAddtime(new Timestamp((new Date()).getTime()));
        int i = deviceTypeService.addDeviceType(deviceType);
        if(i>0){
            request.setAttribute("msg", "添加成功！");
        }else {
            request.setAttribute("msg", "添加失败！");
        }
        request.getRequestDispatcher("/devicetype/alldevicetype").forward(request,response);
    }

    @RequestMapping("/delete")
    public void deleteDeviceType(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");
        int i = deviceTypeService.deleteDeviceType(Integer.parseInt(idStr));
        if(i>0){
            request.setAttribute("msg", "删除成功！");
        }else {
            request.setAttribute("msg", "删除失败！");
        }
        request.getRequestDispatcher("/devicetype/alldevicetype").forward(request,response);
    }

    @RequestMapping("/toupdate")
    public String toUpdateDevicetype(HttpServletRequest request){
        String idStr = request.getParameter("id");
        DeviceType deviceType = deviceTypeService.queryDeviceTypeById(Integer.parseInt(idStr));
        request.setAttribute("devicetype", deviceType);
        return "devicetypeedit";
    }

    @RequestMapping("/update")
    public void updateDeviceType(DeviceType deviceType,HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        int i = deviceTypeService.updateDeviceType(deviceType);
        if(i>0){
            request.setAttribute("msg", "修改成功！");
        }else{
            request.setAttribute("msg", "修改失败！");
        }
        request.getRequestDispatcher("/devicetype/alldevicetype").forward(request,response);
    }

    @RequestMapping("/devicetypeExcel")
    public void devicetypeExcel(HttpServletResponse response,HttpServletRequest request){
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            Date date = Calendar.getInstance().getTime();
            SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMddHHmmss");
            String formatDate = sdf.format(date);
            // 设置文件名
            String fileName = URLEncoder.encode(formatDate +"设备类型", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            String sheetName = "数据展示";
            // 按条件筛选records
            List<DeviceType> list = deviceTypeService.queryDeviceTypeList();
            // easyexcel工具类实现Excel文件导出
            EasyExcel.write(response.getOutputStream(), DeviceType.class).sheet(sheetName).doWrite(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
