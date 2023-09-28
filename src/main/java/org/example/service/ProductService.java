package org.example.service;

import org.example.model.Product;

import java.util.List;

public interface ProductService {
    void setProductFromDatabase();
    void clearListProduct();
    String getProductNameById(int productId);
    int getPriceById(int productId);
    List<Product> getProduct();

}
