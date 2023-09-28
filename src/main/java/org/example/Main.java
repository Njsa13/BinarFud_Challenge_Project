package org.example;


import org.example.controller.Alert;
import org.example.controller.MenuController;

public class Main {
    public static void main(String[] args) {
        MenuController menuController = new MenuController();
        Alert alert = new Alert();
        boolean stat;

        System.out.println("-- Klik Enter --");

        /*
        * Perulangan dan percabangan untuk menu Login
        * */
        do {
            switch (menuController.loginMenu()) {
                case 1 :
                    stat = !menuController.login();
                    break;
                case 2 :
                    do {
                        stat = menuController.signUp();
                    } while (stat);
                    stat = true;
                    break;
                case 0 :
                    menuController.closeScanner();
                    return;
                default:
                    alert.notAvailableOption();
                    stat = true;
                    break;
            }
        } while (stat);

        /*
        * Perulangan dan percabangan untuk main menu
        * */
        do {
            Integer option = menuController.mainMenu();
            if (option.equals(99)){
                /*
                * Perulangan dan percabangan untuk menu konfirmasi dan bayar
                * */
                do {
                    switch (menuController.payAndConfirm()) {
                        case 1 :
                            stat = menuController.checkOut();
                            break;
                        case 99 :
                            stat = false;
                            break;
                        case 0 :
                            menuController.closeScanner();
                            return;
                        default:
                            alert.notAvailableOption();
                            stat = true;
                            break;
                    }
                } while (stat);
                stat = true;
            } else if (option.equals(0)) {
                menuController.closeScanner();
                return;
            } else if ((option > 0) && (option <= menuController.getProductIndexSize())) {
                /*
                * Perulangan dan percabangan fitur tambah pesanan
                * */
                do {
                    stat = menuController.addToOrderList(option);
                } while (stat);
                stat = true;
            } else {
                alert.notAvailableOption();
                stat = true;
            }
        } while (stat);
    }
}