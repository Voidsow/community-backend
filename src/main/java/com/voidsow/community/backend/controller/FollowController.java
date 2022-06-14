package com.voidsow.community.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.voidsow.community.backend.annotation.LoginRequire;
import com.voidsow.community.backend.dto.Result;
import com.voidsow.community.backend.dto.UserDTO;
import com.voidsow.community.backend.entity.Event;
import com.voidsow.community.backend.entity.User;
import com.voidsow.community.backend.event.EventProducer;
import com.voidsow.community.backend.service.FollowService;
import com.voidsow.community.backend.service.UserService;
import com.voidsow.community.backend.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.voidsow.community.backend.constant.Constant.ILLEGAL;
import static com.voidsow.community.backend.constant.Constant.TOPIC_FOLLOW;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class FollowController {
    HostHolder hostHolder;
    UserService userService;
    FollowService followService;
    EventProducer eventProducer;

    @Value("${community.page.size}")
    private int pageSize;

    @Value("${community.page.num}")
    private int pageNum;

    @Autowired
    public FollowController(UserService userService, FollowService followService, HostHolder hostHolder, EventProducer eventProducer) {
        this.followService = followService;
        this.userService = userService;
        this.hostHolder = hostHolder;
        this.eventProducer = eventProducer;
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
    @LoginRequire
    @PostMapping(value = "/follow/{uid}")
    @ResponseBody
    public Result follow(@PathVariable("uid") int followeeId) throws JsonProcessingException {
        int userId = hostHolder.user.get().getId();
        if (userService.findById(followeeId) == null)
            return new Result(ILLEGAL, "关注的用户不存在", null);
        boolean followStatus = followService.followOrNot(userId, followeeId);
        //发送关注通知
        if (followStatus) {
            Event event = new Event().setTopic(TOPIC_FOLLOW).
                    setSourceUid(userId).
                    setToUid(followeeId).setTime(new Date());
            eventProducer.fireEvent(event);
        }
        return Result.getSuccess(followStatus);
    }
}
