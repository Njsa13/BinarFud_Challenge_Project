package org.example.controller;

import org.example.model.OrderDetail;
import org.example.model.Product;
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
    MenuView menuView = new MenuView();
    Alert alert = new Alert();
    private Integer productIndexSize;

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
            if (validation.loginValidation(username, password, this.userService)) {
                userService.setUserIdFromDatabase(username, password);
                return true;
            } else {
                return false;
            }
        } catch (OutOfMemoryError | NullPointerException e) {
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
            return validation.signUpValidation(username, password, this.userService);
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
            List<Product> product = this.productService.getProduct();
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
     * @param index
     * @return
     */
    public boolean addToOrderList (int index) {
        try {
            scanner.nextLine();
            productService.clearListProduct();
            productService.setProductFromDatabase();

            int productId = productService.getIdByIndex(index);
            String productName = this.productService.getProductNameById(productId);
            int price = this.productService.getPriceById(productId);

            menuView.addToOrderListView(productName, price);
            Integer quantity = scanner.nextInt();
            System.out.println();
            if (quantity.equals(0)) {
               return false;
            } else if ((quantity > 5) || (quantity < 0)) {
                alert.quantityLimit();
                return true;
            }
            int subTotalPrice = orderDetailService.calculateSubtotal(quantity, price);
            orderDetailService.setOrderDetail(productId, quantity, price, this.userService.getUserId(), subTotalPrice);

            if (orderDetailService.checkProductAvailability()) {
                orderDetailService.updateQuantity();
            } else {
                orderDetailService.insertOrderDetail();
            }
            System.out.println();
        } catch (InputMismatchException e) {
            System.out.println();
            alert.wrongInputType();
        } catch (OutOfMemoryError | NullPointerException | IndexOutOfBoundsException e) {
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

            orderDetailService.setUserId(this.userService.getUserId());
            orderDetailService.clearListOrderDetail();
            orderDetailService.setOrderDetailFromDatabase();

            List<OrderDetail> orderDetails = this.orderDetailService.getOrderDetail();
            orderService.setTotalPrice(orderService.calculateTotal(orderDetails));

            menuView.payAndConfirmView(this.productService, orderDetails, this.orderService.getTotalPrice());
            Integer option = scanner.nextInt();

            System.out.println();
            return option;
        } catch (InputMismatchException e) {
            System.out.println();
            alert.wrongInputType();
        } catch (OutOfMemoryError | IndexOutOfBoundsException | NullPointerException e) {
            System.out.println();
            alert.allErrorAndExceptionAlert();
        }
        System.out.println();
        return -1;
    }

    /**
     * Method untuk mengelola fitur check out pesanan
     * @return
     */
    public boolean checkOut() {
        try {
            productService.clearListProduct();
            productService.setProductFromDatabase();

            orderDetailService.clearListOrderDetail();
            orderDetailService.setOrderDetailFromDatabase();
            List<OrderDetail> orderDetails = this.orderDetailService.getOrderDetail();

            if (orderDetails.isEmpty()) {
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
            orderService.setTotalPrice(orderService.calculateTotal(orderDetails));
            orderService.insertOrder();
            orderService.setOrderIdFromDatabase();

            orderDetailService.setOrderId(this.orderService.getOrderId());
            orderService.printReceipt("D:\\StrukBelanja.txt", this.productService, this.orderDetailService);
            if (orderDetailService.updateOrderDetail())
                alert.orderSuccess();
        } catch (OutOfMemoryError | IndexOutOfBoundsException | NullPointerException e) {
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
