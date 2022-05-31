package com.voidsow.community.backend.controller;

import com.google.code.kaptcha.Producer;
import com.voidsow.community.backend.constant.Activation;
import com.voidsow.community.backend.dto.LoginInfo;
import com.voidsow.community.backend.dto.Result;
import com.voidsow.community.backend.entity.User;
import com.voidsow.community.backend.service.UserService;
import com.voidsow.community.backend.utils.Authorizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
    private Authorizer authorizer;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${community.token.duration.session}")
    private int SESSION;

    @Value("${community.token.duration.long-term}")
    private int LONG_TERM;

    @Autowired
    public AuthController(UserService userService, Producer captchaProducer, Authorizer authorizer, StringRedisTemplate strRedisTemplate) {
        this.userService = userService;
        this.captchaProducer = captchaProducer;
        this.authorizer = authorizer;
        this.strRedisTemplate = strRedisTemplate;
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
    public String register(User user, @RequestParam("confirm-psw") String confirmPsw, Model model) throws MessagingException {
        Map<String, Object> result = userService.register(user, confirmPsw);
        if (result.isEmpty()) {
            model.addAttribute("msg", "注册成功，激活邮件已发往您的邮箱，请前往邮箱查看！");
            model.addAttribute("targetLink", "/");
            return "operate-result";
        } else {
            model.addAllAttributes(result);
            model.addAttribute("user", user);
            return "register";
        }
    }

    @GetMapping("/activate/{activationCode}")
    public String activate(@PathVariable("activationCode") String activationCode,
                           Model model) {
        Activation result = userService.activate(activationCode);
        if (result == SUCCEESS) {
            model.addAttribute("msg", "激活成功，跳转到登录页面");
            model.addAttribute("targetLink", "/login");
        } else if (result == ACTIVATED) {
            model.addAttribute("msg", "该账号已被激活！");
            model.addAttribute("targetLink", "/index");
        } else {
            model.addAttribute("msg", "激活链接无效！");
            model.addAttribute("targetLink", "/index");
        }
        return "operate-result";
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result login(@RequestBody LoginInfo info, HttpSession session, HttpServletResponse response) {
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
            authorizer.generateToken(user, duration);
            Cookie token = new Cookie("token", authorizer.generateToken(user, duration));
            token.setPath(contextPath);
            token.setMaxAge(duration);
            response.addCookie(token);
        }
        return result;
    }

    @GetMapping("/logout")
    public Result logout(@CookieValue(value = "token", required = false) Cookie cookie, HttpServletResponse response) {
        if (cookie != null) {
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
        return Result.getSuccess(null);
    }
}