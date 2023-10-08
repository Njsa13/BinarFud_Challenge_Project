package com.binarfud.binarfud_challenge4.controller;

public class Alert {
    public void wrongInputType() {
        System.out.println("--- Input Harus Berupa Angka ---\n");
    }

    public void notAvailableOption() {
        System.out.println("--- Pilihan Tidak Tersedia ---\n");
    }

    public void allErrorAndExceptionAlert() {
        System.out.println("--- Terjadi Kesalahan, Silahkan Inputkan Kembali ---\n");
    }

    public void loginFail() {
        System.out.println("--- Login Gagal ---\n");
    }

    public  void  loginSuccess() {
        System.out.println("--- Login Berhasil ---\n");
    }

    public void signUpFail() {
        System.out.println("--- Daftar Gagal ---\n");
    }

    public void signUpSuccess() {
        System.out.println("--- Daftar Berhasil ---\n");
    }

    public void wrongFormat() {
        System.out.println("\t\t--- Format Username atau Password atau Email Salah ---");
        System.out.println("| Username Harus Terdiri Dari Angka Atau Huruf, Minimal 4 Karakter |");
        System.out.println("| Password Harus Terdiri Dari Huruf Dan Angka, Minimal 6 Karakter  |\n");
    }

    public void usernameAvailable() {
        System.out.println("--- Username Sudah Dipakai, Mohon Buat Username Lain ---\n");
    }

    public void merchantAvailable() {
        System.out.println("--- Merchant Name Sudah Digunakan ---\n");
    }

    public void productAvailable() {
        System.out.println("--- Product Name Sudah Digunakan / Merchant Name Salah ---\n");
    }

    public void insertSuccess() {
        System.out.println("--- Insert Berhasil ---\n");
    }

    public void updateSuccess() {
        System.out.println("--- Update Berhasil ---\n");
    }

    public void quantityLimit() {
        System.out.println("--- Jumlah Tidak Boleh lebih dari 5 Dan Tidak Boleh Minus ---\n");
    }

    public void orderSuccess() {
        System.out.println("--- Pemesanan Berhasil ---\n");
    }
    public void orderFailed() {
        System.out.println("--- Pemesanan Gagal ---");
    }

    public void notOrder() {
        System.out.println("--- Anda Belum Pesan Menu, Pesan Terlebih Dahulu ---\n");
    }
}
