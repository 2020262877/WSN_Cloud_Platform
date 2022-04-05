package com.gong.controller;

import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gong.pojo.Gate;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/gate")
public class GateController {

    @Autowired
    private GateService gateService;

    @RequestMapping("/allgate")
    public String queryGateList(HttpServletRequest request) {
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
                Gate gate = gateService.queryGateById(id);
                List<Gate> gates = new ArrayList<>();
                if (gate != null) {
                    gates.add(gate);
                }
                request.setAttribute("gates", gates);
                request.setAttribute("total", 1);// 计算总页数
                request.setAttribute("currentPage", 1);
            } else if ("gatename".equals(fieldname)) {
                //通过设备名称查询
                PageHelper.startPage(pageNum, pageSize);
                List<Gate> gateByNameLikes = gateService.getGateByNameLike(keyword);
                GetGate(request, gateByNameLikes);
            } else if ("gatecode".equals(fieldname)) {
                //通过设备代码查询
                PageHelper.startPage(pageNum, pageSize);
                List<Gate> gateByCodeLikes = gateService.getGateByCodeLike(keyword);
                GetGate(request, gateByCodeLikes);
            }
            // 回传给页面显示查询条件，否则会清空
            request.setAttribute("fieldname", fieldname);
            request.setAttribute("keyword", keyword);
            return "gatelist";
        }
        //无条件查询
        PageHelper.startPage(pageNum, pageSize);
        List<Gate> gateList = gateService.queryGateList();
        GetGate(request, gateList);
        return "gatelist";
    }

    private void GetGate(HttpServletRequest request, List<Gate> gateByCodeOrNameOrAllLikes) {
        PageInfo pageInfo = new PageInfo(gateByCodeOrNameOrAllLikes);
        List<Gate> gates = pageInfo.getList();
        if(pageInfo.getPageSize()!=0){
            int totalPages = (int) ((pageInfo.getTotal()-1)/pageInfo.getPageSize()+1);
            request.setAttribute("total", totalPages);// 计算总页数
            request.setAttribute("currentPage", pageInfo.getPageNum());
        }else{
            //说明为空
            request.setAttribute("total", 1);// 计算总页数
            request.setAttribute("currentPage",1);
        }
        request.setAttribute("gates", gates);
    }

    @RequestMapping("/toadd")
    public String toAddGate(HttpServletRequest request){
        return "gateadd";
    }

    @RequestMapping("/add")
    public void addGate(Gate gate, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        gate.setAddtime(new Timestamp((new Date()).getTime()));
        int i = gateService.addGate(gate);
        if(i>0){
            request.setAttribute("msg", "添加成功！");
        }else {
            request.setAttribute("msg", "添加失败！");
        }
        request.getRequestDispatcher("/gate/allgate").forward(request,response);
    }

    @RequestMapping("/delete")
    public void deleteGate(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");
        int i = gateService.deleteGate(Integer.parseInt(idStr));
        if(i>0){
            request.setAttribute("msg", "删除成功！");
        }else {
            request.setAttribute("msg", "删除失败！");
        }
        request.getRequestDispatcher("/gate/allgate").forward(request,response);
    }

    @RequestMapping("/toupdate")
    public String toUpdateGate(HttpServletRequest request){
        String idStr = request.getParameter("id");
        Gate gate = gateService.queryGateById(Integer.parseInt(idStr));
        request.setAttribute("gate", gate);
        return "gateedit";
    }

    @RequestMapping("/update")
    public void updateGate(Gate gate,HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        int i = gateService.updateGate(gate);
        if(i>0){
            request.setAttribute("msg", "修改成功！");
        }else{
            request.setAttribute("msg", "修改失败！");
        }
        request.getRequestDispatcher("/gate/allgate").forward(request,response);
    }

    @RequestMapping("/gateExcel")
    public void userExcel(HttpServletResponse response, HttpServletRequest request){
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            Date date = Calendar.getInstance().getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String formatDate = sdf.format(date);
            // 设置文件名
            String fileName = URLEncoder.encode(formatDate +"网关", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            String sheetName = "数据展示";
            // 按条件筛选records
            List<Gate> list =  gateService.queryGateList();
            // easyexcel工具类实现Excel文件导出
            EasyExcel.write(response.getOutputStream(),Gate.class).sheet(sheetName).doWrite(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
