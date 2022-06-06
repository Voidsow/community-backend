package com.voidsow.community.backend.controller;

import com.voidsow.community.backend.annotation.LoginRequire;
import com.voidsow.community.backend.dto.Result;
import com.voidsow.community.backend.dto.UserDTO;
import com.voidsow.community.backend.entity.User;
import com.voidsow.community.backend.exception.ResourceNotFoundException;
import com.voidsow.community.backend.service.FollowService;
import com.voidsow.community.backend.service.LikeService;
import com.voidsow.community.backend.service.UserService;
import com.voidsow.community.backend.utils.Authorizer;
import com.voidsow.community.backend.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {
    UserService userService;
    LikeService likeService;
    FollowService followService;
    HostHolder hostHolder;
    Authorizer authorizer;

    @Value("${community.path.upload}")
    private String UPLOAD_PATH;

    @Value("${community.domain}")
    private String DOMAIN;

    @Value("${server.servlet.context-path}")
    private String CONTEXT_PATH;

    @Autowired
    public UserController(UserService userService, LikeService likeService,
                          HostHolder hostHolder, Authorizer authorizer, FollowService followService) {
        this.userService = userService;
        this.likeService = likeService;
        this.followService = followService;
        this.hostHolder = hostHolder;
        this.authorizer = authorizer;
    }

    @LoginRequire
    @PostMapping("/upload")
    public String uploadHeader(MultipartFile headerImage,
                               Model model) throws IOException {
        if (headerImage == null) {
            model.addAttribute("errorMsg", "未选中图片");
            return "/setting";
        }
        String imageName = headerImage.getOriginalFilename();
        String suffix = imageName.substring(imageName.lastIndexOf('.'));
        if (suffix.isBlank() || !isImage(suffix)) {
            model.addAttribute("errorMsg", "图片格式不正确");
            return "/setting";
        }
        imageName = UUID.randomUUID().toString().replaceAll("-", "") + imageName;
        headerImage.transferTo(new File(UPLOAD_PATH + "/" + imageName));
        String headerUrl = DOMAIN + CONTEXT_PATH + "user/header/" + imageName;
        userService.updateHeader(hostHolder.user.get().getId(), headerUrl);
        hostHolder.user.get().setHeaderUrl(headerUrl);
        return "redirect:/";
    }

    @GetMapping(value = "/header/{imageName}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE})
    @ResponseBody
    byte[] getHeader(@PathVariable("imageName") String fileName) throws IOException {
        fileName = UPLOAD_PATH + "/" + fileName;
        try (FileInputStream fileInputStream = new FileInputStream(fileName);
             ByteArrayOutputStream byteOs = new ByteArrayOutputStream()) {
            fileInputStream.transferTo(byteOs);
            return byteOs.toByteArray();
        }
    }

    @GetMapping("/{id}")
    public Result getProfile(@PathVariable("id") int id) {
        User user = hostHolder.user.get();
        User observed = userService.findById(id);
        if (observed == null)
            return Result.resourceNotFound();
        return Result.getSuccess(userService.findByIdToDTO(observed, user));
    }

    boolean isImage(String suffix) {
        suffix = suffix.toLowerCase();
        return suffix.equals(".jpg") || suffix.equals(".png") || suffix.equals(".jpeg");
    }
}