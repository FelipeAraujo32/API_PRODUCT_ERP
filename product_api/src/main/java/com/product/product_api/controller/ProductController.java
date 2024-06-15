package com.product.product_api.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.product.product_api.dto.ProductDTO;
import com.product.product_api.dto.ProductResponseDTO;
import com.product.product_api.service.ProductService;
import com.product.product_api.service.business_exception.NotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Product COntroller", description = "ERP system product controller")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping()
    @Operation(summary = "Get all products", description = "Retrieve a list of all registered products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful"),
            @ApiResponse(responseCode = "404", description = "List not found")
    })
    public ResponseEntity<List<ProductResponseDTO>> findAllProducts() {

        var ProductResponseDTO = service.findAllProduct().stream()
                .map(ProductResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ProductResponseDTO);
    }

    @GetMapping("/{uuid}")
    @Operation(summary = "Get a product by UUID", description = "Retrieve a specific product based on its UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieve a specific product based on its UUID"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<ProductResponseDTO> findProduct(@PathVariable UUID uuid) throws NotFoundException {

        var findProduct = service.findById(uuid);
        return ResponseEntity.ok(new ProductResponseDTO(findProduct));
    }

    @PostMapping()
    @Operation(summary = "Create a new product", description = "Create a new product and return the created product's data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "422", description = "Invalid product data provided")
    })
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {

        var createProduct = service.createProduct(productDTO.toProduct());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{uuid}")
                .buildAndExpand(createProduct.getUuid())
                .toUri();
        return ResponseEntity.created(location).body(new ProductResponseDTO(createProduct));
    }

    @PutMapping("/{uuid}")
    @Operation(summary = "Update a product", description = "Update the data of an existing product based on its UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "422", description = "Invalid product data provided")
    })
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable UUID uuid,
            @Valid @RequestBody ProductDTO productDTO) throws NotFoundException {

        var updateProduct = service.updateProduct(uuid, productDTO.toProduct());

        return ResponseEntity.status(HttpStatus.OK).body(new ProductResponseDTO(updateProduct));
    }

    @DeleteMapping("/{uuid}")
    @Operation(summary = "Delete a product", description = "Delete an existing product based on its UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID uuid) throws NotFoundException {
        service.deleteProduct(uuid);
        return ResponseEntity.noContent().build();
    }
}
