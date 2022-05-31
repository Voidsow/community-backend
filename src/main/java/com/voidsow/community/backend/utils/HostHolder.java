package com.voidsow.community.backend.utils;

import com.voidsow.community.backend.entity.User;
import org.springframework.stereotype.Component;

@Component
public class HostHolder {
    public ThreadLocal<User> user = new ThreadLocal<>();
}
