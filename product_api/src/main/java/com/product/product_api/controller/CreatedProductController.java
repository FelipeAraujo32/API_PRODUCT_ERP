package com.product.product_api.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.product.product_api.dto.request.RequestProductModelDto;
import com.product.product_api.service.productservice.CreatedProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Product Controller Created", description = "Control responsible for creating the product")
public class CreatedProductController {

    private final CreatedProductService createdProductService;

    public CreatedProductController(CreatedProductService createdProductService) {
        this.createdProductService = createdProductService;
    }

    @PostMapping()
    @Operation(summary = "Create a new product", description = "Create a new product and return the created product's data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "422", description = "Invalid product data provided")
    })
    public ResponseEntity<RequestProductModelDto> createProduct(@Valid @RequestBody RequestProductModelDto productDTO) {
        RequestProductModelDto createProduct = createdProductService.createProductAndNotifyInventory(productDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{uuid}")
                .buildAndExpand(createProduct.getProductId())
                .toUri();
        return ResponseEntity.created(location).body(createProduct);
    }
}
