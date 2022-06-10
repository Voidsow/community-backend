package com.voidsow.community.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.voidsow.community.backend.dto.Like;
import com.voidsow.community.backend.dto.Result;
import com.voidsow.community.backend.entity.Event;
import com.voidsow.community.backend.entity.User;
import com.voidsow.community.backend.event.EventProducer;
import com.voidsow.community.backend.service.LikeService;
import com.voidsow.community.backend.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.voidsow.community.backend.constant.Constant.TOPIC_LIKE;

@RestController
@RequestMapping("/like")
public class LikeController {
    LikeService likeService;
    HostHolder hostHolder;
    EventProducer eventProducer;

    @Autowired
    public LikeController(LikeService likeService, EventProducer eventProducer, HostHolder hostHolder) {
        this.likeService = likeService;
        this.hostHolder = hostHolder;
        this.eventProducer = eventProducer;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Result like(@RequestBody Like like) throws JsonProcessingException {
        User user = hostHolder.user.get();
        boolean status = likeService.likeOrNot(like.getType(), like.getId(), user.getId());
        long likeNum = likeService.likeNum(like.getType(), like.getId());

        //发送点赞消息。通知接收者的uid延迟到消费者端查询以加快响应请求速度
        if (status) {
            Event event = new Event().setTopic(TOPIC_LIKE).setSourceUid(user.getId()).setTime(new Date()).
                    addProperty("type", like.getType()).addProperty("id", like.getId());
            eventProducer.fireEvent(event);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("num", likeNum);
        result.put("like", status);
        return Result.getSuccess(result);
    }
}
