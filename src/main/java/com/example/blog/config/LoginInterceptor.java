package com.example.blog.config;

import com.alibaba.fastjson.JSON;
import com.example.blog.common.result.Result;
import com.example.blog.common.util.JwtUtil;
import com.example.blog.common.util.UserHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    // 进入controller之前执行
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        // 1. 从请求头获取 token
        String token = request.getHeader("token");
        if (token == null || token.isEmpty()) {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(JSON.toJSONString(Result.error("请先登录")));
            return false;
        }

        // 2. 校验 token
        if (!jwtUtil.validateToken(token)) {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(JSON.toJSONString(Result.error("token无效或已过期")));
            return false;
        }

        // 3. 解析出用户ID，存入 ThreadLocal 后面用
        Long userId = jwtUtil.getUserIdFromToken(token);
        UserHolder.setUserId(userId);

        return true;
    }

    // 执行完controller之后
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) throws Exception {
        UserHolder.remove();
    }
}