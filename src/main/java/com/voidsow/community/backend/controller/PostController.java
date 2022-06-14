package com.voidsow.community.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.voidsow.community.backend.annotation.LoginRequire;
import com.voidsow.community.backend.dto.CommentDTO;
import com.voidsow.community.backend.dto.PostDTO;
import com.voidsow.community.backend.dto.Result;
import com.voidsow.community.backend.entity.*;
import com.voidsow.community.backend.event.EventProducer;
import com.voidsow.community.backend.service.CommentService;
import com.voidsow.community.backend.service.LikeService;
import com.voidsow.community.backend.service.PostService;
import com.voidsow.community.backend.service.UserService;
import com.voidsow.community.backend.utils.HostHolder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.voidsow.community.backend.constant.Constant.*;

@RestController
@RequestMapping(value = "/post", produces = MediaType.APPLICATION_JSON_VALUE)
public class PostController {
    PostService postService;
    UserService userService;
    CommentService commentService;
    LikeService likeService;
    EventProducer eventProducer;
    private final HostHolder hostHolder;

    @Autowired
    public PostController(PostService postService, UserService userService,
                          CommentService commentService, HostHolder hostHolder,
                          LikeService likeService, EventProducer eventProducer) {
        this.postService = postService;
        this.userService = userService;
        this.commentService = commentService;
        this.likeService = likeService;
        this.eventProducer = eventProducer;
        this.hostHolder = hostHolder;
    }

    @GetMapping("/{id}")
    public Result getPost(@PathVariable("id") int id, int page, @RequestParam("size") int pageSize) {
        Post post = postService.get(id);
        User user = hostHolder.user.get();
        boolean login = user != null;
        Map<String, Object> map = new HashMap<>();
        PostDTO postDTO = new PostDTO();
        BeanUtils.copyProperties(post, postDTO);
        postDTO.setLikeNum(likeService.likeNum(LIKE_POST, post.getId()));
        postDTO.setLike(login && likeService.like(LIKE_POST, post.getId(), user.getId()));
        map.put("post", postDTO);
        //一级评论列表
        List<CommentDTO> commentDTOS = new ArrayList<>();
        List<Comment> comments = commentService.find(COMMENT_LEVEL_ONE, id, (page - 1) * pageSize, pageSize);
        for (var comment : comments) {
            //二级评论列表
            List<Comment> subComments = commentService.find(COMMENT_LEVEL_TWO,
                    comment.getId(), 0, Integer.MAX_VALUE);
            List<CommentDTO> subCommentDTOs = new ArrayList<>();
            for (var subComment : subComments) {
                CommentDTO subCommentDTO = new CommentDTO();
                BeanUtils.copyProperties(subComment, subCommentDTO);
                subCommentDTO.setLikeNum(likeService.likeNum(LIKE_COMMENT, subComment.getId()));
                subCommentDTO.setLike(login && likeService.like(LIKE_COMMENT, subComment.getId(), user.getId()));
                subCommentDTO.setUser(userService.findById(subComment.getUid()));
                subCommentDTO.setTarget(subComment.getReplyToUid() == null ? null :
                        userService.findById(subComment.getReplyToUid()));
                subCommentDTOs.add(subCommentDTO);
            }
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userService.findById(comment.getUid()));
            commentDTO.setSubComments(subCommentDTOs);
            commentDTO.setCommentNum(subComments.size());
            commentDTO.setLikeNum(likeService.likeNum(LIKE_COMMENT, comment.getId()));
            commentDTO.setLike(login && likeService.like(LIKE_COMMENT, comment.getId(), user.getId()));
            commentDTOS.add(commentDTO);
        }
        map.put("comments", commentDTOS);
        map.put("author", userService.findByIdToDTO(userService.findById(post.getUid()), user));
        map.put("lastPage", Math.ceil(1.0 * commentService.getCount(COMMENT_LEVEL_ONE, id) / pageSize));
        return new Result(200, "ok", map);
    }

    @LoginRequire
    @PostMapping
    public Result addPost(@RequestBody PostDTO postDTO) throws JsonProcessingException {
        User user = hostHolder.user.get();
        if (postDTO.getTitle() == null || postDTO.getTitle().isBlank())
            return new Result(INVALID, "标题不能为空", null);
        else if (postDTO.getContent() == null || postDTO.getContent().isBlank())
            return new Result(INVALID, "内容不能为空", null);
        Post post = Factory.newPost(user.getId(), postDTO.getTitle(), postDTO.getContent());
        postService.add(post);
        //存入elastic
        eventProducer.fireEvent(new Event().setTopic(TOPIC_ELASTIC_SEARCH).addProperty("id", post.getId()));
        BeanUtils.copyProperties(post, postDTO);
        return Result.getSuccess(postDTO);
    }
}
