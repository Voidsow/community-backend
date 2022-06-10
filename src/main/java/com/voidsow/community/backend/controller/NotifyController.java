package com.voidsow.community.backend.controller;

import com.voidsow.community.backend.annotation.LoginRequire;
import com.voidsow.community.backend.dto.Result;
import com.voidsow.community.backend.entity.User;
import com.voidsow.community.backend.service.NotifyService;
import com.voidsow.community.backend.service.UserService;
import com.voidsow.community.backend.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/notification")
public class NotifyController {
    NotifyService notifyService;
    UserService userService;
    HostHolder hostHolder;

    @Autowired
    public NotifyController(NotifyService notifyService, UserService userService, HostHolder hostHolder) {
        this.notifyService = notifyService;
        this.userService = userService;
        this.hostHolder = hostHolder;
    }

    @LoginRequire
    @GetMapping("/follow")
    public Result notifyFollow() {
        int uid = hostHolder.user.get().getId();
        List<User> users = new ArrayList<>();
        List<Date> times = new ArrayList<>();
        notifyService.getFollowNotifications(uid).forEach(notification -> {
            users.add(userService.findById(notification.getSourceUid()));
            times.add(notification.getTime());
        });
        Map<String, Object> map = new HashMap<>();
        map.put("users", users);
        map.put("times", times);
        return Result.getSuccess(map);
    }

    @LoginRequire
    @GetMapping("/like")
    public Result notifyLike() {
        int uid = hostHolder.user.get().getId();
        return Result.getSuccess(notifyService.getLikeNotifications(uid));
    }

    @LoginRequire
    @GetMapping("/comment")
    public Result notifyComment() {
        int uid = hostHolder.user.get().getId();
        return Result.getSuccess(notifyService.getCommentNotifications(uid));
    }

}