package org.example.controller;

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

    public void wrongUsernameFormat() {
        System.out.println("--- Username Harus Terdiri Dari Angka Atau Huruf, Minimal 4 Karakter ---\n");
    }

    public void wrongPasswordFormat() {
        System.out.println("--- Password Harus Terdiri Dari Huruf Dan Angka, Minimal 6 Karakter ---\n");
    }

    public void usernameAvailable() {
        System.out.println("--- Username Sudah Dipakai, Mohon Buat Username Lain ---\n");
    }

    public void quantityLimit() {
        System.out.println("--- Jumlah Tidak Boleh lebih dari 5 Dan Tidak Boleh Minus ---\n");
    }

    public void orderSuccess() {
        System.out.println("--- Pemesanan Berhasil ---\n");
    }

    public void notOrder() {
        System.out.println("--- Anda Belum Pesan Menu, Pesan Terlebih Dahulu ---\n");
    }
}
