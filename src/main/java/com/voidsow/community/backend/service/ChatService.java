package com.voidsow.community.backend.service;

import com.voidsow.community.backend.dto.Conversation;
import com.voidsow.community.backend.entity.Chat;
import com.voidsow.community.backend.mapper.ChatMapper;
import com.voidsow.community.backend.mapper.CustomChatMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;
import java.util.List;

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

    public List<Conversation> getConversations(int uid) {
        List<Conversation> conservations = customChatMapper.getConversations(uid);
        conservations.forEach((v) -> v.setLast(chatMapper.selectByPrimaryKey(v.getLast().getId())));
        return conservations;
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

    public void sendMessage(Chat message) {
        int from = message.getSpeaker(), to = message.getListener();
        message.setConversationId(from < to ? from + "_" + to : to + "_" + from);
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setStatus(UNREAD);
        message.setGmtCreate(new Date());
        chatMapper.insertSelective(message);
    }

    public void setRead(List<Integer> ids) {
        customChatMapper.updateMsgStatus(ids, READ);
    }
}