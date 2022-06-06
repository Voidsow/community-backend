package com.voidsow.community.backend.mapper;

import com.voidsow.community.backend.dto.Conversation;
import com.voidsow.community.backend.entity.Chat;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CustomChatMapper {
    //查询用户的会话列表，返回每个会话最后一条消息的id、消息总数和未读数量
    List<Conversation> getConversations(int uid);

    //查询当前会话的消息
    List<Chat> getConversation(String conversationId, Integer offset, Integer limit);

    int countConversations(int uid);

    int countConversation(String conversationId);

    void updateMsgStatus(List<Integer> ids, int status);
}
