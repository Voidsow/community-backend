package com.voidsow.community.backend.interceptor;

import com.voidsow.community.backend.service.UserService;
import com.voidsow.community.backend.utils.Authorizer;
import com.voidsow.community.backend.utils.HostHolder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;

@Component
public class UserInterceptor implements HandlerInterceptor {
    static Logger logger = LoggerFactory.getLogger(UserInterceptor.class);
    Key key;
    HostHolder hostHolder;
    Authorizer authorizer;
    UserService userService;

    @Autowired
    public UserInterceptor(Key key, HostHolder hostHolder, Authorizer authorizer, UserService userService) {
        this.key = key;
        this.hostHolder = hostHolder;
        this.authorizer = authorizer;
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if (cookies != null)
            for (var cookie : cookies)
                if (cookie.getName().equals("token")) {
                    logger.debug(String.format("token%s", cookie.getValue()));
                    try {
                        Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(cookie.getValue());
                        hostHolder.user.set(userService.findById(Integer.parseInt(claimsJws.getBody().getAudience())));
                    } catch (Exception e) {
                        cookie.setMaxAge(0);
                        response.addCookie(cookie);
                    }
                }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null && hostHolder.user.get() != null)
            modelAndView.addObject("user", hostHolder.user.get());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.user.remove();
    }
}
