package org.example.service;

import org.example.model.Product;
import org.example.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProductServiceImpl implements ProductService {
    private List<Product> listProduct = new ArrayList<>();

    /**
     * Method untuk mengambil data dari repository
     */
    @Override
    public void setProductFromDatabase() {
        ProductRepository productRepository = new ProductRepository();
        for (int i = 0; i < productRepository.getProductId().size(); i++) {
            Product product = new Product(
                    Integer.parseInt(productRepository.getProductId().get(i)),
                    productRepository.getProductName().get(i),
                    Integer.parseInt(productRepository.getProductPrice().get(i)),
                    productRepository.getProductCategory().get(i)
            );
            this.listProduct.add(product);
        }
    }

    @Override
    public void clearListProduct() {
        listProduct.clear();
    }

    /**
     * Method untuk mengambil data nama produk
     * @param productId
     * @return
     * @throws NullPointerException
     */
    @Override
    public String getProductNameById(Integer productId) throws NullPointerException {
        return this.listProduct.stream()
                .filter(value -> productId.equals(value.getProductId()))
                .map(Product::getProductName)
                .findFirst()
                .orElse("Nama Kosong");
    }

    /**
     * Method untuk mengambil data harga
     * @param productId
     * @return
     * @throws NullPointerException
     */
    @Override
    public int getPriceById(Integer productId) throws NullPointerException {
        return this.listProduct.stream()
                .filter(value -> productId.equals(value.getProductId()))
                .map(Product::getPrice)
                .findFirst()
                .orElse(0);
    }

    /**
     * Method ini digunakan untuk mendapatkan id produk berdasarkan index list
     * @param productIndex
     * @return
     * @throws NullPointerException
     * @throws IndexOutOfBoundsException
     */
    @Override
    public int getIdByIndex(Integer productIndex) throws NullPointerException, IndexOutOfBoundsException {
        return Optional.ofNullable(this.listProduct)
                .map(value -> value.get(productIndex-1).getProductId())
                .orElse(0);
    }

    /**
     * Method untuk mengambil data dari field
     * @return
     */
    @Override
    public List<Product> getProduct() {
        return this.listProduct.stream()
                .map(value -> new Product(value.getProductId(),
                                value.getProductName(),
                                value.getPrice(),
                                value.getCategory()))
                .collect(Collectors.toList());
    }
}
