package com.voidsow.community.backend.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voidsow.community.backend.annotation.LoginRequire;
import com.voidsow.community.backend.dto.Result;
import com.voidsow.community.backend.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.voidsow.community.backend.constant.Constant.UNAUTHORIZED;

@Component
public class LoginRequireInterceptor implements HandlerInterceptor {
    private final HostHolder hostHolder;
    ObjectMapper objectMapper;

    @Autowired
    public LoginRequireInterceptor(HostHolder hostHolder, ObjectMapper objectMapper) {
        this.hostHolder = hostHolder;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            LoginRequire loginRequire = handlerMethod.getMethod().getAnnotation(LoginRequire.class);
            if (loginRequire != null && hostHolder.user.get() == null) {
                //拦截器抛出的异常不会传给controllerAdvice处理
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getWriter().write(objectMapper.writeValueAsString(
                        new Result(UNAUTHORIZED, "unauthorized,login required", null)));
                return false;
            }
        }
        return true;
    }
}
