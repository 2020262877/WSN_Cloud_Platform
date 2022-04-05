package com.gong.controller;

import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gong.pojo.Device;
import com.gong.pojo.Gate;
import com.gong.pojo.GateDevice;
import com.gong.service.DeviceService;
import com.gong.service.GateDeviceService;
import com.gong.service.GateService;
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
import java.util.*;

@Controller
@RequestMapping("/gatedevice")
public class GateDeviceController {

    @Autowired
    private GateDeviceService gateDeviceService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private GateService gateService;

    @RequestMapping("/allgatedevice")
    public String queryGateDeviceList(HttpServletRequest request){
        String keyword = request.getParameter("keyword");
        String fieldname = request.getParameter("fieldname");
        String page = request.getParameter("page");
        if (page == null) {
            page = "1";
        }
        int pageNum = Integer.parseInt(page);
        int pageSize = 5;


        if (keyword != null && fieldname != null && fieldname.length() > 0 && keyword.length() > 0) {
            if (fieldname.equals("id")) {
                //通过id查询
                //先判断是否全为数字
                int id = Integer.parseInt(keyword);
                GateDevice gateDevice = gateDeviceService.queryGateDeviceById(id);
                List<GateDevice> gateDevices = new ArrayList<>();
                if(gateDevice!=null){
                    gateDevices.add(gateDevice);
                }
                request.setAttribute("devices", gateDevices);
                request.setAttribute("total", 1);// 计算总页数
                request.setAttribute("currentPage", 1);
            }else if("did".equals(fieldname)){
                //通过设备类型id查询
                int did = Integer.parseInt(keyword);
                List<GateDevice> gateDeviceList = gateDeviceService.queryGateDeviceByDId(did);
                GetGateDevice(request, gateDeviceList);
            }else if("gid".equals(fieldname)){
                //通过设备类型id查询
                int gid = Integer.parseInt(keyword);
                List<GateDevice> gateDeviceList = gateDeviceService.queryGateDeviceByGId(gid);
                GetGateDevice(request, gateDeviceList);
            }else if("clientdevicename".equals(fieldname)){
                //通过设备名称查询
                PageHelper.startPage(pageNum,pageSize);
                List<GateDevice> gateDeviceByNameLike = gateDeviceService.getGateDeviceByNameLike(keyword);
                GetGateDevice(request, gateDeviceByNameLike);
            }else if("clientdeviceid".equals(fieldname)){
                //通过设备代码查询
                PageHelper.startPage(pageNum,pageSize);
                List<GateDevice> gateDeviceByCodeLike = gateDeviceService.getGateDeviceByCodeLike(keyword);
                GetGateDevice(request,gateDeviceByCodeLike);
            }
            // 回传给页面显示查询条件，否则会清空
            request.setAttribute("fieldname", fieldname);
            request.setAttribute("keyword", keyword);
            return "gatedevicelist";
        }
        //无条件查询
        PageHelper.startPage(pageNum,pageSize);
        List<GateDevice> gateDeviceList = gateDeviceService.queryGateDeviceList();
        GetGateDevice(request, gateDeviceList);
        return "gatedevicelist";
    }

    private void GetGateDevice(HttpServletRequest request, List<GateDevice> gateDeviceParam) {
        PageInfo<GateDevice> pageInfo = new PageInfo<>(gateDeviceParam);
        List<GateDevice> gateDevices = pageInfo.getList();
        if(pageInfo.getPageSize()!=0){
            int totalPages = (int) ((pageInfo.getTotal()-1)/pageInfo.getPageSize()+1);
            request.setAttribute("total", totalPages);// 计算总页数
            request.setAttribute("currentPage", pageInfo.getPageNum());
        }else{
            //说明为空
            request.setAttribute("total", 1);// 计算总页数
            request.setAttribute("currentPage",1);
        }
        request.setAttribute("gatedevices", gateDevices);
    }

    @RequestMapping("/toadd")
    public String toAdd(HttpServletRequest request){
        GetGateDeviceMap(request);
        return "gatedeviceadd";
    }

    @RequestMapping("/add")
    public void addGateDeviceType(GateDevice gateDevice, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        gateDevice.setAddtime(new Timestamp((new Date()).getTime()));
        String secretkey = UUID.randomUUID().toString();
        System.out.println(secretkey);
        gateDevice.setSecretkey(secretkey);
        int i = gateDeviceService.addGateDevice(gateDevice);
        if(i>0){
            request.setAttribute("msg", "添加成功！");
        }else {
            request.setAttribute("msg", "添加失败！");
        }
        request.getRequestDispatcher("/gatedevice/allgatedevice").forward(request,response);
    }

    @RequestMapping("/delete")
    public void deleteGateDeviceType(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");
        int i = gateDeviceService.deleteGateDevice(Integer.parseInt(idStr));
        if(i>0){
            request.setAttribute("msg", "删除成功！");
        }else {
            request.setAttribute("msg", "删除失败！");
        }
        request.getRequestDispatcher("/gatedevice/allgatedevice").forward(request,response);
    }

    @RequestMapping("/toupdate")
    public String toUpdateGateDevice(HttpServletRequest request){
        String idStr = request.getParameter("id");
        GateDevice gateDevice = gateDeviceService.queryGateDeviceById(Integer.parseInt(idStr));
        request.setAttribute("gatedevice", gateDevice);
        GetGateDeviceMap(request);
        return "gatedeviceedit";
    }

    @RequestMapping("/update")
    public void updateGateDevice(GateDevice gateDevice,HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        int i = gateDeviceService.updateGateDevice(gateDevice);
        if(i>0){
            request.setAttribute("msg", "修改成功！");
        }else{
            request.setAttribute("msg", "修改失败！");
        }
        request.getRequestDispatcher("/gatedevice/allgatedevice").forward(request,response);
    }

    private void GetGateDeviceMap(HttpServletRequest request) {
        List<Gate> gates = gateService.queryGateList();
        List<Device> devices = deviceService.queryDeviceList();
        Map<Integer,String> gateMap = new HashMap<>();
        Map<Integer,String> deviceMap = new HashMap<>();
        for (Gate gate:gates){
            gateMap.put(gate.getId(),gate.getGatename());
        }
        for (Device device:devices){
            deviceMap.put(device.getId(),device.getDevicename());
        }
        request.setAttribute("gateMap",gateMap);
        request.setAttribute("deviceMap",deviceMap);
    }

    @RequestMapping("/gatedeviceExcel")
    public void gateDeviceExcel(HttpServletResponse response,HttpServletRequest request){
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            Date date = Calendar.getInstance().getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String formatDate = sdf.format(date);
            // 设置文件名
            String fileName = URLEncoder.encode(formatDate +"终端设备", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            String sheetName = "数据展示";
            // 按条件筛选records
            List<GateDevice> list = gateDeviceService.queryGateDeviceList();
            // easyexcel工具类实现Excel文件导出
            EasyExcel.write(response.getOutputStream(), GateDevice.class).sheet(sheetName).doWrite(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/todetail")
    public String toDetail(HttpServletRequest request){
        int id = Integer.parseInt(request.getParameter("id"));
        System.out.println("id="+id);
        GateDevice gateDeviceDetail = gateDeviceService.queryGateDeviceById(id);
        System.out.println(gateDeviceDetail);
        request.setAttribute("gateDeviceDetail",gateDeviceDetail);
        return "gatedevicedetail";
    }

    @RequestMapping("/detail")
    public void Detail(){

    }
}
