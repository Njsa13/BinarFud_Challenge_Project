package com.binarfud.binarfud_challenge4.controller;

import com.binarfud.binarfud_challenge4.dto.*;
import com.binarfud.binarfud_challenge4.entity.Admin;
import com.binarfud.binarfud_challenge4.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.InputMismatchException;
import java.util.Scanner;

@Component
public class UserController {
    private Scanner scanner = new Scanner(System.in);
    UserView userView = new UserView();
    Alert alert = new Alert();

    @Autowired
    private AdminController adminController;
    @Autowired
    private AdminService adminService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private OrderService orderService;

    @PostConstruct
    public void init() {
        System.out.println("\n-- Klik Enter --");
        this.loginMenu();
    }

    public void loginMenu() {
        try {
            scanner.nextLine();
            userView.loginMenuView();
            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    this.login();
                    break;
                case 2:
                    this.signUp();
                    break;
                case 0:
                    System.exit(0);
                    break;
                default:
                    alert.notAvailableOption();
                    this.loginMenu();
            }
        } catch (InputMismatchException e) {
            alert.wrongInputType();
        } catch (OutOfMemoryError | NullPointerException e) {
            alert.allErrorAndExceptionAlert();
        }
        this.loginMenu();
    }

    public void login() {
        try {
            scanner.nextLine();
            userView.loginView();
            System.out.print("Masukan Username : ");
            String username = scanner.next();
            if (username.equals("0")) { this.loginMenu(); }

            System.out.print("Masukan Password : ");
            String password = scanner.next();
            if (password.equals("0")) { this.loginMenu(); }

            UserDTO userDTO = UserDTO.builder().username(username).password(password).build();
            Admin admin = Admin.builder().username(username).password(password).build();

            System.out.println();
            if (Boolean.TRUE.equals(adminService.checkAdminLogin(admin))) {
                alert.loginSuccess();
                adminController.adminMainMenu();
            } else if (Boolean.TRUE.equals(userService.checkUserLogin(userDTO))) {
                alert.loginSuccess();
                this.mainMenu(null, username);
            } else {
                alert.loginFail();
            }
        } catch (OutOfMemoryError | NullPointerException e) {
            alert.allErrorAndExceptionAlert();
        }
        this.loginMenu();
    }

    public void signUp() {
        try {
            scanner.nextLine();
            userView.signUpView();
            System.out.print("Masukan Username : ");
            String username = scanner.next();
            if (username.equals("0")) { this.loginMenu(); }

            System.out.print("Masukan Password : ");
            String password = scanner.next();
            if (password.equals("0")) { this.loginMenu(); }

            System.out.print("Masukan Email : ");
            String email = scanner.next();
            if (password.equals("0")) { this.loginMenu(); }

            UserDTO userDTO = UserDTO.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .build();

            System.out.println();
            if (userService.validateUserWithRegex(userDTO)) {
                if (!userService.checkUsernameAvailability(userDTO) && !adminService.checkUsernameAvailability(userDTO)) {
                    if (userService.addUserAccount(userDTO)) {
                        alert.signUpSuccess();
                    } else {
                        alert.signUpFail();
                    }
                } else {
                    alert.usernameAvailable();
                }
            } else {
                alert.wrongFormat();
            }
        } catch (OutOfMemoryError | NullPointerException e) {
            alert.allErrorAndExceptionAlert();
        }
        this.loginMenu();
    }

    public void mainMenu(Integer page, String username) {
        try {
            scanner.nextLine();
            ProductPageDTO productPageDTO = productService.getAllProduct(page);
            userView.userMainMenuView1(productPageDTO);

            int option1;
            if (!productPageDTO.getTotalPages().equals(0)){
                option1 = scanner.nextInt();
                System.out.println();

                if (option1 != 0) {
                    if (!((option1 > 0) && (option1 <= productPageDTO.getTotalPages()))) {
                        alert.notAvailableOption();
                        this.mainMenu(null, username);
                    }
                    this.mainMenu(option1-1, username);
                }
            }

            userView.userMainMenuView2();
            int option2 = scanner.nextInt();
            System.out.println();
            switch (option2) {
                case 99:
                    this.payAndConfirm(username, null);
                    break;
                case 999:
                    this.mainMenu(null, username);
                    break;
                case 0:
                    this.loginMenu();
                    break;
                default:
                    if ((option2 > 0) && (option2 <= productPageDTO.getProductDTOS().size())) {
                        ProductDTO productDTO = ProductDTO.builder()
                                .productName(productPageDTO.getProductDTOS().get(option2 -1).getProductName())
                                .price(productPageDTO.getProductDTOS().get(option2 -1).getPrice())
                                .merchantName(productPageDTO.getProductDTOS().get(option2 -1).getMerchantName())
                                .index(option2)
                                .page(page == null ? 0 : page)
                                .build();
                        this.addToOrderList(productDTO, username);
                    }
                    alert.notAvailableOption();
                    this.mainMenu(null, username);
            }
        } catch (InputMismatchException e) {
            alert.wrongInputType();
        } catch (OutOfMemoryError | NullPointerException e) {
            alert.allErrorAndExceptionAlert();
        }
        this.mainMenu(null, username);
    }

    public void addToOrderList(ProductDTO productDTO, String username) {
        try {
            scanner.nextLine();
            userView.addToOrderListView(productDTO);
            int quantity = scanner.nextInt();
            if (quantity == (0)) { this.mainMenu(null, username);}
            System.out.println();

            if ((quantity < 0) || (quantity >5)) {
                alert.quantityLimit();
                this.mainMenu(null, username);
            }
            OrderDetailDTO orderDetailDTO = OrderDetailDTO.builder()
                    .username(username)
                    .productName(productDTO.getProductName())
                    .merchantName(productDTO.getMerchantName())
                    .price(productDTO.getPrice())
                    .quantity(quantity)
                    .build();
            if (orderDetailService.checkOrderDetailAvailability(orderDetailDTO)) {
                orderDetailService.updateOrderDetail(orderDetailDTO);
            } else {
                orderDetailService.addOrderDetail(orderDetailDTO);
            }
            alert.orderSuccess();
        } catch (InputMismatchException e) {
            alert.wrongInputType();
        } catch (OutOfMemoryError | NullPointerException e) {
            alert.allErrorAndExceptionAlert();
        }
        this.mainMenu(null, username);
    }

    public void payAndConfirm(String username, Integer page) {
        try {
            scanner.nextLine();
            OrderDetailPageDTO orderDetailPageDTO = orderDetailService.getAllOrderDetail(username, page);
            userView.payAndConfirmView1(orderDetailPageDTO);

            int option1;
            if (!orderDetailPageDTO.getTotalPages().equals(0)){
                option1 = scanner.nextInt();
                System.out.println();

                if (option1 != 0) {
                    if (!((option1 > 0) && (option1 <= orderDetailPageDTO.getTotalPages()))) {
                        alert.notAvailableOption();
                        this.payAndConfirm(username ,null);
                    }
                    this.payAndConfirm(username ,option1-1);
                }
            }

            userView.payAndConfirmView2();
            int option2 = scanner.nextInt();
            System.out.println();
            switch (option2) {
                case 99:
                    if (!orderDetailPageDTO.getTotalPages().equals(0)) {
                        this.checkout(username);
                    }
                    alert.notOrder();
                    break;
                case 999:
                    this.payAndConfirm(username ,null);
                    break;
                case 0:
                    this.mainMenu(null, username);
                    break;
                default:
                    alert.notAvailableOption();
                    this.payAndConfirm(username ,null);
            }
        } catch (InputMismatchException e) {
            alert.wrongInputType();
        } catch (OutOfMemoryError | NullPointerException e) {
            alert.allErrorAndExceptionAlert();
        }
        this.mainMenu(null, username);
    }

    public void checkout(String username) {
        try {
            scanner.nextLine();
            userView.checkOutView();
            System.out.print("Masukan Alamat :");
            String address = scanner.nextLine();
            if (address.equals("0")) { this.payAndConfirm(username, null);}

            System.out.println("Check Out Sekarang ? (Y/N)");
            String confirm = scanner.next();
            if (confirm.equalsIgnoreCase("N")) {
                this.payAndConfirm(username, null);
            } else if (!confirm.equalsIgnoreCase("Y")) {
                alert.notAvailableOption();
                this.payAndConfirm(username, null);
            }
            orderService.printReceipt(username, address, "D:\\StrukBelanja.txt");
            orderService.updateOrder(username, address);
        } catch (InputMismatchException e) {
            alert.wrongInputType();
        } catch (OutOfMemoryError | NullPointerException e) {
            alert.allErrorAndExceptionAlert();
        }
        this.mainMenu(null, username);
    }
}
