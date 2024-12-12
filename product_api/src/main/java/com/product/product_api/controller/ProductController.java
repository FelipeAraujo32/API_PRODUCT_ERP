package com.product.product_api.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

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

import com.product.product_api.dto.ProductModelDto;
import com.product.product_api.service.business_exception.NotFoundException;
import com.product.product_api.service.productservice.ProductService;
import com.product.product_api.service.productservice.ProductServiceCreated;
import com.product.product_api.service.productservice.ProductServiceUpdate;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Product Controller", description = "ERP system product controller")
public class ProductController {

        private final ProductService productService;
        private final ProductServiceCreated productServceCreated;
        private final ProductServiceUpdate productServiceUpdate;

        public ProductController(ProductService productService, ProductServiceCreated productServceCreated,
                        ProductServiceUpdate productServiceUpdate) {
                this.productService = productService;
                this.productServceCreated = productServceCreated;
                this.productServiceUpdate = productServiceUpdate;
        }

        @GetMapping()
        @Operation(summary = "Get all products", description = "Retrieve a list of all registered products")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Operation successful"),
                        @ApiResponse(responseCode = "404", description = "List not found")
        })
        public ResponseEntity<List<ProductModelDto>> findAllProducts() {
                List<ProductModelDto> productModelDTO = productService.findAllProduct();
                return ResponseEntity.ok(productModelDTO);
        }

        @GetMapping("/{uuid}")
        @Operation(summary = "Get a product by UUID", description = "Retrieve a specific product based on its UUID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Retrieve a specific product based on its UUID"),
                        @ApiResponse(responseCode = "404", description = "Product not found")
        })
        public ResponseEntity<ProductModelDto> findByProduct(@PathVariable UUID uuid) throws NotFoundException {
                ProductModelDto findByProduct = productService.findByProduct(uuid);
                return ResponseEntity.ok(findByProduct);
        }

        @PostMapping()
        @Operation(summary = "Create a new product", description = "Create a new product and return the created product's data")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Product created successfully"),
                        @ApiResponse(responseCode = "422", description = "Invalid product data provided")
        })
        public ResponseEntity<ProductModelDto> createProduct(@Valid @RequestBody ProductModelDto productDTO) {
                ProductModelDto createProduct = productServceCreated.createProductAndNotifyInventory(productDTO);
                URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                                .path("/{uuid}")
                                .buildAndExpand(createProduct.getProductId())
                                .toUri();
                return ResponseEntity.created(location).body(createProduct);
        }

        @PutMapping("/{uuid}")
        @Operation(summary = "Update a product", description = "Update the data of an existing product based on its UUID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Product updated successfully"),
                        @ApiResponse(responseCode = "404", description = "Product not found"),
                        @ApiResponse(responseCode = "422", description = "Invalid product data provided")
        })
        public ResponseEntity<ProductModelDto> updateProduct(@PathVariable UUID uuid,
                        @Valid @RequestBody ProductModelDto productModelDTO) throws NotFoundException {
                ProductModelDto updateProduct = productServiceUpdate.updateProductAndSyncInventory(uuid,
                                productModelDTO);
                return ResponseEntity.status(HttpStatus.OK).body(updateProduct);
        }

        @DeleteMapping("/{uuid}")
        @Operation(summary = "Delete a product", description = "Delete an existing product based on its UUID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
                        @ApiResponse(responseCode = "404", description = "Product not found")
        })
        public ResponseEntity<Void> deleteProduct(@PathVariable UUID uuid) throws NotFoundException {
                productService.deleteProduct(uuid);
                return ResponseEntity.noContent().build();
        }
}
