package com.voidsow.community.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.voidsow.community.backend.annotation.LoginRequire;
import com.voidsow.community.backend.dto.CommentDTO;
import com.voidsow.community.backend.dto.CommentPOST;
import com.voidsow.community.backend.dto.Result;
import com.voidsow.community.backend.entity.Comment;
import com.voidsow.community.backend.entity.Event;
import com.voidsow.community.backend.entity.Post;
import com.voidsow.community.backend.entity.User;
import com.voidsow.community.backend.event.EventProducer;
import com.voidsow.community.backend.service.CommentService;
import com.voidsow.community.backend.service.PostService;
import com.voidsow.community.backend.utils.HostHolder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import static com.voidsow.community.backend.constant.Constant.*;

@RestController
public class CommentController {
    CommentService commentService;
    PostService postService;
    EventProducer producer;
    HostHolder hostHolder;


    @Autowired
    public CommentController(CommentService commentService, PostService postService, EventProducer producer, HostHolder hostHolder) {
        this.commentService = commentService;
        this.postService = postService;
        this.producer = producer;
        this.hostHolder = hostHolder;
    }

    @LoginRequire
    @PostMapping(value = "/comment", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result pubComment(@RequestBody CommentPOST commentPOST) throws JsonProcessingException {
        User user = hostHolder.user.get();
        Comment comment = new Comment();
        int pid = commentPOST.getPid();
        BeanUtils.copyProperties(commentPOST, comment);
        //????????????????????????????????????????????????????????????????????????sourceUid?????????????????????????????????id??????????????????????????????????????????????????????
        int notifiedUid;
        int entityid;
        //????????????
        if (commentPOST.getReplyTo() == null) {
            //????????????????????????
            Post post = postService.get(pid);
            if (post == null)
                return Result.illegalAccess();
            notifiedUid = post.getUid();
            comment.setReplyTo(pid);
            comment.setType(COMMENT_LEVEL_ONE);
            entityid = pid;
        } else {
            Comment replyTo = commentService.findBydId(commentPOST.getReplyTo());
            //????????????????????????????????????????????????id???pid???????????????????????????uid?????????
            if (replyTo == null || replyTo.getPostId() != pid)
                return Result.illegalAccess();
            //??????????????????????????????????????????
            if (commentPOST.getReplyToUid() != null) {
                //?????????????????????@?????????????????????????????????uid???replyToUid????????????
                if (!commentPOST.getReplyToUid().equals(commentService.findBydId(commentPOST.getAtCommentId()).getUid()))
                    return Result.illegalAccess();
                notifiedUid = commentPOST.getReplyToUid();
                entityid = commentPOST.getAtCommentId();
            } else {
                notifiedUid = replyTo.getUid();
                entityid = replyTo.getId();
            }
            comment.setType(COMMENT_LEVEL_TWO);
        }
        comment.setPostId(pid);
        comment.setUid(user.getId());
        int sourceId = commentService.add(comment);

        //????????????
        //??????????????????????????????
        if (notifiedUid != user.getId()) {
            Event event = new Event().setTopic(TOPIC_COMMENT).
                    setSourceUid(sourceId).
                    setToUid(notifiedUid).
                    setTime(new Date()).
                    addProperty("type", comment.getType()).
                    addProperty("id", entityid);//??????????????????id
            producer.fireEvent(event);
        }
        //??????????????????elastic????????????
        producer.fireEvent(new Event().setTopic(TOPIC_ELASTIC_SEARCH).addProperty("id", commentPOST.getPid()));
        //?????????????????????
        CommentDTO commentDTO = new CommentDTO();
        BeanUtils.copyProperties(comment, commentDTO);
        return new Result(200, "ok", commentDTO);
    }
}
