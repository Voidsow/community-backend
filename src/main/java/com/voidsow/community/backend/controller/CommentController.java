package com.voidsow.community.backend.controller;

import com.voidsow.community.backend.dto.CommentDTO;
import com.voidsow.community.backend.dto.CommentPOST;
import com.voidsow.community.backend.dto.Result;
import com.voidsow.community.backend.entity.Comment;
import com.voidsow.community.backend.service.CommentService;
import com.voidsow.community.backend.utils.HostHolder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.voidsow.community.backend.constant.Constant.POST_LEVEL_ONE;
import static com.voidsow.community.backend.constant.Constant.POST_LEVEL_TWO;

@RestController
public class CommentController {
    CommentService commentService;
    HostHolder hostHolder;

    @Autowired
    public CommentController(CommentService commentService, HostHolder hostHolder) {
        this.commentService = commentService;
        this.hostHolder = hostHolder;
    }

    @PostMapping(value = "/comment", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getMapping(@RequestBody CommentPOST commentPOST) {
        Comment comment = new Comment();
        int pid = commentPOST.getPid();
        BeanUtils.copyProperties(commentPOST, comment);
        //一级评论
        if (commentPOST.getReplyTo() == null) {
            comment.setReplyTo(pid);
            comment.setType(POST_LEVEL_ONE);
        } else
            comment.setType(POST_LEVEL_TWO);
        comment.setUid(hostHolder.user.get().getId());
        commentService.add(comment, pid);
        CommentDTO commentDTO = new CommentDTO();
        BeanUtils.copyProperties(comment, commentDTO);
        return new Result(200, "ok", commentDTO);
    }
}
