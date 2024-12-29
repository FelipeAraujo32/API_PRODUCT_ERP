package com.product.product_api.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.product.product_api.service.business_exception.NotFoundException;
import com.product.product_api.service.productservice.DeleteProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Product Controller Delete", description = "Control responsible for deleted the product")
public class DeleteProductController {

    private final DeleteProductService deleteProductService;

    public DeleteProductController(DeleteProductService deleteProductService) {
        this.deleteProductService = deleteProductService;
    }


    @DeleteMapping("/{uuid}")
        @Operation(summary = "Delete a product", description = "Delete an existing product based on its UUID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
                        @ApiResponse(responseCode = "404", description = "Product not found")
        })
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID uuid) throws NotFoundException {
                deleteProductService.deleteProduct(uuid);
                return ResponseEntity.noContent().build();
        }
}
