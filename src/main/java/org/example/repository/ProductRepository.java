package org.example.repository;

import java.util.List;

public class ProductRepository {
    private DatabaseConfig databaseConfig = new DatabaseConfig();

    public List<String> getIdProduct() {
        String query = "SELECT id_produk FROM produk;";
        return databaseConfig.getData(query, "id_produk");
    }

    public List<String> getNamaProduct() {
        String query = "SELECT nama_produk FROM produk;";
        return databaseConfig.getData(query, "nama_produk");
    }

    public List<String> getKategoriProduct() {
        String query = "SELECT kategori FROM produk;";
        return databaseConfig.getData(query, "kategori");
    }

    public List<String> getHargaProduct() {
        String query = "SELECT harga FROM produk;";
        return databaseConfig.getData(query, "harga");
    }
}
