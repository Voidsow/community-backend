package com.voidsow.community.backend.interceptor;

import com.voidsow.community.backend.annotation.TokenRequire;
import com.voidsow.community.backend.entity.User;
import com.voidsow.community.backend.exception.IllegalAccessException;
import com.voidsow.community.backend.service.UserService;
import com.voidsow.community.backend.utils.Authorizer;
import com.voidsow.community.backend.utils.HostHolder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;

@Component
public class UserInterceptor implements HandlerInterceptor {
    static Logger logger = LoggerFactory.getLogger(UserInterceptor.class);
    Key key;
    StringRedisTemplate strRedisTemplate;
    HostHolder hostHolder;
    Authorizer authorizer;

    @Autowired
    public UserInterceptor(Key key, StringRedisTemplate strRedisTemplate, HostHolder hostHolder, Authorizer authorizer) {
        this.key = key;
        this.strRedisTemplate = strRedisTemplate;
        this.hostHolder = hostHolder;
        this.authorizer = authorizer;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String header = request.getHeader("Authorization");
        String[] auth = header != null ? header.split(" ") : null;
        if (auth != null && auth.length > 1) {
            String tokenStr = auth[1];
            logger.debug(String.format("token%s", tokenStr));
            try {
                Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(tokenStr);
                String jti = claimsJws.getBody().getId();
                //检查是否在黑名单里
                if (strRedisTemplate.opsForValue().get("blacklist:" + jti) != null)
                    throw new IllegalAccessException();
                if (((HandlerMethod) handler).getMethod().getAnnotation(TokenRequire.class) != null)
                    request.setAttribute("token", claimsJws.getBody());
                User user = new User();
                user.setId(Integer.parseInt(claimsJws.getBody().getAudience()));
                hostHolder.user.set(user);
            } catch (Exception ignored) {
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
