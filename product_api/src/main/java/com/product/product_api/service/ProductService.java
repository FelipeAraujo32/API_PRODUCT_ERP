package com.product.product_api.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.product.product_api.entity.Product;
import com.product.product_api.repository.ProductRepository;
import com.product.product_api.service.business_exception.BadRequestException;
import com.product.product_api.service.business_exception.NotFoundException;

@Service
public class ProductService {

    private final ValidationDataProduct dataProduct;
    private final ProductRepository repository;
    private final Logger logger = LoggerFactory.getLogger(ProductService.class);

    public ProductService(ValidationDataProduct dataProduct, ProductRepository repository) {
        this.dataProduct = dataProduct;
        this.repository = repository;
    }

    /**
     * @return returns a list of products saved in the db.
     * @throws NotFoundException If the list returns empty throws an exception along
     *                           with a logger.
     */
    public List<Product> findAllProduct() throws NotFoundException {
        List<Product> products = repository.findAll();
        if (products.isEmpty()) {
            logger.info("No products found");
            throw new NotFoundException("Product list not found");
        }
        return products;
    }

    /**
     * 
     * @param uuid UUID parameter for specific product location.
     * @return Only returns the search for the product passed by UUID if it exists in the db.
     * @throws NotFoundException If the UUID passed by the user does not exist in the DB, the exception will be thrown.
     */
    public Optional<Product> findProduct(UUID uuid) throws NotFoundException {

        Optional<Product> optProduct = repository.findById(uuid);
        if (optProduct.isEmpty()) {
            logger.info("No products found");
            throw new NotFoundException("No products found");
        }
        return repository.findById(uuid);
    }

    public Product createProduct(Product product) throws BadRequestException {
        dataProduct.validation(product);
        return repository.save(product);
    }

    public Product updateProduct(UUID uuid, Product product) throws BadRequestException {

        Optional<Product> optProduct = repository.findById(uuid);
        if (optProduct.isEmpty()) {
            logger.info("No products found");
            throw new NotFoundException("Product not found");
        }
        dataProduct.validation(product);
        return repository.save(product);
    }

    public void deleteProduct(UUID uuid) throws NotFoundException {

        Optional<Product> optProduct = repository.findById(uuid);
        if (optProduct.isEmpty()) {
            logger.info("No products found");
            throw new NotFoundException("Product not found");
        }
        repository.deleteById(uuid);
    }

}
