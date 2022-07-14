package com.voidsow.community.backend.controller;

import com.voidsow.community.backend.annotation.LoginRequire;
import com.voidsow.community.backend.dto.Result;
import com.voidsow.community.backend.dto.UserDTO;
import com.voidsow.community.backend.entity.User;
import com.voidsow.community.backend.exception.ResourceNotFoundException;
import com.voidsow.community.backend.service.FollowService;
import com.voidsow.community.backend.service.LikeService;
import com.voidsow.community.backend.service.PostService;
import com.voidsow.community.backend.service.UserService;
import com.voidsow.community.backend.utils.Authorizer;
import com.voidsow.community.backend.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {
    UserService userService;
    PostService postService;
    LikeService likeService;
    FollowService followService;
    HostHolder hostHolder;
    Authorizer authorizer;

    @Value("${community.path.upload}")
    private String UPLOAD_PATH;

    @Value("${community.backend}")
    private String DOMAIN;

    @Value("${server.servlet.context-path}")
    private String CONTEXT_PATH;

    @Autowired
    public UserController(UserService userService, PostService postService, LikeService likeService,
                          HostHolder hostHolder, Authorizer authorizer, FollowService followService) {
        this.userService = userService;
        this.postService = postService;
        this.likeService = likeService;
        this.followService = followService;
        this.hostHolder = hostHolder;
        this.authorizer = authorizer;
    }

    @LoginRequire
    @PostMapping("/upload")
    public Result uploadHeader(MultipartFile avatar) throws IOException {
        if (avatar == null)
            return Result.incorrectArgument("未选中图片");
        String imageName = avatar.getOriginalFilename();
        String suffix = imageName.substring(imageName.lastIndexOf('.'));
        if (suffix.isBlank() || !isImage(suffix))
            return Result.incorrectArgument("图片格式不正确");
        imageName = UUID.randomUUID().toString().replaceAll("-", "") + imageName;
        avatar.transferTo(new File(UPLOAD_PATH + "/" + imageName));
        String headerUrl = "http://" + DOMAIN + CONTEXT_PATH + "user/header/" + imageName;
        userService.updateHeader(hostHolder.user.get().getId(), headerUrl);
        hostHolder.user.get().setHeaderUrl(headerUrl);
        return Result.getSuccess(headerUrl);
    }

    @GetMapping(value = "/header/{imageName}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE})
    public byte[] getHeader(@PathVariable("imageName") String fileName) throws IOException {
        fileName = UPLOAD_PATH + "/" + fileName;
        try (FileInputStream fileInputStream = new FileInputStream(fileName);
             ByteArrayOutputStream byteOs = new ByteArrayOutputStream()) {
            fileInputStream.transferTo(byteOs);
            return byteOs.toByteArray();
        }
    }

    @LoginRequire
    @PostMapping("/update/password")
    public Result updatePassword(@RequestBody Map<String, String> passwords) {
        String msg = userService.updatePassword(hostHolder.user.get().getId(),
                passwords.get("former"), passwords.get("present"));
        if (msg != null)
            return Result.incorrectArgument(msg);
        else
            return Result.getSuccess(null);
    }

    @GetMapping("/{id}")
    public Result getProfile(@PathVariable("id") int id) {
        User user = hostHolder.user.get();
        User observed = userService.findById(id);
        if (observed == null)
            return Result.resourceNotFound();
        return Result.getSuccess(userService.findByIdToDTO(observed, user));
    }

    @GetMapping("/{id}/posts")
    public Result getPosts(@PathVariable("id") int id) {
        return Result.getSuccess(postService.getPostsByUid(id));
    }

    boolean isImage(String suffix) {
        suffix = suffix.toLowerCase();
        return suffix.equals(".jpg") || suffix.equals(".png") || suffix.equals(".jpeg");
    }
}