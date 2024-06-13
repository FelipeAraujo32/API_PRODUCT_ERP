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
import com.product.product_api.service.business_exception.BadRequestException;
import com.product.product_api.service.business_exception.NotFoundException;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Product COntroller", description = "ERP system product controller")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping()
    public ResponseEntity<List<ProductDTO>> findAllProducts() throws NotFoundException {

        var ProductDTO = service.findAllProduct().stream()
                .map(ProductDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ProductDTO);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ProductDTO> findProduct(@PathVariable UUID uuid) throws NotFoundException {

        var findProduct = service.findProduct(uuid);
        return ResponseEntity.ok(new ProductDTO(findProduct));
    }

    @PostMapping()
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO)
            throws BadRequestException {

        var createProduct = service.createProduct(productDTO.toProduct());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{uuid}")
                .buildAndExpand(createProduct.getUuid())
                .toUri();

        return ResponseEntity.created(location).body(new ProductDTO(createProduct));
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable UUID uuid,
            @Valid @RequestBody ProductDTO productDTO) throws BadRequestException {

        var updateProduct = service.updateProduct(uuid, productDTO.toProduct());

        return ResponseEntity.status(HttpStatus.OK).body(new ProductDTO(updateProduct));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID uuid) throws NotFoundException {
        service.deleteProduct(uuid);
        return ResponseEntity.noContent().build();
    }
}
