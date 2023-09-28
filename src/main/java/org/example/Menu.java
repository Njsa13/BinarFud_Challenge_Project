package org.example;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Menu {
    SetQuery setQuery = new SetQuery();
    Scanner input = new Scanner(System.in);

    //Method  main menu
    public Integer mainMenu() {
        List<String> makanan = setQuery.getMenuData("nama_makanan");
        List<String > harga = setQuery.getMenuData("harga");

        System.out.println("+=====================+");
        System.out.println("| Welcome To BinarFud |");
        System.out.println("+=====================+\n");
        System.out.println("Silahkan pilih menu :");
        for (int i = 0; i < makanan.size(); i++) {      //menampilkan menu dan harga
            System.out.print((i+1)+". "+makanan.get(i)+" -----> ");
            System.out.println("Rp "+String.format("%,d",Integer.parseInt(harga.get(i))).replace(",","."));
        }
        System.out.println("99. Pesan dan Bayar");
        System.out.println("0. Keluar Aplikasi\n");
        System.out.print("=> ");

        int pilihan = Integer.parseInt(getInput());
        return pilihan;
    }

    //Method  menu login dan daftar
    public Integer loginMenu() {
        Scanner input = new Scanner(System.in);
        System.out.println("+=====================+");
        System.out.println("| Welcome To BinarFud |");
        System.out.println("+=====================+\n");
        System.out.println("Pilihan :");
        System.out.println("1. Login");
        System.out.println("2. Daftar");
        System.out.println("3. Keluar\n");
        System.out.print("=> ");

        int pilihan = Integer.parseInt(getInput());
        return pilihan;
    }

    //method login
    public String login() {
        System.out.println("+=======+");
        System.out.println("| Login |");
        System.out.println("+=======+\n");
        System.out.print("Masukan Username : ");
        String username = getInput();
        System.out.print("Masukan Password : ");
        String password = getInput();

        if (setQuery.checkLoginData(username, password)) {
            return username;
        } else {
            return "";
        }
    }

    //method daftar
    public Boolean daftar() {
        System.out.println("+=======+");
        System.out.println("| Daftar |");
        System.out.println("+=======+\n");
        System.out.print("Masukan Username : ");
        String username = getInput();
        System.out.print("Masukan Password : ");
        String password = getInput();

        if (setQuery.getUserId(username).size() <= 0) {
            if (setQuery.addUserData(username, password)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    //method tambah pesanan (untnuk menentukan jumlah menu yang dipesan)
    public Boolean tambahPesanan(String username, int idMakanan) {
        String makanan = setQuery.getMenuData("nama_makanan", idMakanan);
        int harga = Integer.parseInt(setQuery.getMenuData("harga", idMakanan));
        System.out.println("+=====================+");
        System.out.println("| Berapa pesanan anda |");
        System.out.println("+=====================+\n\n");
        System.out.println(makanan+" -----> "+harga);
        System.out.println("(Input 0 untuk kembali)");
        System.out.print("qty => ");
        Integer jumlah = Integer.parseInt(getInput());
        if (!jumlah.equals(0)) {
            if (setQuery.addDetailOrder(username, makanan, harga, jumlah, idMakanan)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    //Menu konfirmasi dan pembayaran
    public Integer konfirPemb(String username) {
        List<String> makanan = setQuery.getDetailOrder("nama_makanan", username);
        List<String > jumlah = setQuery.getDetailOrder("jumlah", username);
        List<String> subTotal = setQuery.getDetailOrder("subtotal", username);
        int total = 0, totalJumlah = 0;

        System.out.println("+=========================+");
        System.out.println("| Konfirmasi & Pembayaran |");
        System.out.println("+=========================+\n");
        for (int i = 0; i < makanan.size(); i++) {
            String  tabText = "\t";
            if (makanan.get(i).length() < 4){
                tabText = "\t\t\t";
            } if (makanan.get(i).length() < 8) {
                tabText = "\t\t";
            }
            System.out.print(makanan.get(i)+tabText+jumlah.get(i)+"\t");
            System.out.println("Rp "+String.format("%,d",Integer.parseInt(subTotal.get(i))).replace(",","."));
            total += Integer.parseInt(subTotal.get(i));
            totalJumlah += Integer.parseInt(jumlah.get(i));
        }
        System.out.println("------------------------------+");
        System.out.println("Total\t"+"\t"+totalJumlah+"\tRp "+String.format("%,d",total).replace(",",".")+"\n");
        System.out.println("1. Konfirmasi dan Bayar");
        System.out.println("2. Kembali ke menu utama");
        System.out.println("0. Keluar Aplikasi\n");
        System.out.print("=> ");
        int pilihan = Integer.parseInt(getInput());

        return pilihan;
    }

    //method untuk memasukan alamat dan konfirmasi pembayaran
    public Boolean konfirmasi(String username) {
        System.out.println("+==========+");
        System.out.println("| Checkout |");
        System.out.println("+==========+\n");
        System.out.print("Masukan alamat : ");
        String data = getInput();
        System.out.print("Konfirmasi (Y/N)");
        String confirm = getInput();

        if (confirm.equalsIgnoreCase("Y")) {
            if (setQuery.addOrder(data, username)) {
                createReceipt("D:\\StrukBelanja.txt", username);
                if (setQuery.updateDetailOrder(username)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    //method input scanner
    public String getInput() {
        String data = input.nextLine();
        return data;
    }

    //method tutup input scanner
    public void closeScanner() {
        input.close();
    }

    //method pembuatan struk belanja (file txt)
    public void createReceipt(String pathFile, String username) {
        try {
            List<String> makanan = setQuery.getDetailOrder("nama_makanan", username);
            List<String > jumlah = setQuery.getDetailOrder("jumlah", username);
            List<String> subTotal = setQuery.getDetailOrder("subtotal", username);
            int total = 0, totalJumlah = 0;

            File file = new File(pathFile);     //membuat file
            if (file.createNewFile()) {
                System.out.println("Struk belanja berhasil dicetak");
            }
            FileWriter fileWriter = new FileWriter(file);       //deklarasi FileWriter dan BufferedWriter
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("=====================================\n");
            bufferedWriter.write("BinarFud\n");
            bufferedWriter.write("=====================================\n\n");
            bufferedWriter.write("Terima kasih sudah memesan \ndi BinarFud\n\n");
            bufferedWriter.write("Dibawah ini adalah pesanan anda");
            bufferedWriter.newLine();

            for (int i = 0; i < makanan.size(); i++) {
                String  tabText = "\t";
                if (makanan.get(i).length() < 4){           //untuk merapikan list menu agar sejajar
                    tabText = "\t\t\t";
                } if (makanan.get(i).length() < 8) {
                    tabText = "\t\t";
                }
                String text = makanan.get(i)+tabText+jumlah.get(i)+"\t";        //print menu di file txt
                bufferedWriter.write(text);
                text = "Rp "+String.format("%,d",Integer.parseInt(subTotal.get(i))).replace(",",".");
                bufferedWriter.write(text);
                bufferedWriter.newLine();
                total += Integer.parseInt(subTotal.get(i));
                totalJumlah += Integer.parseInt(jumlah.get(i));
            }
            bufferedWriter.write("------------------------------------+\n");
            bufferedWriter.write("Total\t"+"\t"+totalJumlah+"\tRp "+String.format("%,d",total).replace(",",".")+"\n");
            bufferedWriter.newLine();

            bufferedWriter.write("Pembayaran : BinarCash\n");
            bufferedWriter.write("=====================================\n");
            bufferedWriter.write("Simpan struk ini sebagai \nbukti pembayaran\n");
            bufferedWriter.write("=====================================\n");
            bufferedWriter.newLine();

            bufferedWriter.flush();
            bufferedWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
