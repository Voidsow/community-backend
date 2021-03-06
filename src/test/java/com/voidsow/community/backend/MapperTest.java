package com.voidsow.community.backend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voidsow.community.backend.mapper.ChatMapper;
import com.voidsow.community.backend.mapper.CustomChatMapper;
import com.voidsow.community.backend.mapper.CustomPostMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest
public class MapperTest {
    final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    ChatMapper chatMapper;

    @Autowired
    CustomChatMapper customChatMapper;

    @Autowired
    CustomPostMapper customPostMapper;

    @Test
    void testGetChat() {
        customChatMapper.getConversation("1_2", 1, 100).forEach(v -> {
            try {
                System.out.println(objectMapper.writeValueAsString(v));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    void testGetConversation() {
        customChatMapper.getConversations(1).forEach(v -> {
            try {
                v.setLast(chatMapper.selectByPrimaryKey(v.getLast().getId()));
                System.out.println(objectMapper.writeValueAsString(v));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    void testBlobSelect() throws JsonProcessingException {
        Set<Integer> uids = new HashSet<>();
        uids.add(4);
        System.out.println(objectMapper.writeValueAsString(customPostMapper.selectByUid(uids, 0, Integer.MAX_VALUE)));
    }
}
