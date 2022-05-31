package com.voidsow.community.backend.service;

import com.voidsow.community.backend.entity.Chat;
import com.voidsow.community.backend.mapper.ChatMapper;
import com.voidsow.community.backend.mapper.CustomChatMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.voidsow.community.backend.constant.Constant.READ;
import static com.voidsow.community.backend.constant.Constant.UNREAD;

@Service
public class ChatService {
    ChatMapper chatMapper;
    CustomChatMapper customChatMapper;

    @Autowired
    public ChatService(ChatMapper chatMapper, CustomChatMapper customChatMapper) {
        this.chatMapper = chatMapper;
        this.customChatMapper = customChatMapper;
    }

    public List<Map<String, Object>> getConversations(int uid, Integer offset, Integer limit) {
        List<Map<String, Object>> conversations = customChatMapper.getConversations(uid, offset, limit);
        conversations.forEach((v) -> v.put("message", chatMapper.selectByPrimaryKey((int) v.get("last"))));
        return conversations;
    }

    public int countConversations(int uid) {
        return customChatMapper.countConversations(uid);
    }

    public List<Chat> getConversation(String conversationId, int offset, int limit) {
        return customChatMapper.getConversation(conversationId, offset, limit);
    }

    public int countConversation(String conversationId) {
        return customChatMapper.countConversation(conversationId);
    }

    public void sendMessage(int from, int to, String content) {
        Chat message = new Chat(null, from, to, from < to ? from + "_" + to : to + "_" + from, HtmlUtils.htmlEscape(content), UNREAD, new Date());
        chatMapper.insertSelective(message);
    }

    public void setRead(List<Integer> ids) {
        customChatMapper.updateMsgStatus(ids, READ);
    }
}