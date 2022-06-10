package com.voidsow.community.backend.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voidsow.community.backend.entity.Event;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.voidsow.community.backend.constant.Constant.EXCHANGE_NAME;

@Component
public class EventProducer {
    AmqpTemplate amqpTemplate;
    ObjectMapper objectMapper;

    @Autowired
    public EventProducer(AmqpTemplate amqpTemplate, ObjectMapper objectMapper) {
        this.amqpTemplate = amqpTemplate;
        this.objectMapper = objectMapper;
    }

    public void fireEvent(Event event) throws JsonProcessingException {
        amqpTemplate.send(EXCHANGE_NAME, event.getTopic(),
                MessageBuilder.withBody(objectMapper.writeValueAsBytes(event)).
                        setTimestamp(new Date()).build());
    }
}