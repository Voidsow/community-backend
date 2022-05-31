package com.voidsow.community.backend.utils;

import com.voidsow.community.backend.entity.User;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Component
public class Authorizer {
    Key key;

    @Autowired
    public Authorizer(Key key) {
        this.key = key;
    }

    static public String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public String generateToken(User user, int duration) {
        Calendar calendar = Calendar.getInstance();
        Date curTime = calendar.getTime();
        calendar.add(Calendar.SECOND, duration);

        return Jwts.builder().setHeaderParam(Header.TYPE, Header.JWT_TYPE).
                setIssuedAt(curTime).setExpiration(calendar.getTime()).
                setAudience(user.getId().toString()).
                setId(generateUUID()).signWith(key).compact();
    }
}
