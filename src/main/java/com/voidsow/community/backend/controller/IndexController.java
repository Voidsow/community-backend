package com.voidsow.community.backend.controller;

import com.voidsow.community.backend.dto.Result;
import com.voidsow.community.backend.service.PostService;
import com.voidsow.community.backend.service.UserService;
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

    @Autowired
    public IndexController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Result hello(int page, int size) {
        return new Result(200, "ok", postService.getPosts(null, page, size));
    }

}
