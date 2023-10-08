package com.binarfud.binarfud_challenge4.controller;

import com.binarfud.binarfud_challenge4.dto.OrderDetailPageDTO;
import com.binarfud.binarfud_challenge4.dto.ProductDTO;
import com.binarfud.binarfud_challenge4.dto.ProductPageDTO;

public class UserView {
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

    public void userMainMenuView1(ProductPageDTO productPageDTO) {
        System.out.println("+===========+");
        System.out.println("| Main Menu |");
        System.out.println("+===========+\n");
        System.out.println("Pilihan :");
        for (int i = 0; i < productPageDTO.getProductDTOS().size(); i++) {
            System.out.printf("%d. %s --- %d --- %s%n", i+1,
                    productPageDTO.getProductDTOS().get(i).getProductName(),
                    productPageDTO.getProductDTOS().get(i).getPrice(),
                    productPageDTO.getProductDTOS().get(i).getMerchantName());
        }
        if (productPageDTO.getTotalPages().equals(1)) {
            System.out.println("\nHalaman yang tersedia < 1 >");
            System.out.print("Pilih Halaman, pilih 0 untuk lanjut =>");
        }
        if (productPageDTO.getTotalPages() > 1) {
            System.out.printf("\nHalaman yang tersedia < 1-%d >%n", productPageDTO.getTotalPages());
            System.out.print("Pilih Halaman, pilih 0 untuk lanjut =>");
        }
        if(productPageDTO.getTotalPages().equals(0)) {
            System.out.println("--- Data Kosong ---");
        }
    }

    public void payAndConfirmView1(OrderDetailPageDTO orderDetailPageDTO) {
        System.out.println("+=================+");
        System.out.println("| Pay And Confirm |");
        System.out.println("+=================+\n");
        System.out.println("Pilihan :");
        for (int i = 0; i < orderDetailPageDTO.getOrderListDTOS().size(); i++) {
            System.out.printf("%d. %s --- %s --- %d x %d ------> %d %n", i+1,
                    orderDetailPageDTO.getOrderListDTOS().get(i).getProductName(),
                    orderDetailPageDTO.getOrderListDTOS().get(i).getMerchantName(),
                    orderDetailPageDTO.getOrderListDTOS().get(i).getPrice(),
                    orderDetailPageDTO.getOrderListDTOS().get(i).getQuantity(),
                    orderDetailPageDTO.getOrderListDTOS().get(i).getSubtotalPrice());
        }
        if (orderDetailPageDTO.getTotalPages().equals(1)) {
            System.out.println("\nHalaman yang tersedia < 1 >");
            System.out.print("Pilih Halaman, pilih 0 untuk lanjut =>");
        }
        if (orderDetailPageDTO.getTotalPages() > 1) {
            System.out.printf("\nHalaman yang tersedia < 1-%d >%n", orderDetailPageDTO.getTotalPages());
            System.out.print("Pilih Halaman, pilih 0 untuk lanjut =>");
        }
        if(orderDetailPageDTO.getTotalPages().equals(0)) {
            System.out.println("--- Data Kosong ---");
        }
    }

    public void userMainMenuView2() {
        System.out.println("99. Konfirmasi dan bayar");
        System.out.println("999. Pilih halaman");
        System.out.println("0. Kembali");
        System.out.print("Pilih menu =>");
    }

    public void payAndConfirmView2() {
        System.out.println("99. Checkout");
        System.out.println("999. Pilih halaman");
        System.out.println("0. Kembali");
        System.out.print("Pilih menu =>");
    }

    public void addToOrderListView(ProductDTO productDTO) {
        System.out.println("---------------------------------------------------------");
        System.out.printf("Product Name : %s%n", productDTO.getProductName());
        System.out.printf("Product Price : %d%n", productDTO.getPrice());
        System.out.printf("Merchant Name : %s%n", productDTO.getMerchantName());
        System.out.println("---------------------------------------------------------");
        System.out.println("Ketik 0 untuk kembali");
        System.out.print("Jumlah =>");
    }

    public void checkOutView() {
        System.out.println("+==========+");
        System.out.println("| CheckOut |");
        System.out.println("+==========+");
        System.out.println("Ketik 0 lalu enter pada kolom alamat jika ingin kembali\n");
    }
}
