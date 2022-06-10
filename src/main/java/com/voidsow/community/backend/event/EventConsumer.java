package com.voidsow.community.backend.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voidsow.community.backend.entity.Event;
import com.voidsow.community.backend.entity.Notification;
import com.voidsow.community.backend.mapper.CommentMapper;
import com.voidsow.community.backend.mapper.NotificationMapper;
import com.voidsow.community.backend.mapper.PostMapper;
import com.voidsow.community.backend.service.ChatService;
import com.voidsow.community.backend.service.NotifyService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

import static com.voidsow.community.backend.constant.Constant.*;

@Component
public class EventConsumer {
    ChatService chatService;
    ObjectMapper objectMapper;
    NotifyService notifyService;
    NotificationMapper notificationMapper;
    PostMapper postMapper;
    CommentMapper commentMapper;

    @Autowired
    public EventConsumer(ObjectMapper objectMapper, ChatService chatService, NotifyService notifyService, NotificationMapper notificationMapper, PostMapper postMapper,
                         CommentMapper commentMapper) {
        this.objectMapper = objectMapper;
        this.chatService = chatService;
        this.notifyService = notifyService;
        this.notificationMapper = notificationMapper;
        this.postMapper = postMapper;
        this.commentMapper = commentMapper;
    }

    @RabbitListener(queues = {"comment"})
    public void consumeComment(Message message) throws IOException {
        Event event = objectMapper.readValue(message.getBody(), Event.class);
        notificationMapper.insert(new Notification(null, COMMENT, event.getSourceUid(), event.getToUid(),
                (Integer) event.getProperty("id"), String.valueOf(event.getProperty("type")), UNREAD, event.getTime()));
    }

    @RabbitListener(queues = {"follow"})
    public void consumeFollow(Message message) throws IOException {
        Event event = objectMapper.readValue(message.getBody(), Event.class);
        List<Notification> notifications = notificationMapper.select(FOLLOW, event.getSourceUid(), event.getToUid(), null, null);
        //如果不存在则直接创建
        if (notifications == null || notifications.isEmpty()) {
            notificationMapper.insert(new Notification(FOLLOW, event.getSourceUid(), event.getToUid(), UNREAD, event.getTime()));
        } else {
            //只有点赞和关注需要更新
            notificationMapper.updateTime(FOLLOW, event.getSourceUid(), event.getToUid(), event.getTime());
        }
    }

    @RabbitListener(queues = {"like"})
    public void consumeLike(Message message) throws IOException {
        Event event = objectMapper.readValue(message.getBody(), Event.class);
        String props = String.valueOf(event.getProperty("type"));
        Integer entityId = (Integer) event.getProperty("id");
        List<Notification> notifications = notificationMapper.select(LIKE, event.getSourceUid(), event.getToUid(), entityId, props);
        //如果不存在则直接创建
        if (notifications.isEmpty()) {
            if (event.getProperty("type").equals(LIKE_POST))
                event.setToUid(postMapper.selectByPrimaryKey(entityId).getUid());
            else
                event.setToUid(commentMapper.selectByPrimaryKey(entityId).getUid());
            notificationMapper.insert(new Notification(null, LIKE, event.getSourceUid(), event.getToUid(), entityId,
                    props, UNREAD, event.getTime()));
        } else {
            //只有点赞和关注需要更新
            notificationMapper.updateTimeById(notifications.get(0).getId(), event.getTime());
        }
    }
}
