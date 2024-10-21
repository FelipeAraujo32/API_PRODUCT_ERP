package com.product.product_api.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfig {
    
    @Bean
    public Queue productCreatedQueue() {
        return new Queue("product.created.queue");
    }

    @Bean
    public Queue productUpdatedQueue() {
        return new Queue("product.updated.queue");
    }

    @Bean
    public Queue productDeletedQueue() {
        return new Queue("product.deleted.queue");
    }
    
}
