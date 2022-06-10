package com.voidsow.community.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voidsow.community.backend.entity.Event;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class JsonTest {

    @Test
    void test() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.readValue(objectMapper.writeValueAsBytes(new Event()), Event.class));
    }
}
