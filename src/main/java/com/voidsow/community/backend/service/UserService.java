package com.voidsow.community.backend.service;

import com.voidsow.community.backend.constant.Activation;
import com.voidsow.community.backend.constant.Constant;
import com.voidsow.community.backend.dto.Result;
import com.voidsow.community.backend.dto.UserDTO;
import com.voidsow.community.backend.entity.User;
import com.voidsow.community.backend.entity.UserExample;
import com.voidsow.community.backend.mapper.UserMapper;
import com.voidsow.community.backend.utils.MailClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.mail.MessagingException;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.voidsow.community.backend.constant.Activation.*;
import static com.voidsow.community.backend.constant.Activation.INVALID;
import static com.voidsow.community.backend.constant.Constant.*;
import static com.voidsow.community.backend.utils.Authorizer.generateUUID;

@Service
public class UserService {
    FollowService followService;
    LikeService likeService;
    UserMapper userMapper;
    MailClient mailClient;
    Key key;

    @Autowired
    public UserService(FollowService followService, LikeService likeService, UserMapper userMapper, MailClient mailClient, Key key) {
        this.followService = followService;
        this.likeService = likeService;
        this.userMapper = userMapper;
        this.mailClient = mailClient;
        this.key = key;
    }

    public User findById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    public UserDTO findByIdToDTO(User observed, User user) {
        return new UserDTO(observed,
                followService.countFollowee(observed.getId()),
                followService.countFollower(observed.getId()),
                user != null && followService.isFollower(observed.getId(), user.getId()),
                likeService.getLike(observed.getId()));
    }

    public String register(User user) throws MessagingException {
        if (user.getUsername() == null)
            return "用户名不能为空";
        if (user.getPassword() == null)
            return "密码不能为空";
        if (user.getEmail() == null)
            return "邮箱不能为空";
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUsernameEqualTo(user.getUsername());
        if (!userMapper.selectByExample(userExample).isEmpty())
            return "用户名已经被使用";
        userExample.clear();
        userExample.createCriteria().andEmailEqualTo(user.getEmail());
        if (!userMapper.selectByExample(userExample).isEmpty())
            return "邮箱已经被注册";

        user.setSalt(generateUUID());
        user.setPassword(DigestUtils.md5DigestAsHex((user.getPassword() + user.getSalt()).getBytes()));
        //普通用户
        user.setType(COMMON);
        //未激活
        user.setStatus(UNACTIVATED);
        user.setActivationCode(generateUUID());
        user.setGmtCreate(new Date());
        user.setGmtModified(user.getGmtCreate());
        userMapper.insertSelective(user);
        mailClient.sendActivationEmail(user.getEmail(), user.getActivationCode());
        return null;
    }

    public Activation activate(String activationCode) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andActivationCodeEqualTo(activationCode);
        List<User> activated = userMapper.selectByExample(userExample);
        if (activated.isEmpty())
            return INVALID;
        else if (activated.get(0).getStatus() == UNACTIVATED) {
            User user = new User();
            user.setStatus(NORMAL);
            userMapper.updateByExampleSelective(user, userExample);
            return SUCCEESS;
        } else
            return ACTIVATED;
    }

    public void login(String username, String password, Result result) {
        if (username == null || username.isEmpty()) {
            result.setCode(Constant.INVALID);
            result.setMessage("用户名不能为空");
        } else if (password == null || password.isEmpty()) {
            result.setCode(Constant.INVALID);
            result.setMessage("密码不能为空");
        }
        if (result.getCode() != SUCCESS)
            return;
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUsernameEqualTo(username);
        List<User> users = userMapper.selectByExample(userExample);
        if (users.isEmpty()) {
            result.setCode(Constant.INVALID);
            result.setMessage("用户名不存在");
            return;
        }
        User user = users.get(0);
        if (user.getStatus().equals(UNACTIVATED)) {
            result.setCode(INCORRECT);
            result.setMessage("账号尚未激活");
            return;
        }
        if (!DigestUtils.md5DigestAsHex((password + user.getSalt()).getBytes()).
                equals(user.getPassword())) {
            result.setCode(INCORRECT);
            result.setMessage("密码错误");
            return;
        }
        result.setData(user);
    }

    public void updateHeader(int userId, String headerUrl) {
        User user = new User();
        user.setId(userId);
        user.setHeaderUrl(headerUrl);
        userMapper.updateByPrimaryKeySelective(user);
    }

    public String updatePassword(int uid, String former, String present) {
        User user = userMapper.selectByPrimaryKey(uid);
        if (!DigestUtils.md5DigestAsHex((former + user.getSalt()).getBytes())
                .equals(user.getPassword()))
            return "原密码错误";
        user.setPassword(DigestUtils.md5DigestAsHex((present + user.getSalt()).getBytes()));
        userMapper.updateByPrimaryKey(user);
        return null;
    }
}