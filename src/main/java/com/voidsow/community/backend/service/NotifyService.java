package com.voidsow.community.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voidsow.community.backend.dto.notification.CommentNotifyDTO;
import com.voidsow.community.backend.dto.notification.LikeDTO;
import com.voidsow.community.backend.entity.Comment;
import com.voidsow.community.backend.entity.Notification;
import com.voidsow.community.backend.entity.Post;
import com.voidsow.community.backend.mapper.NotificationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.voidsow.community.backend.constant.Constant.*;

@Component
public class NotifyService {
    ObjectMapper objectMapper;
    NotificationMapper notificationMapper;
    PostService postService;
    CommentService commentService;
    UserService userService;

    @Autowired
    public NotifyService(NotificationMapper notificationMapper, ObjectMapper objectMapper, PostService postService, CommentService commentService, UserService userService) {
        this.notificationMapper = notificationMapper;
        this.objectMapper = objectMapper;
        this.postService = postService;
        this.commentService = commentService;
        this.userService = userService;
    }

    public List<Notification> getFollowNotifications(int uid) {
        return notificationMapper.select(FOLLOW, null, uid, null, null);
    }

    public List<LikeDTO> getLikeNotifications(int uid) {
        List<Notification> notifications = notificationMapper.select(LIKE, null, uid, null, null);
        List<LikeDTO> likeDTOS = new ArrayList<>();
        for (var notification : notifications) {
            LikeDTO likeDTO = new LikeDTO();
            likeDTO.setType(Integer.parseInt(notification.getProps()));
            if (likeDTO.getType() == LIKE_POST) {
                Post post = postService.get(notification.getEntityId());
                likeDTO.setContent(post.getTitle());
                likeDTO.setPostId(post.getId());
                likeDTO.setUser(userService.findById(notification.getSourceUid()));
            } else {
                Comment comment = commentService.findBydId(notification.getEntityId());
                likeDTO.setContent(comment.getContent());
                likeDTO.setPostId(comment.getPostId());
                likeDTO.setUser(userService.findById(notification.getSourceUid()));
            }
            likeDTO.setTime(notification.getTime());
            likeDTOS.add(likeDTO);
        }
        return likeDTOS;
    }

    public List<CommentNotifyDTO> getCommentNotifications(int uid) {
        List<Notification> notifications = notificationMapper.select(COMMENT, null, uid, null, null);
        List<CommentNotifyDTO> commentNotifyDTOS = new ArrayList<>();
        for (var notification : notifications) {
            var commentNotifyDTO = new CommentNotifyDTO();
            commentNotifyDTO.setType(Integer.parseInt(notification.getProps()));
            Comment reply = commentService.findBydId(notification.getSourceUid());
            commentNotifyDTO.setUser(userService.findById(reply.getUid()));
            commentNotifyDTO.setReply(reply.getContent());
            if (commentNotifyDTO.getType() == COMMENT_LEVEL_ONE) {
                Post post = postService.get(notification.getEntityId());
                commentNotifyDTO.setReplied(post.getTitle());
                commentNotifyDTO.setPostId(post.getId());
            } else {
                Comment replied = commentService.findBydId(notification.getEntityId());
                commentNotifyDTO.setReplied(replied.getContent());
                commentNotifyDTO.setPostId(replied.getPostId());
            }
            commentNotifyDTO.setTime(notification.getTime());
            commentNotifyDTOS.add(commentNotifyDTO);
        }
        return commentNotifyDTOS;
    }
}
