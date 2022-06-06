package com.voidsow.community.backend.controller;

import com.voidsow.community.backend.dto.Result;
import com.voidsow.community.backend.entity.User;
import com.voidsow.community.backend.service.PostService;
import com.voidsow.community.backend.service.UserService;
import com.voidsow.community.backend.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class IndexController {
    PostService postService;
    UserService userService;
    HostHolder hostHolder;

    @Autowired
    public IndexController(PostService postService, UserService userService, HostHolder hostHolder) {
        this.postService = postService;
        this.userService = userService;
        this.hostHolder = hostHolder;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Result hello(int page, int size) {
        User user = hostHolder.user.get();
        return new Result(200, "ok", postService.getPosts(user != null ? user.getId() : null, page, size));
    }

    @GetMapping("/error")
    public String error() {
        return "error/500";
    }
}
