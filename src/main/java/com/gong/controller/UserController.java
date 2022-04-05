package com.gong.controller;

import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gong.pojo.User;
import com.gong.service.UserService;
import com.gong.utils.MD5;
import com.gong.utils.SendMailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    String yzmSystem;

    @Autowired
    private UserService userService;

    @RequestMapping("/tologin")
    public String tologin() {
        return "login";
    }

    @RequestMapping("/login")
    public String login(User user, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String username = user.getUsername();

        User u = userService.queryUserByUserName(username);

        if (u == null || !MD5.getInstance().getMD5(user.getPassword()).equals(u.getPassword())) {
            request.setAttribute("msg", "登录失败！");
            return "login";
        }
        HttpSession session = request.getSession();
        // 用户登录信息存到 session
        session.setAttribute("loginusername", u.getUsername());
        session.setAttribute("loginusertype", u.getUsertype());
        // 添加Cookie
        Cookie cookie = new Cookie("SESSIONID", session.getId());// 创建一个键值对的cookie对象
        cookie.setMaxAge(60 * 60 * 24 * 7);// 设置cookie的生命周期
        response.addCookie(cookie);// 添加到response中
        return "index";
    }

    @RequestMapping("/toregister")
    public String toregister() {
        return "register";
    }

    @RequestMapping("/register")
    public void register(User user, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = response.getWriter();
        String yzm = request.getParameter("yzm");
        if (!yzmSystem.equals(yzm)) {
            out.write("2");
            return;
        }
        user.setUsertype("普通用户");
        int i = userService.addUser(user);
        if (i > 0) {
            out.write("1");
            HttpSession session = request.getSession();
            // 用户登录信息存到 session
            session.setAttribute("loginusername", user.getUsername());
            session.setAttribute("loginusertype", user.getUsertype());
            // 添加Cookie
            Cookie cookie = new Cookie("SESSIONID", session.getId());// 创建一个键值对的cookie对象
            cookie.setMaxAge(60 * 60 * 24 * 7);// 设置cookie的生命周期
            response.addCookie(cookie);// 添加到response中
        } else {
            out.write("0");
        }
    }

    @RequestMapping("/toforget")
    public String toforget() {
        return "forget";
    }

    @RequestMapping("/forget")
    public void forget(User user, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = response.getWriter();
        String yzm = request.getParameter("yzm");
        if (!yzmSystem.equals(yzm)) {
            out.write("2");
            return;
        }
        int i = userService.updatePassword(user);
        if (i > 0) {
            out.write("1"); //修改密码成功
        } else {
            out.write("0");
        }
    }

    @RequestMapping("/mailexist")
    public void mailExist(User user, HttpServletResponse response) throws IOException {
        User u = userService.mailExist(user.getMail());
        PrintWriter out = response.getWriter();
        if (u == null) {
            out.write("1");   //邮箱不存在
        } else {
            out.write("0");   //邮箱存在
        }
    }

    @RequestMapping("/sendmailregister")
    public void sendMailRegister(HttpServletResponse response, HttpServletRequest request) throws Exception {
        yzmSystem = SendMailUtil.randomBiocuration();
        SimpleDateFormat sdf = new SimpleDateFormat();
        String time = sdf.format(new Date());
        String mail = request.getParameter("mail");
        String emailSubject = "苹果质量安全监管物联网平台-注册";
        String emailContent = "欢迎注册，您的验证码为:" + yzmSystem + "\n时间为:" + time;
        SendMailUtil.sendMail(mail, emailContent, emailSubject);
        PrintWriter out = response.getWriter();
        out.write("1");
    }

    @RequestMapping("/sendmailforget")
    public void sendMailForget(HttpServletResponse response, HttpServletRequest request) throws Exception {
        yzmSystem = SendMailUtil.randomBiocuration();
        SimpleDateFormat sdf = new SimpleDateFormat();
        String time = sdf.format(new Date());
        String mail = request.getParameter("mail");
        String emailSubject = "苹果质量安全监管物联网平台-修改密码";
        String emailContent = "亲，您的验证码为:" + yzmSystem + "，请妥善保管哟！\n时间为:" + time;
        SendMailUtil.sendMail(mail, emailContent, emailSubject);
        PrintWriter out = response.getWriter();
        out.write("1");
    }

    @RequestMapping("/usernameexist")
    public void usernameExist(HttpServletResponse response, HttpServletRequest request) throws IOException {
        String username = request.getParameter("username");
        User user = userService.queryUserByUserName(username);
        PrintWriter out = response.getWriter();
        if (user == null) {
            out.write("1");//用户名不存在
        } else {
            out.write("0"); //用户名存在
        }
    }

    @RequestMapping("/index")
    public String toindex() {
        return "index";
    }

    // 查找用户
    @RequestMapping("/alluser")
    public String queryUserList(HttpServletRequest request) {
        // 查询条件
        String keyword = request.getParameter("keyword");
        String keyword_group = request.getParameter("keyword-group");
        String fieldname = request.getParameter("fieldname");
        String page = request.getParameter("page");
        if (page == null) {
            page = "1";
        }
        int pageNum = Integer.parseInt(page);
        int pageSize = 5;
        // 根据用户类型查询  && (keyword == null||"".equals(keyword))
        if ("usertype".equals(fieldname)) {
            if (keyword == null || "".equals(keyword)) {
                keyword = keyword_group;
            }
            PageHelper.startPage(pageNum, pageSize);
            List<User> userList = userService.queryUserByUserType(keyword);
            getUsers(request, userList);
            request.setAttribute("fieldname", fieldname);
            request.setAttribute("keyword", keyword);
            return "userlist";
        }
        if (keyword != null && fieldname != null && fieldname.length() > 0
                && keyword.length() > 0) {
            // 根据用户id查询
            if (fieldname.equals("id")) {
                int id = Integer.parseInt(keyword);
                User user = userService.queryUserById(id);
                List<User> users = new ArrayList<>();
                users.add(user);
                request.setAttribute("users", users);
                request.setAttribute("total", 1);// 计算总页数
                request.setAttribute("currentPage", 1);
            }
            // 根据用户名模糊查询
            else if (fieldname.equals("username")) {
                PageHelper.startPage(pageNum, pageSize);
                List<User> userList = userService.getUserLike(keyword);
                getUsers(request, userList);
            }
            // 回传给页面显示查询条件，否则会清空
            request.setAttribute("fieldname", fieldname);
            request.setAttribute("keyword", keyword);
            return "userlist";
        }
        // 不按照条件查询，查询全部用户
        PageHelper.startPage(pageNum, pageSize);
        List<User> userList = userService.queryUserList();
        getUsers(request, userList);
        return "userlist";
    }

    private void getUsers(HttpServletRequest request, List<User> userList) {
        PageInfo pageInfo = new PageInfo(userList);
        List<User> users = pageInfo.getList();
        request.setAttribute("users", users);
        int totalPages = (int) ((pageInfo.getTotal() - 1) / pageInfo.getPageSize() + 1);
        request.setAttribute("total", totalPages);// 计算总页数
        request.setAttribute("currentPage", pageInfo.getPageNum());
    }

    @RequestMapping("/toadd")
    public String toAddUser() {
        return "useradd";
    }

    @RequestMapping("/add")
    public void addUser(User user, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int i = userService.addUser(user);
        if (i > 0) {
            request.setAttribute("msg", "添加成功！");
        } else {
            request.setAttribute("msg", "添加失败！");
        }
        request.getRequestDispatcher("/user/alluser").forward(request, response);
    }

    @RequestMapping("/delete")
    public void deleteUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");
        int i = userService.deleteUser(Integer.parseInt(idStr));
        if (i > 0) {
            request.setAttribute("msg", "删除成功！");
        } else {
            request.setAttribute("msg", "删除失败！");
        }
        request.getRequestDispatcher("/user/alluser").forward(request, response);
    }

    @RequestMapping("/toupdate")
    public String toUpdateUser(HttpServletRequest request) {
        String idStr = request.getParameter("id");
        User user = userService.queryUserById(Integer.parseInt(idStr));
        request.setAttribute("user", user);
        return "useredit";
    }

    @RequestMapping("/update")
    public void updateUser(User user, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User u = userService.queryUserByUserName(user.getUsername());
        PrintWriter out = response.getWriter();
        System.out.println(user.toString());
        if (u == null) {
            int i = userService.updateUser(user);
            if (i > 0) {
                request.setAttribute("msg", "修改成功！");
            } else {
                request.setAttribute("msg", "修改失败！");
            }
            out.write("1");
        } else {
            out.write("0");
        }
    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.removeAttribute("loginusername");
        session.removeAttribute("loginusertype");
        // 获取Cookies数组
        Cookie[] cookies = request.getCookies();
        // 迭代查找并清除Cookie
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("SESSIONID")) {
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
        request.setAttribute("msg", "您已成功退出登录！");
        return "login";
    }

    @RequestMapping("/userExcel")
    public void userExcel(HttpServletResponse response, HttpServletRequest request) {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            Date date = Calendar.getInstance().getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String formatDate = sdf.format(date);
            // 设置文件名
            String fileName = URLEncoder.encode(formatDate + "用户", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            String sheetName = "数据展示";
            // 按条件筛选records
            List<User> list = userService.queryUserList();
            // easyexcel工具类实现Excel文件导出
            EasyExcel.write(response.getOutputStream(), User.class).sheet(sheetName).doWrite(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
