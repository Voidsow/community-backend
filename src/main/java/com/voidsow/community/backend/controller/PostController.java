package com.voidsow.community.backend.controller;

import com.voidsow.community.backend.dto.CommentDTO;
import com.voidsow.community.backend.dto.PostDTO;
import com.voidsow.community.backend.dto.Result;
import com.voidsow.community.backend.entity.Comment;
import com.voidsow.community.backend.entity.Post;
import com.voidsow.community.backend.entity.User;
import com.voidsow.community.backend.service.CommentService;
import com.voidsow.community.backend.service.LikeService;
import com.voidsow.community.backend.service.PostService;
import com.voidsow.community.backend.service.UserService;
import com.voidsow.community.backend.utils.HostHolder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.voidsow.community.backend.constant.Constant.*;

@RestController
@RequestMapping("/post")
public class PostController {
    PostService postService;
    UserService userService;
    CommentService commentService;
    LikeService likeService;
    private HostHolder hostHolder;

    @Autowired
    public PostController(PostService postService, UserService userService,
                          CommentService commentService, HostHolder hostHolder,
                          LikeService likeService) {
        this.postService = postService;
        this.userService = userService;
        this.commentService = commentService;
        this.likeService = likeService;
        this.hostHolder = hostHolder;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getPost(@PathVariable("id") int id, int page, @RequestParam("size") int pageSize) {
        Post post = postService.get(id);
        User user = hostHolder.user.get();
        boolean login = user != null;
        Map<String, Object> map = new HashMap<>();
        PostDTO postDTO = new PostDTO();
        BeanUtils.copyProperties(post, postDTO);
        postDTO.setLikeNum(likeService.likeNum(POST, post.getId()));
        postDTO.setLike(login && likeService.like(POST, post.getId(), user.getId()));
        map.put("post", postDTO);
        //一级评论列表
        List<CommentDTO> commentDTOS = new ArrayList<>();
        List<Comment> comments = commentService.find(POST_LEVEL_ONE, id, (page - 1) * pageSize, pageSize);
        for (var comment : comments) {
            //二级评论列表
            List<Comment> subComments = commentService.find(POST_LEVEL_TWO,
                    comment.getId(), 0, Integer.MAX_VALUE);
            List<CommentDTO> subCommentDTOs = new ArrayList<>();
            for (var subComment : subComments) {
                CommentDTO subCommentDTO = new CommentDTO();
                BeanUtils.copyProperties(subComment, subCommentDTO);
                subCommentDTO.setLikeNum(likeService.likeNum(COMMENT, subComment.getId()));
                subCommentDTO.setLike(login && likeService.like(COMMENT, subComment.getId(), user.getId()));
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
            commentDTO.setLikeNum(likeService.likeNum(COMMENT, comment.getId()));
            commentDTO.setLike(login && likeService.like(COMMENT, comment.getId(), user.getId()));
            commentDTOS.add(commentDTO);
        }
        map.put("comments", commentDTOS);
        map.put("author", userService.findById(post.getUid()));
        map.put("lastPage", Math.ceil(1.0 * commentService.getCount(POST_LEVEL_ONE, id) / pageSize));
        return new Result(200, "ok", map);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Result addPost(@RequestBody Post post) {
        User user = hostHolder.user.get();
        if (post.getTitle() == null || post.getTitle().isBlank())
            return new Result(400, "标题不能为空", null);
        else if (post.getContent() == null || post.getContent().isBlank())
            return new Result(400, "内容不能为空", null);
        else if (user == null)
            return new Result(403, "尚未登录", null);
        post.setUid(user.getId());
        Date curTime = new Date();
        post.setGmtCreate(curTime);
        post.setGmtModified(curTime);
        postService.add(post);
        return new Result(0, "发布成功", null);
    }
}
