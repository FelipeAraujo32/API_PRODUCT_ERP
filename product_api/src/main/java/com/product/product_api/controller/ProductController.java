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
import com.product.product_api.service.ProductService;


import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired 
    private ProductService service;

    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> findAllProducts(){

    var ProductDTO = service.findAllProduct().stream()
                .map(ProductDTO::new)
                .collect(Collectors.toList());
    return ResponseEntity.ok(ProductDTO);
    }
    
    @GetMapping("/{uuid}")
    public ResponseEntity<ProductDTO> findProduct(@PathVariable UUID uuid){
        
        var findProduct = service.findProduct(uuid);
        return ResponseEntity.ok(new ProductDTO(findProduct));
    }

    @PostMapping("/products")
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO){

        var createProduct = service.createProduct(productDTO.toProduct());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{uuid}")
                .buildAndExpand(createProduct.getUuid())
                .toUri();

        return ResponseEntity.created(location).body(new ProductDTO(createProduct));
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable UUID uuid, @Valid @RequestBody ProductDTO productDTO){

        var updateProduct = service.updateProduct(uuid, productDTO.toProduct());

        return ResponseEntity.status(HttpStatus.OK).body(new ProductDTO(updateProduct));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID uuid){
        service.deleteProduct(uuid);
        return ResponseEntity.noContent().build();
    }
}
