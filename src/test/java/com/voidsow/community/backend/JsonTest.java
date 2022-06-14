package com.voidsow.community.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voidsow.community.backend.entity.Event;
import com.voidsow.community.backend.entity.User;
import com.voidsow.community.backend.utils.Authorizer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class JsonTest {

    @Test
    void test() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.readValue(objectMapper.writeValueAsBytes(new Event()), Event.class));
    }

    @Test
    void testJWT(@Autowired Authorizer authorizer) {
        User user = new User();
        user.setId(2);
        System.out.println(authorizer.generateToken(user, 1800));
        int debug = 0;
    }
}
