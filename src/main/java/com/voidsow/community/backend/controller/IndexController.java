package com.voidsow.community.backend.controller;

import com.voidsow.community.backend.annotation.LoginRequire;
import com.voidsow.community.backend.dto.Result;
import com.voidsow.community.backend.service.PostService;
import com.voidsow.community.backend.service.UserService;
import com.voidsow.community.backend.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.voidsow.community.backend.constant.Constant.*;

@RestController
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @GetMapping
    public Result reccomend(int page, int size) {
        return new Result(200, "ok", postService.getPosts(null, page, size, ORDER_BY_RECCOMEND));
    }

    @GetMapping("/newest")
    public Result getByTime(int page, int size) {
        return Result.getSuccess(postService.getPosts(null, page, size, ORDER_BY_NEWEST));
    }

    @LoginRequire
    @GetMapping("/follow")
    public Result getByFollow(int page, int size) {
        return Result.getSuccess(postService.getPosts(hostHolder.user.get().getId(), page, size, ORDER_BY_FOLLOW));
    }

}
