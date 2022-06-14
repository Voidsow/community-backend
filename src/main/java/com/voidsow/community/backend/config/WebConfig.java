package com.voidsow.community.backend.config;

import com.voidsow.community.backend.interceptor.LoginRequireInterceptor;
import com.voidsow.community.backend.interceptor.UserInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    UserInterceptor userInterceptor;
    LoginRequireInterceptor loginRequireInterceptor;

    public WebConfig(UserInterceptor userInterceptor, LoginRequireInterceptor loginRequireInterceptor) {
        this.userInterceptor = userInterceptor;
        this.loginRequireInterceptor = loginRequireInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInterceptor).addPathPatterns("/**");
        registry.addInterceptor(loginRequireInterceptor).addPathPatterns("/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("http://localhost:8080").allowCredentials(true);
    }
}
