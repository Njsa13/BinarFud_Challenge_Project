package org.example;
import java.util.List;S

public class Main {
    public static void main(String[] args) {
        Menu menu = new Menu();
        SetQuery setQuery = new SetQuery();
        List<String> makanan = setQuery.getMenuData("nama_makanan");
        String username = "";
        boolean stat = false;

        //loop untuk menampilkann menu login dan daftar
        do {
            switch (menu.loginMenu()) {
                case 1 :
                    clearConsole();
                    String pilihan = menu.login();
                    stat = login(!pilihan.equals(""));  //login
                    username = pilihan;
                    break;
                case 2 :
                    clearConsole();
                    daftar(menu.daftar());  //daftar
                    stat = true;
                    break;
                case 3 :
                    clearConsole();
                    menu.closeScanner();    //keluar
                    return;
                default:
                    clearConsole(); //pilihan tidak tersedia
                    wrongOption();
                    stat = true;
                    break;
            }
        } while (stat);

        //loop untuk menampilkan main menu
        do {
            Integer pilihan = menu.mainMenu();
            if (pilihan.equals(99)) {           //menu konfirmasi & pembayaran
                do {
                    switch (menu.konfirPemb(username)){
                        case 1 :
                            clearConsole();
                            confirm(menu.konfirmasi(username)); //Konfirmasi dan bayar
                            stat = false;
                            break;
                        case 2 :                //Kembali ke menu utama
                            clearConsole();
                            stat = false;
                            break;
                        case 0 :                //keluar
                            clearConsole();
                            menu.closeScanner();
                            return;
                        default:
                            clearConsole();     //Pilihan tisak tersedia
                            wrongOption();
                            stat = true;
                            break;
                    }
                } while (stat);
                stat = true;
            } else if (pilihan.equals(0)) {     //keluar
                clearConsole();
                menu.closeScanner();
                return;
            } else if ((pilihan <= makanan.size()) && (pilihan > 0)) {      //Menu untuk menginputkan jumlah pesanan
                clearConsole();
                quantity(menu.tambahPesanan(username, pilihan));
                stat = true;
            } else {
                clearConsole();     //Pilihan tisak tersedia
                wrongOption();
                stat = true;
            }
        } while (stat);

    }

    //Hapus console
    public static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    //alert login
    public static boolean login(boolean loginParameter) {
        if (loginParameter) {
            System.out.println("Login Berhasil!");
            return false;
        } else {
            System.out.println("Login Gagal!");
            return true;
        }
    }

    //alert daftar
    public static void daftar(boolean daftarParameter) {
        if (daftarParameter) {
            System.out.println("Daftar Berhasil!");
        } else {
            System.out.println("Daftar Gagal!\nUsername sudah digunakan!");
        }
    }

    //alert pilihan tidak tersedia
    public static void wrongOption() {
        System.out.println("Pilihan tidak tersedia");
    }

    //alert pemesanan berhasil
    public static void quantity(boolean quantityParameter) {
        if (quantityParameter) {
            System.out.println("Pemesanan berhasil!");
        }
    }

    //alert pembayaran berhasil
    public static void confirm(boolean confirmParameter) {
        if (confirmParameter) {
            System.out.println("Pembayaran Berhasil!");
        }
    }
}
