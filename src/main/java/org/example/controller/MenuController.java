package org.example.controller;

import org.example.model.OrderDetail;
import org.example.model.Product;
import org.example.model.User;
import org.example.service.*;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class MenuController {
    private Scanner scanner = new Scanner(System.in);
    private UserService userService = new UserServiceImpl();
    private ProductService productService = new ProductServiceImpl();
    private OrderDetailService orderDetailService = new OrderDetailServiceImpl();
    private OrderService orderService = new OrderServiceImpl();
    private Validation validation = new Validation();
    private User user = new User();
    private Integer productIndexSize;
    private Integer totalPrice;
    MenuView menuView = new MenuView();
    Alert alert = new Alert();

    /**
     * Method untuk mengelola login menu
     * @return
     */
    public Integer loginMenu() {
        try {
            scanner.nextLine();
            menuView.loginMenuView();
            Integer option = scanner.nextInt();

            System.out.println();
            return option;
        } catch (InputMismatchException e) {
            System.out.println();
            alert.wrongInputType();
        } catch (OutOfMemoryError e) {
            System.out.println();
            alert.allErrorAndExceptionAlert();
        }
        return -1;
    }

    /**
     * Method untuk mengelola fitur login
     * @return
     */
    public boolean login() {
        try {
            menuView.loginView();
            scanner.nextLine();
            System.out.print("Masukan Username : ");
            String username = scanner.next();
            if (username.equals("0")) { return false; }

            System.out.print("Masukan Password : ");
            String password = scanner.next();
            if (password.equals("0")) { return false; }

            System.out.println();
            if (validation.loginValidation(username, password, userService)) {
                userService.setUserIdFromDatabase(username, password);
                this.user.setUserId(userService.getUserId());
                return true;
            } else {
                return false;
            }
        } catch (OutOfMemoryError e) {
            System.out.println();
            alert.allErrorAndExceptionAlert();
        }
        return false;
    }

    /**
     * Method untuk mengelola fitur daftar atau sign up
     * @return
     */
    public boolean signUp() {
        try {
            menuView.signUpView();
            scanner.nextLine();
            System.out.print("Masukan Username : ");
            String username = scanner.next();
            if (username.equals("0")) { return false; }

            System.out.print("Masukan Password : ");
            String password = scanner.next();
            if (password.equals("0")) { return false; }

            System.out.println();
            return validation.signUpValidation(username, password, userService);
        } catch (OutOfMemoryError e) {
            System.out.println();
            alert.allErrorAndExceptionAlert();
        }
        return false;
    }

    /**
     * Method untuk mengelola main menu
     * @return
     */
    public Integer mainMenu() {
        try {
            scanner.nextLine();
            productService.clearListProduct();
            productService.setProductFromDatabase();
            List<Product> product = productService.getProduct();
            this.productIndexSize = product.size();

            menuView.mainMenuView(product);
            Integer option = scanner.nextInt();

            System.out.println();
            return option;
        } catch (InputMismatchException e) {
            System.out.println();
            alert.wrongInputType();
        } catch (OutOfMemoryError | IndexOutOfBoundsException e) {
            System.out.println();
            alert.allErrorAndExceptionAlert();
        }
        return -1;
    }

    /**
     * Method untuk mengelola fitur tambah pesanan
     * @param productId
     * @return
     */
    public boolean addToOrderList (int productId) {
        try {
            scanner.nextLine();
            productService.clearListProduct();
            productService.setProductFromDatabase();
            String productName = productService.getProductNameById(productId);
            int price = productService.getPriceById(productId);

            menuView.addToOrderListView(productName, price);
            Integer quantity = scanner.nextInt();
            System.out.println();
            if (quantity.equals(0)) {
               return false;
            } else if ((quantity > 5) || (quantity < 0)) {
                alert.quantityLimit();
                return true;
            }
            orderDetailService.setUserId(this.user.getUserId());
            orderDetailService.clearListOrderDetail();
            orderDetailService.setOrderDetail(productId, quantity, price);
            orderDetailService.checkProductAvailability();
            System.out.println();
        } catch (InputMismatchException e) {
            System.out.println();
            alert.wrongInputType();
        } catch (OutOfMemoryError | IndexOutOfBoundsException e) {
            System.out.println();
            alert.allErrorAndExceptionAlert();
        }
        return false;
    }

    /**
     * Method untuk mengelola menu konfirmasi dan pembayaran
     * @return
     */
    public Integer payAndConfirm() {
        try {
            scanner.nextLine();
            productService.clearListProduct();
            productService.setProductFromDatabase();
            orderDetailService.setUserId(this.user.getUserId());
            orderDetailService.clearListOrderDetail();
            orderDetailService.setOrderDetailFromDatabase();
            List<Product> products = productService.getProduct();
            List<OrderDetail> orderDetails = orderDetailService.getOrderDetail();
            orderService.setTotalPrice(orderDetails);
            this.totalPrice = orderService.getTotalPrice();

            menuView.payAndConfirmView(products, orderDetails, this.totalPrice);
            Integer option = scanner.nextInt();

            System.out.println();
            return option;
        } catch (InputMismatchException e) {
            System.out.println();
            alert.wrongInputType();
        } catch (OutOfMemoryError | IndexOutOfBoundsException e) {
            System.out.println();
            alert.allErrorAndExceptionAlert();
        }
        System.out.println();
        return -1;
    }

    /*
    * Method untuk mengelola fitur checkout pesanan
    * */
    /**
     * Method untuk mengelola fitur check out pesanan
     * @return
     */
    public boolean checkOut() {
        try {
            orderDetailService.setUserId(this.user.getUserId());
            orderDetailService.clearListOrderDetail();
            orderDetailService.setOrderDetailFromDatabase();
            if (orderDetailService.getOrderDetail().isEmpty()) {
                alert.notOrder();
                return false;
            }
            scanner.nextLine();
            menuView.checkOutView();
            System.out.print("Masukan Alamat : ");
            String  address = scanner.next();
            if (address.equals("0")) { return true; }

            System.out.println("Check Out Sekarang ? (Y/N)");
            String confirm = scanner.next();
            if (confirm.equalsIgnoreCase("N")) {
                return false;
            } else if (!confirm.equalsIgnoreCase("Y")) {
                alert.notAvailableOption();
                return false;
            }

            orderService.setAddress(address);
            orderService.setTotalPrice(this.totalPrice);
            orderService.insertOrder();
            orderService.setOrderIdFromDatabase();

            int orderId = orderService.getOrderId();
            orderDetailService.setOrderId(orderId);
            orderService.printReceipt("D:\\StrukBelanja.txt", this.user.getUserId());
            orderDetailService.updateOrderDetail();
            alert.orderSuccess();
        } catch (OutOfMemoryError | IndexOutOfBoundsException e) {
            System.out.println();
            alert.allErrorAndExceptionAlert();
        }
        return true;
    }

    public Integer getProductIndexSize() {
        return this.productIndexSize;
    }

    public void closeScanner() {
        scanner.close();
    }
}
