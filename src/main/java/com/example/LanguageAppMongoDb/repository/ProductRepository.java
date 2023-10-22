package com.example.LanguageAppMongoDb.repository;

import com.example.LanguageAppMongoDb.model.product.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {

}
