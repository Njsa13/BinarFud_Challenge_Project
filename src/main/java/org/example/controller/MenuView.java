package org.example.controller;

import org.example.model.OrderDetail;
import org.example.model.Product;

import java.util.List;

public class MenuView {
    public void loginMenuView() {
        System.out.println("+=====================+");
        System.out.println("| Welcome To BinarFud |");
        System.out.println("+=====================+\n");
        System.out.println("Pilihan :");
        System.out.println("1. Login");
        System.out.println("2. Daftar");
        System.out.println("0. Keluar\n");
        System.out.print("=> ");
    }

    public void loginView() {
        System.out.println("+===============+");
        System.out.println("|     Login     |");
        System.out.println("+===============+");
        System.out.println("Ketik 0 lalu enter pada kolom username atau password jika ingin kembali\n");
    }

    public void signUpView() {
        System.out.println("+================+");
        System.out.println("|     Daftar     |");
        System.out.println("+================+");
        System.out.println("Ketik 0 lalu enter pada kolom username atau password jika ingin kembali\n");
    }

    public void mainMenuView(List<Product> product) {
        System.out.println("+===================+");
        System.out.println("|    Daftar Menu    |");
        System.out.println("+===================+\n");
        System.out.println("Pilihan : ");
        for (int i = 0; i < product.size(); i++) {
            int productId = product.get(i).getProductId();
            String productName = product.get(i).getProductName();
            int price = product.get(i).getPrice();
            String category = product.get(i).getCategory();
            System.out.println(String.format("%d. %s -------- %d -------- %s", productId, productName, price, category));
        }
        product.clear();
        System.out.println("----------------------------------");
        System.out.println("99. Konfirmasi Dan Bayar");
        System.out.println("0. Keluar\n");
        System.out.print("=> ");
    }

    public void addToOrderListView(String productName, int price) {
        System.out.println("+================================+");
        System.out.println("|  Berapa Jumlah Pesanan Anda ?  |");
        System.out.println("+================================+\n");
        System.out.println(String.format("%s\t\t| %d", productName, price));
        System.out.println("(Input 0 untuk kembali)\n");
        System.out.print("Jumlah => ");
    }

    public void payAndConfirmView(List<Product> products, List<OrderDetail> orderDetails, int totalPrice) {
        System.out.println("+=======================+");
        System.out.println("|  Daftar Pesanan Anda  |");
        System.out.println("+=======================+\n");
        for (int i = 0; i < orderDetails.size(); i++) {
            int productId = orderDetails.get(i).getProductId();
            String productName = products.get(productId).getProductName();
            int price = products.get(productId).getPrice();
            int quantity = orderDetails.get(i).getQuantity();
            int subTotalPrice = orderDetails.get(i).getPriceSubTotal();
            System.out.println(String.format("%d. %s ---- Rp %,d x %d =====> %d", i+1, productName, price, quantity, subTotalPrice).replace(",","."));
        }
        System.out.println("------------------------------------------------------");
        System.out.println(String.format("Total harga : Rp %,d", totalPrice).replace(",", "."));
        System.out.println("\n1. Konfirmasi");
        System.out.println("99. Kembali");
        System.out.println("0. Keluar\n");
        System.out.print("=> ");
    }

    public void checkOutView() {
        System.out.println("+==========+");
        System.out.println("| CheckOut |");
        System.out.println("+==========+");
        System.out.println("Ketik 0 lalu enter pada kolom alamat jika ingin kembali\n");
    }
}
