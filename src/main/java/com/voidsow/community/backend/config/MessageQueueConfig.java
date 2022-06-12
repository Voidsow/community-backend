package com.voidsow.community.backend.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import static com.voidsow.community.backend.constant.Constant.*;

@Configuration
@EnableRabbit
public class MessageQueueConfig {
    AmqpAdmin amqpAdmin;

    @Autowired
    public MessageQueueConfig(AmqpAdmin amqpAdmin) {
        this.amqpAdmin = amqpAdmin;
        Exchange community = ExchangeBuilder.topicExchange(EXCHANGE_NAME).durable(false).build();
        amqpAdmin.declareExchange(community);
        Queue comment = new Queue(TOPIC_COMMENT, false, true, false);
        Queue like = new Queue(TOPIC_LIKE, false, true, false);
        Queue follow = new Queue(TOPIC_FOLLOW, false, true, false);
        Queue search = new Queue(TOPIC_ELASTIC_SEARCH, false, true, false);
        amqpAdmin.declareQueue(comment);
        amqpAdmin.declareQueue(like);
        amqpAdmin.declareQueue(follow);
        amqpAdmin.declareQueue(search);
        amqpAdmin.declareBinding(BindingBuilder.bind(comment).to(community).with(TOPIC_COMMENT).noargs());
        amqpAdmin.declareBinding(BindingBuilder.bind(like).to(community).with(TOPIC_LIKE).noargs());
        amqpAdmin.declareBinding(BindingBuilder.bind(follow).to(community).with(TOPIC_FOLLOW).noargs());
        amqpAdmin.declareBinding(BindingBuilder.bind(search).to(community).with(TOPIC_ELASTIC_SEARCH).noargs());
    }
}
