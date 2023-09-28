package org.example.repository;

import java.util.List;

public class OrderRepository {
    private DatabaseConfig databaseConfig = new DatabaseConfig();

    public List<String> getMaxOrderId() {
        String query = "SELECT MAX(id_pesanan) AS max_id FROM pesanan;";
        return databaseConfig.getData(query, "max_id");
    }

    public boolean addOrder(String address, int totalPrice) {
        String query = "INSERT INTO pesanan(alamat, total_harga) VALUES(?, ?);";
        Object[] parameters = new Object[2];
        parameters[0] = address;
        parameters[1] = totalPrice;
        return databaseConfig.setData(query, parameters);
    }
}
