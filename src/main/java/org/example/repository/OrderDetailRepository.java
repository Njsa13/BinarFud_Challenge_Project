package org.example.repository;

import java.util.List;

public class OrderDetailRepository {
    private DatabaseConfig databaseConfig = new DatabaseConfig();

    public List<String> getQuantityByUserIdAndOrderId(int userId) {
        String query = String.format("SELECT jumlah FROM detail_pesanan WHERE id_user = '%s' AND id_pesanan IS NULL", userId);
        return databaseConfig.getData(query, "jumlah");
    }

    public List<String> getSubTotalByUserIdAndOraderId(int userId) {
        String query = String.format("SELECT subtotal FROM detail_pesanan WHERE id_user = '%s' AND id_pesanan IS NULL", userId);
        return databaseConfig.getData(query, "subtotal");
    }

    public List<String> getProductIdByUserIdAndOrderId(int userId) {
        String query = String.format("SELECT id_produk FROM detail_pesanan WHERE id_user = '%s' AND id_pesanan IS NULL", userId);
        return databaseConfig.getData(query, "id_produk");
    }

    public boolean addOrderDetail(int productId, int userId, int quantity, int subTotal) {
        String query = "INSERT INTO detail_pesanan(id_produk, id_user, jumlah, subtotal) VALUES(?, ?, ?, ?);";
        Object[] parameters = new Object[4];
        parameters[0] = productId;
        parameters[1] = userId;
        parameters[2] = quantity;
        parameters[3] = subTotal;
        return databaseConfig.setData(query, parameters);
    }

    public boolean updateOrderIdByUserIdAndOrderId(int orderId, int userId) {
        String query = "UPDATE detail_pesanan SET id_pesanan = ? WHERE id_user = ? AND id_pesanan IS NULL;";
        Object[] parameters = new Object[2];
        parameters[0] = orderId;
        parameters[1] = userId;
        return databaseConfig.setData(query, parameters);
    }

    public boolean updateQuantity(int userId, int quantity, int productId) {
        String query = "UPDATE detail_pesanan SET jumlah = ? WHERE id_user = ? AND id_pesanan IS NULL AND id_produk = ?;";
        Object[] parameters = new Object[3];
        parameters[0] = quantity;
        parameters[1] = userId;
        parameters[2] = productId;
        return databaseConfig.setData(query, parameters);
    }
}
