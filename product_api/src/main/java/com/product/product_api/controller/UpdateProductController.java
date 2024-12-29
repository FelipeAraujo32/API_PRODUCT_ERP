package com.product.product_api.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.product.product_api.dto.request.RequestProductModelDto;
import com.product.product_api.service.business_exception.NotFoundException;
import com.product.product_api.service.productservice.UpdateProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Product Controller Update", description = "Control responsible for deleted the product")
public class UpdateProductController {

    private final UpdateProductService productServiceUpdate;

    public UpdateProductController(UpdateProductService productServiceUpdate) {
        this.productServiceUpdate = productServiceUpdate;
    }

    @PutMapping("/{uuid}")
        @Operation(summary = "Update a product", description = "Update the data of an existing product based on its UUID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Product updated successfully"),
                        @ApiResponse(responseCode = "404", description = "Product not found"),
                        @ApiResponse(responseCode = "422", description = "Invalid product data provided")
        })
        public ResponseEntity<RequestProductModelDto> updateProduct(@PathVariable UUID uuid,
                        @Valid @RequestBody RequestProductModelDto productModelDTO) throws NotFoundException {
                            RequestProductModelDto updateProduct = productServiceUpdate.updateProductAndSyncInventory(uuid,
                                productModelDTO);
                return ResponseEntity.status(HttpStatus.OK).body(updateProduct);
        }
    
    
}
