package com.product.product_api.messaging.producer;

import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.product.product_api.convert.ProductEventConvert;
import com.product.product_api.dto.ProductModelDto;
import com.product.product_api.entity.ProductModel;

@Component
public class InventoryProducer {

    private final RabbitTemplate rabbitTemplate;
    private final ProductEventConvert productEventConvert;

    public InventoryProducer(RabbitTemplate rabbitTemplate, ProductEventConvert productEventConvert) {
        this.rabbitTemplate = rabbitTemplate;
        this.productEventConvert = productEventConvert;
    }

    public void createProduct(ProductModel productModel, ProductModelDto productModelDto) {
        rabbitTemplate.convertAndSend(
                "product.exchange",
                "product.created",
                productEventConvert.toProductEventDTO(productModel, productModelDto));
    }

    public void updateProduct(ProductModel productModel, ProductModelDto productModelDto) {
        rabbitTemplate.convertAndSend(
                "product.exchange",
                "product.updated",
                productEventConvert.toProductEventDTO(productModel, productModelDto));
    }

    public void deleteProduct(UUID productId) {
        rabbitTemplate.convertAndSend(
                "product.exchange",
                "product.deleted",
                productId);
    }

}
