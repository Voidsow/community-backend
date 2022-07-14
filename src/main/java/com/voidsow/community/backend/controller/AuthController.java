package com.voidsow.community.backend.controller;

import com.google.code.kaptcha.Producer;
import com.voidsow.community.backend.annotation.LoginRequire;
import com.voidsow.community.backend.annotation.TokenRequire;
import com.voidsow.community.backend.constant.Activation;
import com.voidsow.community.backend.dto.LoginInfo;
import com.voidsow.community.backend.dto.Result;
import com.voidsow.community.backend.entity.User;
import com.voidsow.community.backend.service.UserService;
import com.voidsow.community.backend.utils.Authorizer;
import com.voidsow.community.backend.utils.HostHolder;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.voidsow.community.backend.constant.Activation.ACTIVATED;
import static com.voidsow.community.backend.constant.Activation.SUCCEESS;
import static com.voidsow.community.backend.constant.Constant.*;

@RestController
    public class AuthController {
    UserService userService;
    Producer captchaProducer;
    StringRedisTemplate strRedisTemplate;
    HostHolder hostHolder;
    private Authorizer authorizer;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${community.token.duration.session}")
    private int SESSION;

    @Value("${community.token.duration.long-term}")
    private int LONG_TERM;

    @Autowired
    public AuthController(UserService userService, Producer captchaProducer, Authorizer authorizer,
                          StringRedisTemplate strRedisTemplate, HostHolder hostHolder) {
        this.userService = userService;
        this.captchaProducer = captchaProducer;
        this.authorizer = authorizer;
        this.strRedisTemplate = strRedisTemplate;
        this.hostHolder = hostHolder;
    }

    @GetMapping(value = "/captcha", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getCaptcha(HttpSession session) throws IOException {
        String text = captchaProducer.createText();
        strRedisTemplate.opsForValue().set("captcha" + session.getId(), text, 60, TimeUnit.SECONDS);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(captchaProducer.createImage(text), "png", outputStream);
        return outputStream.toByteArray();
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user) throws MessagingException {
        String result = userService.register(user);
        if (result == null) {
            return Result.getSuccess(null);
        } else {
            return Result.incorrectArgument(result);
        }
    }

    @PostMapping("/activate/{activationCode}")
    public Result activate(@PathVariable("activationCode") String activationCode) {
        Activation result = userService.activate(activationCode);
        if (result == SUCCEESS) {
            return Result.getSuccess(null);
        } else if (result == ACTIVATED) {
            return new Result(INCORRECT, "重复激活", null);
        } else {
            return new Result(INVALID, "激活链接无效", null);
        }
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result login(@RequestBody LoginInfo info, HttpSession session) {
        String answer = strRedisTemplate.opsForValue().get("captcha" + session.getId());
        Result result = new Result(SUCCESS, "ok", null);
        if (answer == null) {
            result.setCode(EXPIRED);
            result.setMessage("验证码已过期");
        } else if (!answer.equalsIgnoreCase(info.getCaptcha())) {
            result.setCode(INCORRECT);
            result.setMessage("验证码错误");
        }
        int duration = info.isLongTerm() ? LONG_TERM : SESSION;
        userService.login(info.getUsername(), info.getPsw(), result);
        if (result.getCode() == SUCCESS) {
            User user = (User) result.getData();
            Map<String, Object> map = new HashMap<>();
            map.put("token", authorizer.generateToken(user, duration));
            map.put("user", user);
            result.setData(map);
        }
        return result;
    }

    @TokenRequire
    @LoginRequire
    @PostMapping("/logout")
    public Result logout(@RequestAttribute("token") Claims claims) {
        long duration = (claims.getExpiration().getTime() - System.currentTimeMillis()) / 1000;
        strRedisTemplate.opsForValue().set("blacklist:" + claims.getId(), "0",
                Math.max(0, duration), TimeUnit.SECONDS);
        return Result.getSuccess(null);
    }
}