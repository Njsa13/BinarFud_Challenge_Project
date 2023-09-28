package org.example;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SetQuery {
    MyDb myDb = new MyDb();

    //method untuk mengambil data dari tabel makanan
    public List<String> getMenuData(String parameter) {
        String query = "SELECT * FROM makanan;";
        List<String> data = new ArrayList<>();
        data = myDb.getData(query, parameter);
        return data;
    }

    //method untuk mengambil data dari tabel makanan dengan parameter
    public String getMenuData(String parameter, int  idMakanan) {
        String query = "SELECT * FROM makanan WHERE id_makanan = "+idMakanan+";";
        List<String> data = new ArrayList<>();
        data = myDb.getData(query, parameter);
        return data.get(0);
    }

    //method untuk mengambil id user
    public List<String> getUserId(String username) {
        String query = "SELECT * FROM user WHERE username = '"+username+"';";
        List<String> data = new ArrayList<>();
        data = myDb.getData(query, "id_user");
        return data;
    }

    //method untuk mengecek username dan password ketika login
    public Boolean checkLoginData(String username, String  password) {
        String query = "SELECT * FROM user WHERE username = '"+username+"' AND password = '"+password+"'";
        List<String> data = new ArrayList<>();
        data = myDb.getData(query, "id_user");
        if (data.size()>0) {
            return true;
        }
        else {
            return false;
        }
    }

    //method untuk mengambil data menu yang dipesan untuk ditampilkan di menu konfirmasi dan pembayaran
    public  List<String> getDetailOrder(String parameter, String username) {
        String query = "SELECT nama_makanan, jumlah, subtotal FROM detail_pesanan LEFT JOIN makanan USING(id_makanan) WHERE id_user = "+getUserId(username).get(0)+" AND id_pesanan IS NULL;";
        List<String> data = new ArrayList<>();
        data = myDb.getData(query, parameter);
        return data;
    }

    //method untuk mengambil total harga
    public Integer getTotalHarga(String username, String parameter) {
        String query = "SELECT SUM(subtotal) AS total_harga FROM detail_pesanan WHERE id_user = "+getUserId(username).get(0)+";";
        List<String> data = new ArrayList<>();
        data = myDb.getData(query, parameter);
        return Integer.parseInt(data.get(0));
    }

    //method untuk mengambil id pesanan
    public Integer getIdPesanan() {
        String query = "SELECT MAX(id_pesanan) AS max_id FROM pesanan;";
        List<String> data = new ArrayList<>();
        data = myDb.getData(query, "max_id");
        return Integer.parseInt(data.get(0)) ;
    }

    //method untuk menambahkan data ke dalam tabel user
    public Boolean addUserData(String username, String password) {
        String query = "INSERT INTO user(username, password) VALUES('"+username+"','"+password+"')";
        return myDb.setData(query);
    }

    //method untuk menambahkan data menu yang dipesan
    public Boolean addDetailOrder(String username, String makanan, int harga, int jumlah, int idMakanan){
        String query = "INSERT INTO detail_pesanan(jumlah, subtotal, id_user, id_makanan) VALUES("+jumlah+","+harga*jumlah+","+getUserId(username).get(0)+","+idMakanan+")";
        return myDb.setData(query);
    }

    //method untuk menambahkan data kedalam tabel pesanan
    public Boolean addOrder(String alamat, String username) {
        String query = "INSERT INTO pesanan(total_harga, alamat) VALUES("+getTotalHarga(username, "total_harga")+",'"+alamat+"')";
        return  myDb.setData(query);
    }

    //method untuk mengupdate / menambahkan id pesanan kedalam data yang ada pada tabel detail_pesanan
    public Boolean updateDetailOrder(String username) {
        String query = "UPDATE detail_pesanan SET id_pesanan ="+getIdPesanan()+" WHERE id_user ="+getUserId(username).get(0)+" AND id_pesanan IS NULL;";
        return myDb.setData(query);
    }
}
