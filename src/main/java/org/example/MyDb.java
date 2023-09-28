package org.example;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class MyDb {
    //Deklarasi variabel untuk menghubungkan ke database
    private static final String url = "jdbc:mysql://localhost:3306/binarfud"; //Url
    private static final String user = "root"; //User
    private static final String password = ""; //Password
    private Connection connection;

    //Menghubungkan ke database
    public void openConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");     //Menghubungkan ke database menggunakan JDBC Driver
            connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    //Menutup koneksi database
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();     //Tutup koneksi
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Mengambil data dari database
    public List<String> getData(String query, String parameter) {
        try {
            List<String> listData = new ArrayList<>();
            openConnection();
            Statement statement = connection.createStatement();     //Mengirim pernyataan SQL ke database
            ResultSet resultSet = statement.executeQuery(query);    //Mengembalikan hasil ke variabel resultSet
            while (resultSet.next()) {
                String data = resultSet.getString(parameter);   //Memasukan data ke dalam list
                listData.add(data);
            }
            resultSet.close();
            statement.close();
            closeConnection();

            return listData;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Untuk Menginputkan atau Mengubah data pada database
    public Boolean setData(String query) {
        try {
            openConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            if (preparedStatement.executeUpdate() > 0) {    //Mengeksekusi query
                return true;
            }
            preparedStatement.close();
            closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
