package org.example.service;

import org.example.model.Product;
import org.example.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

public class ProductServiceImpl implements ProductService {
    private List<Product> listProduct = new ArrayList<>();

    /**
     * Method untuk mengambil data dari repository
     */
    @Override
    public void setProductFromDatabase() {
        ProductRepository productRepository = new ProductRepository();
        for (int i = 0; i < productRepository.getIdProduct().size(); i++) {
            Product product = Product.builder()
                    .productId(Integer.parseInt(productRepository.getIdProduct().get(i)))
                    .productName(productRepository.getNamaProduct().get(i))
                    .price(Integer.parseInt(productRepository.getHargaProduct().get(i)))
                    .category(productRepository.getKategoriProduct().get(i))
                    .build();
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
     */
    @Override
    public String getProductNameById(int productId) {
        for (Product value : this.listProduct) {
            Integer id = value.getProductId();
            if (id.equals(productId)) {
                return value.getProductName();
            }
        }
        return null;
    }

    /**
     * Method untuk mengambil data harga
     * @param productId
     * @return
     */
    @Override
    public int getPriceById(int productId) {
        for (Product value : this.listProduct) {
            Integer id = value.getProductId();
            if (id.equals(productId)) {
                return value.getPrice();
            }
        }
        return 0;
    }

    /**
     * Method untuk mengambil data dari field
     * @return
     */
    @Override
    public List<Product> getProduct() {
        List<Product> list = new ArrayList<>();
        for (int i = 0; i < this.listProduct.size(); i++){
            Product product = Product.builder()
                    .productId(this.listProduct.get(i).getProductId())
                    .productName(this.listProduct.get(i).getProductName())
                    .price(this.listProduct.get(i).getPrice())
                    .category(this.listProduct.get(i).getCategory())
                    .build();
            list.add(product);
        }
        return list;
    }
}
