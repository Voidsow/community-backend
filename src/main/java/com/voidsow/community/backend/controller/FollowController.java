package com.voidsow.community.backend.controller;

import com.voidsow.community.backend.dto.Follow;
import com.voidsow.community.backend.dto.Result;
import com.voidsow.community.backend.dto.UserDTO;
import com.voidsow.community.backend.entity.User;
import com.voidsow.community.backend.exception.ResourceNotFoundException;
import com.voidsow.community.backend.service.FollowService;
import com.voidsow.community.backend.service.UserService;
import com.voidsow.community.backend.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.voidsow.community.backend.constant.Constant.ILLEGAL;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class FollowController {
    HostHolder hostHolder;
    UserService userService;
    FollowService followService;

    @Value("${community.page.size}")
    private int pageSize;

    @Value("${community.page.num}")
    private int pageNum;

    @Autowired
    public FollowController(UserService userService, FollowService followService, HostHolder hostHolder) {
        this.followService = followService;
        this.userService = userService;
        this.hostHolder = hostHolder;
    }

    @GetMapping("/user/{id}/{type}")
    @ResponseBody
    public Result follow(@PathVariable("id") int id, @PathVariable("type") String type,
                         int page, int pageSize) {
        User user = hostHolder.user.get();
        User observed = userService.findById(id);
        if (observed == null)
            return Result.resourceNotFound();
        Map<String, Object> map = new HashMap<>();
        List<User> users;
        if (type.equals("follower")) {
            map.put("num", followService.countFollower(observed.getId()));
            users = followService.findFollowers(observed.getId(), (page - 1) * pageSize, pageSize);
        } else if (type.equals("followee")) {
            map.put("num", followService.countFollowee(observed.getId()));
            users = followService.findFollowees(observed.getId(), (page - 1) * pageSize, pageSize);
        } else
            return Result.notSupport();
        List<UserDTO> userDTOS = new ArrayList<>();
        users.forEach(u -> userDTOS.add(userService.findByIdToDTO(u, user)));
        map.put("users", userDTOS);
        return Result.getSuccess(map);
    }

    //必须由服务器检验关注情况
    @PostMapping(value = "/follow/{uid}")
    @ResponseBody
    public Result follow(@PathVariable("uid") int followeeId) {
        int userId = hostHolder.user.get().getId();
        if (userService.findById(followeeId) == null)
            return new Result(ILLEGAL, "关注的用户不存在", null);
        return Result.getSuccess(followService.followOrNot(userId, followeeId));
    }
}
