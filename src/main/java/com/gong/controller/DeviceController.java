package com.gong.controller;

import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gong.pojo.Device;
import com.gong.pojo.DeviceType;
import com.gong.service.DeviceService;
import com.gong.service.DeviceTypeService;
import com.gong.utils.CodeExchange;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/device")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private DeviceTypeService deviceTypeService;

    @RequestMapping("/alldevice")
    public String queryDeviceList(HttpServletRequest request){
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
                Device device = deviceService.queryDeviceById(id);
                List<Device> devices = new ArrayList<>();
                if(device!=null){
                    devices.add(device);
                }
                request.setAttribute("devices", devices);
                request.setAttribute("total", 1);// 计算总页数
                request.setAttribute("currentPage", 1);
            }else if("dtid".equals(fieldname)){
                //通过设备类型id查询
                int dtid = Integer.parseInt(keyword);
                List<Device> deviceList = deviceService.queryDeviceByDTId(dtid);
                GetDevices(request,deviceList);
            }else if("devicename".equals(fieldname)){
                //通过设备名称查询
                PageHelper.startPage(pageNum,pageSize);
                List<Device> deviceByNameLikes = deviceService.getDeviceByNameLike(keyword);
                GetDevices(request, deviceByNameLikes);
            }else if("devicecode".equals(fieldname)){
                //通过设备代码查询
                PageHelper.startPage(pageNum,pageSize);
                List<Device> deviceByCodeLikes = deviceService.getDeviceByCodeLike(keyword);
                GetDevices(request,deviceByCodeLikes);
            }
            // 回传给页面显示查询条件，否则会清空
            request.setAttribute("fieldname", fieldname);
            request.setAttribute("keyword", keyword);
            return "devicelist";
        }
        //无条件查询
        PageHelper.startPage(pageNum,pageSize);
        List<Device> deviceList = deviceService.queryDeviceList();
        GetDevices(request, deviceList);
        return "devicelist";
    }

    private void GetDevices(HttpServletRequest request, List<Device> deviceByNameLikes) {
        PageInfo pageInfo = new PageInfo(deviceByNameLikes);
        List<Device> devices = pageInfo.getList();
        if(pageInfo.getPageSize()!=0){
            int totalPages = (int) ((pageInfo.getTotal()-1)/pageInfo.getPageSize()+1);
            request.setAttribute("total", totalPages);// 计算总页数
            request.setAttribute("currentPage", pageInfo.getPageNum());
        }else{
            //说明为空
            request.setAttribute("total", 1);// 计算总页数
            request.setAttribute("currentPage",1);
        }
        request.setAttribute("devices", devices);
    }

    @RequestMapping("/toadd")
    public String toAddDevice(HttpServletRequest request){
        GetDeviceTypeMap(request);
        return "deviceadd";
    }

    @RequestMapping("/add")
    public void addDevice(HttpServletRequest request, HttpServletResponse response) throws FileUploadException, IOException, ServletException {
        // 创建fileItemFactory工厂
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 创建实现文件上传的组件
        ServletFileUpload upload = new ServletFileUpload(factory);
        // 解决文件的中文乱码
        upload.setHeaderEncoding("ISO-8859-1");
        ArrayList<FileItem> items = null;
        Device device = new Device();
        // 应用parseRequest获取全部表单项，有文件域和普通表单域，都当成FileItem对象
        items = (ArrayList<FileItem>) upload.parseRequest(request);
        Iterator<FileItem> it = items.iterator();
        while (it.hasNext()) {
            FileItem tempitem = it.next();// 取得表单中的一个元素，即表单项
            String itemName = tempitem.getFieldName();// 取得input标签的name属性值
            // isFormField()为true，则是普通表单域
            if (tempitem.isFormField()) {
                // 提到表单项文本内容，并进行中文乱码处理
                String content = CodeExchange.chinese(tempitem.getString());
                // 当前为isFormField()为true，则是普通表单域，把表单项内容放到JavaBean中存储。
                // Device device=new Device();
                device.setAddtime(new Timestamp((new Date()).getTime()));
                if (itemName.equals("dtid")) {
                    device.setDtid(Integer.parseInt(content));
                }
                if (itemName.equals("devicename")) {
                    device.setDevicename(content);
                }
                if (itemName.equals("devicecode")) {
                    device.setDevicecode(content);
                }
            }
            // 当前为isFormField()为false,判断表单域是文件域
            else {
                // type是file，上传的文件
                // 上传文件保存到服务器的路径
                if (tempitem.getName() != null & !"".equals(tempitem.getName())) {
                    String newFileName = device.getDevicecode()+"-"+UUID.randomUUID()+'.'+(CodeExchange.chinese(tempitem.getName()).split("\\.")[1]);
                    File tempfile = new File(request.getSession().getServletContext().getRealPath("/")
                            + "view/static/images/"
                            + new File(newFileName));
                    try {
                        tempitem.write(tempfile);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    // 上传的文件存储到JavaBean中
                    // tempfile.getCanonicalPath()，获取当前文件的绝对路径
                    // lastIndexOf(String str):
                    // 返回指定字符在此字符串中最后一次出现处的索引，如果此字符串中没有这样的字符，则返回 -1
                    // substring(start,stop)，用于提取字符串中介于两个指定下标之间的字符。
                    device.setDevicephoto("../view/static/images/"
                            + newFileName);
                } else
                    device.setDevicephoto("../view/static/images/no-photo.gif");
            }
        }
        int i = deviceService.addDevice(device);
        if(i>0){
            request.setAttribute("msg", "添加成功！");
        } else {
            request.setAttribute("msg", "添加失败！");
        }
        request.getRequestDispatcher("/device/alldevice").forward(request,response);
    }

    @RequestMapping("/delete")
    public void deleteDevice(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        int i = deviceService.deleteDevice(Integer.parseInt(id));
        if(i>0){
            request.setAttribute("msg", "删除成功！");
        } else {
            request.setAttribute("msg", "删除失败！");
        }
        request.getRequestDispatcher("/device/alldevice").forward(request,response);
    }

    @RequestMapping("/toupdate")
    public String toUpdateDevice(HttpServletRequest request){
        String idStr = request.getParameter("id");
        Device device = deviceService.queryDeviceById(Integer.parseInt(idStr));
        GetDeviceTypeMap(request);
        request.setAttribute("device", device);
        return "deviceedit";
    }

    @RequestMapping("/update")
    public void updateDevice(HttpServletRequest request,HttpServletResponse response) throws IOException, FileUploadException, ServletException {
        // 创建fileItemFactory工厂
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 创建实现文件上传的组件
        ServletFileUpload upload = new ServletFileUpload(factory);
        // 解决文件的中文乱码
        upload.setHeaderEncoding("ISO-8859-1");
        ArrayList<FileItem> items = null;
        Device device = new Device();
        // 应用parseRequest获取全部表单项，有文件域和普通表单域，都当成FileItem对象
        items = (ArrayList<FileItem>) upload.parseRequest(request);
        Iterator<FileItem> it = items.iterator();
        while (it.hasNext()) {
            FileItem tempitem = it.next();// 取得表单中的一个元素，即表单项
            String itemName = tempitem.getFieldName();// 取得input标签的name属性值
            // isFormField()为true，则是普通表单域
            if (tempitem.isFormField()) {
                // 提到表单项文本内容，并进行中文乱码处理
                String content = CodeExchange.chinese(tempitem.getString());
                // 当前为isFormField()为true，则是普通表单域，把表单项内容放到JavaBean中存储。
                // Device device=new Device();
                device.setAddtime(new Timestamp((new Date()).getTime()));
                if (itemName.equals("id")) {
                    device.setId(Integer.parseInt(content));
                }
                if (itemName.equals("dtid")) {
                    device.setDtid(Integer.parseInt(content));
                }
                if (itemName.equals("devicename")) {
                    device.setDevicename(content);
                }
                if (itemName.equals("devicecode")) {
                    device.setDevicecode(content);
                }
            }
            // 当前为isFormField()为false,判断表单域是文件域
            else {
                // type是file，上传的文件
                // 上传文件保存到服务器的路径
                if (tempitem.getName() != null & !"".equals(tempitem.getName())) {
                    String newFileName = device.getDevicecode()+"-"+UUID.randomUUID()+'.'+(CodeExchange.chinese(tempitem.getName()).split("\\.")[1]);
                    File tempfile = new File(request.getSession().getServletContext().getRealPath("/")
                            + "view\\static\\images\\"
                            + new File(newFileName));
                    try {
                        tempitem.write(tempfile);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    // 上传的文件存储到JavaBean中
                    // tempfile.getCanonicalPath()，获取当前文件的绝对路径
                    // lastIndexOf(String str):
                    // 返回指定字符在此字符串中最后一次出现处的索引，如果此字符串中没有这样的字符，则返回 -1
                    // substring(start,stop)，用于提取字符串中介于两个指定下标之间的字符。
                    device.setDevicephoto("../view/static/images/"
                            + tempfile.getCanonicalPath().substring(
                            tempfile.getCanonicalPath()
                                    .lastIndexOf("\\") + 1));
                } else
                    device.setDevicephoto("../view/static/images/no-photo.gif");
            }
        }
        System.out.println(device);
        int i = deviceService.updateDevice(device);
        if(i>0){
            request.setAttribute("msg", "修改成功！");
        } else {
            request.setAttribute("msg", "修改失败！");
        }
        request.getRequestDispatcher("/device/alldevice").forward(request,response);
    }

    private void GetDeviceTypeMap(HttpServletRequest request) {
        List<DeviceType> deviceTypeList = deviceTypeService.queryDeviceTypeList();
        Map<Integer,String> map = new HashMap<>();
        for(DeviceType deviceType:deviceTypeList){
            map.put(deviceType.getId(),deviceType.getDevicetypename());
        }
        request.setAttribute("deviceTypeMap",map);
    }

    @RequestMapping("/deviceExcel")
    public void deviceExcel(HttpServletResponse response,HttpServletRequest request){
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            Date date = Calendar.getInstance().getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String formatDate = sdf.format(date);
            // 设置文件名
            String fileName = URLEncoder.encode(formatDate +"设备型号", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            String sheetName = "数据展示";
            // 按条件筛选records
            List<Device> list = deviceService.queryDeviceList();
            // easyexcel工具类实现Excel文件导出
            EasyExcel.write(response.getOutputStream(), Device.class).sheet(sheetName).doWrite(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
