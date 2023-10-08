package com.binarfud.binarfud_challenge4.controller;

import com.binarfud.binarfud_challenge4.dto.*;

public class AdminView {
    public void adminMainMenuView() {
        System.out.println("+=================+");
        System.out.println("| Admin Main Menu |");
        System.out.println("+=================+\n");
        System.out.println("Pilihan :");
        System.out.println("1. Edit Merchant");
        System.out.println("2. Edit Produk");
        System.out.println("3. Edit User");
        System.out.println("0. Keluar\n");
        System.out.print("=> ");
    }

    public void merchantListMenuView(MerchantPageDTO merchantPageDTO) {
        System.out.println("+===============+");
        System.out.println("| Edit Merchant |");
        System.out.println("+===============+\n");
        System.out.println("Pilihan :");
        for (int i = 0; i < merchantPageDTO.getMerchantDTOS().size(); i++) {
            System.out.printf("%d. %s --- %s --- %s%n", i+1,
                    merchantPageDTO.getMerchantDTOS().get(i).getMerchantName(),
                    merchantPageDTO.getMerchantDTOS().get(i).getMerchantLocation(),
                    merchantPageDTO.getMerchantDTOS().get(i).getOpen() ? "open" : "close");
        }
        if (merchantPageDTO.getTotalPages().equals(1)) {
            System.out.println("Halaman yang tersedia < 1 >");
            System.out.print("Pilih Halaman, pilih 0 untuk lanjut =>");
        }
        if (merchantPageDTO.getTotalPages() > 1) {
            System.out.printf("Halaman yang tersedia < 1-%d >%n", merchantPageDTO.getTotalPages());
            System.out.print("Pilih Halaman, pilih 0 untuk lanjut =>");
        }
        if(merchantPageDTO.getTotalPages().equals(0)) {
            System.out.println("--- Data Kosong ---");
        }
    }

    public void productListMenuView(ProductPageDTO productPageDTO) {
        System.out.println("+==============+");
        System.out.println("| Edit Product |");
        System.out.println("+==============+\n");
        System.out.println("Pilihan :");
        for (int i = 0; i < productPageDTO.getProductDTOS().size(); i++) {
            System.out.printf("%d. %s --- %d --- %s%n", i+1,
                    productPageDTO.getProductDTOS().get(i).getProductName(),
                    productPageDTO.getProductDTOS().get(i).getPrice(),
                    productPageDTO.getProductDTOS().get(i).getMerchantName());
        }
        if (productPageDTO.getTotalPages().equals(1)) {
            System.out.println("Halaman yang tersedia < 1 >");
            System.out.print("Pilih Halaman, pilih 0 untuk lanjut =>");
        }
        if (productPageDTO.getTotalPages() > 1) {
            System.out.printf("Halaman yang tersedia < 1-%d >%n", productPageDTO.getTotalPages());
            System.out.print("Pilih Halaman, pilih 0 untuk lanjut =>");
        }
        if(productPageDTO.getTotalPages().equals(0)) {
            System.out.println("--- Data Kosong ---");
        }
    }

    public void userListMenuView(UserPageDTO userPageDTO) {
        System.out.println("+===========+");
        System.out.println("| Edit User |");
        System.out.println("+===========+\n");
        System.out.println("Pilihan :");
        for (int i = 0; i <userPageDTO.getUserDTOS().size(); i++) {
            System.out.printf("%d. %s --- %s --- %s%n", i+1,
                    userPageDTO.getUserDTOS().get(i).getUsername(),
                    userPageDTO.getUserDTOS().get(i).getPassword(),
                    userPageDTO.getUserDTOS().get(i).getEmail());
        }
        if (userPageDTO.getTotalPages().equals(1)) {
            System.out.println("Halaman yang tersedia < 1 >");
            System.out.print("Pilih Halaman, pilih 0 untuk lanjut =>");
        }
        if (userPageDTO.getTotalPages() > 1) {
            System.out.printf("Halaman yang tersedia < 1-%d >%n", userPageDTO.getTotalPages());
            System.out.print("Pilih Halaman, pilih 0 untuk lanjut =>");
        }
        if(userPageDTO.getTotalPages().equals(0)) {
            System.out.println("--- Data Kosong ---");
        }
    }

    public void menuView() {
        System.out.println("99. Insert data");
        System.out.println("999. Pilih halaman");
        System.out.println("0. Kembali");
        System.out.print("Pilih menu =>");
    }

    public void insertMerchantView() {
        System.out.println("+=================+");
        System.out.println("| Insert Merchant |");
        System.out.println("+=================+");
        System.out.println("Ketik 0 untuk kembali\n");
    }

    public void editMerchantMenuView(MerchantDTO merchantDTO) {
        System.out.println("---------------------------------------------------------");
        System.out.printf("Merchant Name : %s%n", merchantDTO.getMerchantName());
        System.out.printf("Merchant Location : %s%n", merchantDTO.getMerchantLocation());
        System.out.printf("Merchant Location : %s%n", merchantDTO.getOpen() ? "open" : "close");
        System.out.println("---------------------------------------------------------");
        System.out.println("1. Update data");
        System.out.println("2. Update Status open");
        System.out.println("3. Update Status close");
        System.out.println("4. Delete data");
        System.out.println("0. Kembali");
        System.out.println("Pilihan =>");
    }

    public void editProductMenuView(ProductDTO productDTO) {
        System.out.println("---------------------------------------------------------");
        System.out.printf("Product Name : %s%n", productDTO.getProductName());
        System.out.printf("Product Price : %d%n", productDTO.getPrice());
        System.out.printf("Merchant Name : %s%n", productDTO.getMerchantName());
        System.out.println("---------------------------------------------------------");
        System.out.println("1. Update data");
        System.out.println("2. Delete data");
        System.out.println("0. Kembali");
        System.out.println("Pilihan =>");
    }

    public void editUserMenuVIew(UserDTO userDTO) {
        System.out.println("---------------------------------------------------------");
        System.out.printf("Username : %s%n", userDTO.getUsername());
        System.out.printf("Price : %s%n", userDTO.getPassword());
        System.out.printf("Email : %s%n", userDTO.getEmail());
        System.out.println("---------------------------------------------------------");
        System.out.println("1. Update data");
        System.out.println("2. Delete data");
        System.out.println("0. Kembali");
        System.out.println("Pilihan =>");
    }
}
