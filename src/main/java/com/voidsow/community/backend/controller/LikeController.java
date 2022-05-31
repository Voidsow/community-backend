package com.voidsow.community.backend.controller;

import com.voidsow.community.backend.dto.Like;
import com.voidsow.community.backend.dto.Result;
import com.voidsow.community.backend.entity.User;
import com.voidsow.community.backend.service.LikeService;
import com.voidsow.community.backend.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/like")
public class LikeController {
    LikeService likeService;
    HostHolder hostHolder;

    @Autowired
    public LikeController(LikeService likeService, HostHolder hostHolder) {
        this.likeService = likeService;
        this.hostHolder = hostHolder;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Result like(@RequestBody Like like) {
        User user = hostHolder.user.get();
        boolean status = likeService.likeOrNot(like.getType(), like.getId(), user.getId());
        long likeNum = likeService.likeNum(like.getType(), like.getId());
        Map<String, Object> result = new HashMap<>();
        result.put("num", likeNum);
        result.put("like", status);
        return Result.getSuccess(result);
    }
}
