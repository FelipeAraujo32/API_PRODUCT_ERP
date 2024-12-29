package com.product.product_api.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.product.product_api.dto.response.ResponseProductModelDto;
import com.product.product_api.service.business_exception.NotFoundException;
import com.product.product_api.service.productservice.FindByProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Product Controller Find", description = "Control responsible for find the product")
public class FindByProductController {

        private final FindByProductService findByProductService;

        public FindByProductController(FindByProductService findByProductService) {
                this.findByProductService = findByProductService;
        }

        @GetMapping()
        @Operation(summary = "Get all products", description = "Retrieve a list of all registered products")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Operation successful"),
                        @ApiResponse(responseCode = "404", description = "List not found")
        })
        public ResponseEntity<List<ResponseProductModelDto>> findAllProducts() {
                List<ResponseProductModelDto> productModelDTO = findByProductService.findAllProduct();
                return ResponseEntity.ok(productModelDTO);
        }

        @GetMapping("/{uuid}")
        @Operation(summary = "Get a product by UUID", description = "Retrieve a specific product based on its UUID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Retrieve a specific product based on its UUID"),
                        @ApiResponse(responseCode = "404", description = "Product not found")
        })
        public ResponseEntity<ResponseProductModelDto> findByProduct(@PathVariable UUID uuid) throws NotFoundException {
                ResponseProductModelDto findByProduct = findByProductService.findByProduct(uuid);
                return ResponseEntity.ok(findByProduct);
        }
}
