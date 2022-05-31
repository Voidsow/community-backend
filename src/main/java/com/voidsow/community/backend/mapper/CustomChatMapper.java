package com.voidsow.community.backend.mapper;

import com.voidsow.community.backend.entity.Chat;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CustomChatMapper {
    //查询当前用户的会话列表，返回每个会话最后一条消息的id和会话消息数量
    List<Map<String, Object>> getConversations(int uid, Integer offset, Integer limit);

    //查询当前会话的消息
    List<Chat> getConversation(String conversationId, Integer offset, Integer limit);

    int countConversations(int uid);

    int countConversation(String conversationId);

    void updateMsgStatus(List<Integer> ids,int status);
}
