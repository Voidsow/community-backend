package com.voidsow.community.backend.controller;

import com.voidsow.community.backend.annotation.LoginRequire;
import com.voidsow.community.backend.dto.Conversation;
import com.voidsow.community.backend.dto.Result;
import com.voidsow.community.backend.entity.Chat;
import com.voidsow.community.backend.entity.User;
import com.voidsow.community.backend.service.ChatService;
import com.voidsow.community.backend.service.UserService;
import com.voidsow.community.backend.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.voidsow.community.backend.constant.Constant.UNREAD;

@RestController
@RequestMapping("/chat")
public class ChatController {
    HostHolder hostHolder;
    ChatService chatService;
    UserService userService;

    @Value("${community.page.size}")
    int pageSize;

    @Value("${community.page.num}")
    int pageNum;

    @Autowired
    public ChatController(HostHolder hostHolder, ChatService chatService, UserService userService) {
        this.hostHolder = hostHolder;
        this.chatService = chatService;
        this.userService = userService;
    }

    @LoginRequire
    @GetMapping
    public Result getChatList(int page, @RequestParam("size") int pageSize) {
        User user = hostHolder.user.get();
        //返回的会话按照最后一条消息的日期和阅读数降序排序
        List<Conversation> conversations = chatService.getConversations(user.getId());
        int totalUnread = 0;
        int start = (page - 1) * pageSize;
        int end = page * pageSize;
        var iterator = conversations.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            Conversation conversation = iterator.next();
            totalUnread += conversation.getUnread();
            if (start <= count && count < end) {
                String[] uids = conversation.getLast().getConversationId().split("_");
                int uid0 = Integer.parseInt(uids[0]);
                int talkToUid = uid0 != user.getId() ? uid0 : Integer.parseInt(uids[1]);
                conversation.setTalkTo(userService.findById(talkToUid));
            }
            count++;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("conversations", conversations.subList(start, Math.min(conversations.size(), end)));
        map.put("totalUnread", totalUnread);
        //会话的数量总和
        map.put("sum", Math.ceil(1.0 * chatService.countConversations(user.getId()) / pageSize));
        return Result.getSuccess(map);
    }

    @LoginRequire
    @GetMapping("/{uid}")
    public Result getChatDetail(@PathVariable("uid") int talkToUid) {
        User user = hostHolder.user.get();
        int min = user.getId(), max = talkToUid;
        if (talkToUid < user.getId()) {
            min = talkToUid;
            max = user.getId();
        }
        List<Chat> conversation = chatService.getConversation(
                min + "_" + max, 0, Integer.MAX_VALUE);
        //更新阅读状态的消息id列表
        List<Integer> setReadIds = new ArrayList<>();
        for (var message : conversation) {
            //更新消息读状态
            if (message.getStatus().equals(UNREAD) && message.getListener().equals(user.getId()))
                setReadIds.add(message.getId());
        }
        if (!setReadIds.isEmpty())
            chatService.setRead(setReadIds);
        Map<String, Object> map = new HashMap<>();
        map.put("messages", conversation);
        map.put("talkTo", userService.findById(talkToUid));
        return Result.getSuccess(map);
    }

    @LoginRequire
    @PostMapping(produces = "application/json")
    @ResponseBody
    public Result sendMessage(@RequestBody Chat message) {
        User to = userService.findById(message.getListener());
        message.setSpeaker(hostHolder.user.get().getId());
        if (to == null)
            return new Result(404, "收信人不存在", null);
        chatService.sendMessage(message, true);
        return Result.getSuccess(message);
    }
}
