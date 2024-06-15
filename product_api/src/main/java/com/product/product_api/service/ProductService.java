package com.product.product_api.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.product.product_api.entity.Product;
import com.product.product_api.repository.ProductRepository;
import com.product.product_api.service.business_exception.NotFoundException;

@Service
public class ProductService {

    private final ValidationDataProduct dataProduct;
    private final ProductRepository repository;

    public ProductService(ValidationDataProduct dataProduct, ProductRepository repository) {
        this.dataProduct = dataProduct;
        this.repository = repository;
    }

    /**
     * @return returns a list of products saved in the db.
     * @throws NotFoundException If the list returns empty throws an exception
     *                           along.
     */
    public List<Product> findAllProduct() throws NotFoundException {
        var products = repository.findAll();
        if (products.isEmpty()) {
            throw new NotFoundException("Products list not found");
        }
        return products;
    }

    /**
     * 
     * @param uuid UUID parameter for specific product location.
     * @return Only returns the search for the product passed by UUID if it exists
     *         in the db.
     * @throws NoSuchElementException Returns the exception if it doesn't find the
     *                                correct element
     */
    public Product findById(UUID uuid) {
        return repository.findById(uuid).orElseThrow(NoSuchElementException::new);
    }

    /**
     * 
     * @param product Receives the product object passed by the client to create and
     *                persist the product in the DB.
     * @return Returns the product created and saved in the DB if everything is ok.
     */
    public Product createProduct(Product product) {
        // Validator setting in an external class
        dataProduct.validation(product);
        return repository.save(product);
    }

    /**
     * 
     * @param uuid    receives the uuid parameter to search the database to check if
     *                it exists in the db.
     * @param product If found in the db, it updates the db with the data passed by
     *                the client.
     * @return If the operation is successful, the product is returned.
     */
    public Product updateProduct(UUID uuid, Product product) {

        Product productDb = this.findById(uuid);
        if (!productDb.getUuid().equals(uuid)) {
            throw new NotFoundException("The UUID must be the same as the one you want to update");
        }
        // Validator setting in an external class
        dataProduct.validation(product);
        product.setUuid(uuid);
        return repository.save(product);
    }

    /**
     * 
     * @param uuid receives the uuid parameter to search the database to check if
     *             it exists in the db.
     */
    public void deleteProduct(UUID uuid) {
        Product productDb = this.findById(uuid);
        this.repository.delete(productDb);
    }

}
