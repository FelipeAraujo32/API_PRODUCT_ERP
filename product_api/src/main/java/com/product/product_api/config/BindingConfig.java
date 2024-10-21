package com.product.product_api.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BindingConfig {
    
    @Bean
    public Binding bindingCreated(Queue productCreatedQueue, TopicExchange productExchange) {
        return BindingBuilder.bind(productCreatedQueue).to(productExchange).with("product.created");
    }

    @Bean
    public Binding bindingUpdated(Queue productUpdatedQueue, TopicExchange productExchange) {
        return BindingBuilder.bind(productUpdatedQueue).to(productExchange).with("product.updated");
    }

    @Bean
    public Binding bindingDeleted(Queue productDeletedQueue, TopicExchange productExchange) {
        return BindingBuilder.bind(productDeletedQueue).to(productExchange).with("product.deleted");
    }
}
