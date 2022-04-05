package com.gong.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        //登陆页面放行
        if (uri.contains("tologin")) {
            return true;
        }
        //提交登录也放行
        if (uri.contains("login")) {
            return true;
        }
        if (uri.contains("toregister")) {
            return true;
        }
        //提交登录也放行
        if (uri.contains("register")) {
            return true;
        }

        if (uri.contains("mail")) {
            return true;
        }

        if (uri.contains("usernameexist")) {
            return true;
        }
        if (uri.contains("toforget")) {
            return true;
        }
        if (uri.contains("forget")) {
            return true;
        }
        if (uri.contains("task")) {
            return true;
        }
        //用户名不为空，放行
        String loginusername = (String) request.getSession().getAttribute("loginusername");
        if(loginusername!=null){
            return true;
        }
        request.getRequestDispatcher("/view/jsp/login.jsp").forward(request,response);
        return false;
    }
}
