package org.example.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatabaseConfig {
    private static final String URL = "jdbc:mysql://localhost:3306/binarfud";
    private static final String USER = "root";
    private static final String PASSWORD = System.getProperty("database.password");

    /**
     * Method untuk mengambil data dari database
     * @param query
     * @param parameter
     * @return
     */
    protected List<String> getData(String query, String parameter) {
        try (
                Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                ) {
            List<String> listData = new ArrayList<>();
            while (resultSet.next()) {
                listData.add(resultSet.getString(parameter));
            }
            return listData;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("--- Error: Gagal Mengambil Data Dari Database ---");
            return Collections.emptyList();
        }
    }

    /**
     * Method untuk mengeset (Update dan Insert) data ke dalam database
     * @param query
     * @param parameters
     * @return
     */
    protected boolean setData(String query, Object[] parameters) {
        try (
                Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ) {
            int parameterIndex = 1;
            for (Object parameter : parameters) {
                if (parameter instanceof String) {
                    preparedStatement.setString(parameterIndex, (String) parameter);
                } else if (parameter instanceof Integer) {
                    preparedStatement.setInt(parameterIndex, (Integer) parameter);
                }
                parameterIndex++;
            }
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("--- Error: Gagal Memasukan/Memperbarui Data Ke Dalam Database ---");
            return false;
        }
    }
}
