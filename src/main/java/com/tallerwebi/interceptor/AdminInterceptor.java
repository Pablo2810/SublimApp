package com.tallerwebi.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        Object rolUsuario = session.getAttribute("ROL_USUARIO");

        if (rolUsuario == null || !rolUsuario.equals("ADMIN")) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        return true;
    }

}
