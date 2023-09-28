package org.example.service;

import org.example.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    void setProductFromDatabase();
    void clearListProduct();
    String getProductNameById(Integer productId);
    int getPriceById(Integer productId);
    int getIdByIndex(Integer productIndex);
    List<Product> getProduct();

}
