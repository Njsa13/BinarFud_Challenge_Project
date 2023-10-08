package com.binarfud.binarfud_challenge4.controller;

import com.binarfud.binarfud_challenge4.dto.*;
import com.binarfud.binarfud_challenge4.service.AdminService;
import com.binarfud.binarfud_challenge4.service.MerchantService;
import com.binarfud.binarfud_challenge4.service.ProductService;
import com.binarfud.binarfud_challenge4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.InputMismatchException;
import java.util.Scanner;

@Component
public class AdminController {
    private Scanner scanner = new Scanner(System.in);
    AdminView adminView =new AdminView();
    Alert alert = new Alert();

    @Autowired
    private AdminService adminService;
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;

    public void adminMainMenu() {
        try {
            System.out.println("-- Klik Enter --");
            scanner.nextLine();
            adminView.adminMainMenuView();
            int option = scanner.nextInt();
            System.out.println();
            switch (option) {
                case 1:
                    this.merchantListMenu(null);
                    break;
                case 2:
                    this.productListMenu(null);
                    break;
                case 3:
                    this.userListMenu(null);
                    break;
                case 0:
                    System.exit(0);
                default:
                    alert.notAvailableOption();
                    this.adminMainMenu();
            }
        } catch (InputMismatchException e) {
            alert.wrongInputType();
        } catch (OutOfMemoryError | NullPointerException e) {
            alert.allErrorAndExceptionAlert();
        }
        this.adminMainMenu();
    }

    public void merchantListMenu(Integer page) {
        try {
            scanner.nextLine();
            MerchantPageDTO merchantPageDTO = merchantService.getAllMerchant(page);
            adminView.merchantListMenuView(merchantPageDTO);

            int option1;
            if (!merchantPageDTO.getTotalPages().equals(0)){
                option1 = scanner.nextInt();
                System.out.println();

                if (option1 != 0) {
                    if (!((option1 > 0) && (option1 <= merchantPageDTO.getTotalPages()))) {
                        alert.notAvailableOption();
                        this.merchantListMenu(null);
                    }
                    this.merchantListMenu(option1-1);
                }
            }

            adminView.menuView();
            int option2 = scanner.nextInt();
            System.out.println();
            switch (option2) {
                case 99:
                    this.insertMerchant();
                    break;
                case 999:
                    this.merchantListMenu(null);
                    break;
                case 0:
                    this.adminMainMenu();
                    break;
                default:
                    if ((option2 > 0) && (option2 <= merchantPageDTO.getMerchantDTOS().size())) {
                        MerchantDTO merchantDTO = MerchantDTO.builder()
                                .merchantName(merchantPageDTO.getMerchantDTOS().get(option2 -1).getMerchantName())
                                .merchantLocation(merchantPageDTO.getMerchantDTOS().get(option2 -1).getMerchantLocation())
                                .open(merchantPageDTO.getMerchantDTOS().get(option2 -1).getOpen())
                                .index(option2)
                                .page(page == null ? 0 : page)
                                .build();
                        this.editMerchantMenu(merchantDTO);
                    } else {
                        alert.notAvailableOption();
                    }
                    this.merchantListMenu(null);
            }
        } catch (InputMismatchException e) {
            alert.wrongInputType();
        } catch (OutOfMemoryError | NullPointerException e) {
            alert.allErrorAndExceptionAlert();
        }
        this.merchantListMenu(null);
    }

    public void editMerchantMenu(MerchantDTO merchantDTO) {
        try {
            scanner.nextLine();
            adminView.editMerchantMenuView(merchantDTO);
            int option = scanner.nextInt();
            switch (option) {
                case 1:
                    this.updateMerchant(merchantDTO);
                    break;
                case 2:
                    this.updateMerchantStatus(true, merchantDTO);
                    break;
                case 3:
                    this.updateMerchantStatus(false, merchantDTO);
                    break;
                case 4:
                    merchantService.deleteMerchant(merchantDTO);
                    this.merchantListMenu(null);
                    break;
                case 0:
                    this.merchantListMenu(null);
                    break;
                default:
                    alert.notAvailableOption();
                    this.editMerchantMenu(merchantDTO);
            }
        } catch (InputMismatchException e) {
            alert.notAvailableOption();
        } catch (OutOfMemoryError | NullPointerException | IndexOutOfBoundsException e) {
            alert.allErrorAndExceptionAlert();
        }
        this.editMerchantMenu(merchantDTO);
    }

    public void updateMerchantStatus(Boolean status, MerchantDTO merchantDTO) throws NullPointerException, IndexOutOfBoundsException {
        merchantDTO.setOpen(status);
        merchantService.updateMerchantStatus(merchantDTO);
        this.editMerchantMenu(merchantDTO);
    }

    public void updateMerchant(MerchantDTO merchantDTO) {
        try {
            scanner.nextLine();
            System.out.println("Ketik 0 untuk kembali\n");
            System.out.print("Masukan Merchant Name : ");
            String merchantName = scanner.next();
            if (merchantName.equals("0")) { this.editMerchantMenu(merchantDTO);}
            System.out.println();

            System.out.print("Masukan Merchant Location : ");
            String merchantLocation = scanner.next();
            if (merchantLocation.equals("0")) { this.editMerchantMenu(merchantDTO);}
            System.out.println();

            System.out.println("Masukan Merchant Status");
            System.out.print("Ketik 'o' untuk open dan ketik 'c' untuk close :");
            String merchantStatus = scanner.next();
            if (merchantStatus.equals("0")) { this.editMerchantMenu(merchantDTO);}
            System.out.println();

            if(!(merchantStatus.equalsIgnoreCase("o") || merchantStatus.equalsIgnoreCase("c"))) {
                alert.notAvailableOption();
                this.editMerchantMenu(merchantDTO);
            }

            boolean open = merchantStatus.equalsIgnoreCase("o");
             merchantDTO = MerchantDTO.builder()
                    .merchantName(merchantName)
                    .merchantLocation(merchantLocation)
                    .open(open)
                     .index(merchantDTO.getIndex())
                     .page(merchantDTO.getPage())
                    .build();
            if (!merchantService.checkMerchantNameAvailability(merchantDTO)) {
                merchantService.updateMerchant(merchantDTO);
                alert.updateSuccess();
            } else {
                alert.merchantAvailable();
                this.merchantListMenu(null);
            }
        } catch (OutOfMemoryError | NullPointerException | IndexOutOfBoundsException e) {
            alert.allErrorAndExceptionAlert();
        }
        this.editMerchantMenu(merchantDTO);
    }

    public void insertMerchant() {
        try {
            scanner.nextLine();
            System.out.println("Ketik 0 untuk kembali\n");
            System.out.print("Masukan Merchant Name : ");
            String merchantName = scanner.next();
            if (merchantName.equals("0")) { this.merchantListMenu(null);}
            System.out.println();
            scanner.nextLine();

            System.out.print("Masukan Merchant Location : ");
            String merchantLocation = scanner.next();
            if (merchantLocation.equals("0")) { this.merchantListMenu(null);}
            System.out.println();

            System.out.println("Masukan Merchant Status");
            System.out.print("Ketik 'o' untuk open dan ketik 'c' untuk close :");
            String merchantStatus = scanner.next();
            if (merchantStatus.equals("0")) { this.merchantListMenu(null);}
            System.out.println();

            if(!(merchantStatus.equalsIgnoreCase("o") || merchantStatus.equalsIgnoreCase("c"))) {
                alert.notAvailableOption();
                this.merchantListMenu(null);
            }

            boolean open = merchantStatus.equalsIgnoreCase("o");
            MerchantDTO merchantDTO = MerchantDTO.builder()
                    .merchantName(merchantName)
                    .merchantLocation(merchantLocation)
                    .open(open)
                    .build();
            if (!merchantService.checkMerchantNameAvailability(merchantDTO)) {
                merchantService.addMerchant(merchantDTO);
                alert.insertSuccess();
            } else {
                alert.merchantAvailable();
            }
        } catch (OutOfMemoryError | NullPointerException | IndexOutOfBoundsException e) {
            alert.allErrorAndExceptionAlert();
        }
        this.merchantListMenu(null);
    }

    public void productListMenu(Integer page) {
        try {
            scanner.nextLine();
            ProductPageDTO productPageDTO = productService.getAllProduct(page);
            adminView.productListMenuView(productPageDTO);

            int option1;
            if (!productPageDTO.getTotalPages().equals(0)){
                option1 = scanner.nextInt();
                System.out.println();

                if (option1 != 0) {
                    if (!((option1 > 0) && (option1 <= productPageDTO.getTotalPages()))) {
                        alert.notAvailableOption();
                        this.productListMenu(null);
                    }
                    this.productListMenu(option1-1);
                }
            }

            adminView.menuView();
            int option2 = scanner.nextInt();
            System.out.println();
            switch (option2) {
                case 99:
                    this.insertProduct();
                    break;
                case 999:
                    this.productListMenu(null);
                    break;
                case 0:
                    this.adminMainMenu();
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
                        this.editProductMenu(productDTO);
                    }
                    alert.notAvailableOption();
                    this.productListMenu(null);
            }
        } catch (InputMismatchException e) {
            alert.wrongInputType();
        } catch (OutOfMemoryError | NullPointerException e) {
            alert.allErrorAndExceptionAlert();
        }
        this.productListMenu(null);
    }

    public void editProductMenu(ProductDTO productDTO) {
        try {
            scanner.nextLine();
            adminView.editProductMenuView(productDTO);
            int option = scanner.nextInt();
            switch (option) {
                case 1:
                    updateProduct(productDTO);
                    break;
                case 2:
                    productService.deleteProduct(productDTO);
                    this.productListMenu(null);
                    break;
                case 0:
                    this.productListMenu(null);
                    break;
                default:
                    this.editProductMenu(productDTO);
            }
        } catch (InputMismatchException e) {
            alert.notAvailableOption();
        } catch (OutOfMemoryError | NullPointerException | IndexOutOfBoundsException e) {
            alert.allErrorAndExceptionAlert();
        }
        this.editProductMenu(productDTO);
    }

    public void updateProduct(ProductDTO productDTO) {
        try {
            scanner.nextLine();
            System.out.println("Ketik 0 untuk kembali\n");
            System.out.print("Masukan Product Name : ");
            String productName = scanner.next();
            if (productName.equals("0")) { this.editProductMenu(productDTO);}
            System.out.println();

            System.out.print("Masukan Price : ");
            Integer price = scanner.nextInt();
            if (price.equals(0)) { this.editProductMenu(productDTO);}
            System.out.println();

            productDTO = ProductDTO.builder()
                    .productName(productName)
                    .price(price)
                    .index(productDTO.getIndex())
                    .page(productDTO.getPage())
                    .build();
            if (productService.checkProductAvailability(productDTO)) {
                productService.updateProduct(productDTO);
                alert.updateSuccess();
            } else {
                alert.productAvailable();
                this.productListMenu(null);
            }
        } catch (OutOfMemoryError | NullPointerException | IndexOutOfBoundsException e) {
            alert.allErrorAndExceptionAlert();
        }
        this.editProductMenu(productDTO);
    }

    public void insertProduct() {
        try {
            scanner.nextLine();
            System.out.println("Ketik 0 untuk kembali\n");
            System.out.print("Masukan Product Name : ");
            String productName = scanner.next();
            if (productName.equals("0")) { this.productListMenu(null);}
            System.out.println();

            System.out.print("Masukan Price : ");
            Integer price = scanner.nextInt();
            if (price.equals(0)) { this.productListMenu(null);}
            System.out.println();

            System.out.print("Masukan Merchant Name : ");
            String merchantName = scanner.next();
            if (merchantName.equals("0")) { this.productListMenu(null);}
            System.out.println();

            ProductDTO productDTO = ProductDTO.builder()
                    .productName(productName)
                    .price(price)
                    .merchantName(merchantName)
                    .build();

            MerchantDTO merchantDTO = MerchantDTO.builder()
                    .merchantName(productDTO.getMerchantName())
                    .build();

            if (productService.checkProductAvailability(productDTO) && merchantService.checkMerchantNameAvailability(merchantDTO)) {
                productService.addProduct(productDTO);
                alert.insertSuccess();
            } else {
                alert.productAvailable();
            }
        } catch (InputMismatchException e) {
            alert.wrongInputType();
        } catch (OutOfMemoryError | NullPointerException | IndexOutOfBoundsException e) {
            alert.allErrorAndExceptionAlert();
        }
        this.productListMenu(null);
    }

    private void userListMenu(Integer page) {
        try {
            scanner.nextLine();
            UserPageDTO userPageDTO = userService.getAllUser(page);
            adminView.userListMenuView(userPageDTO);

            int option1;
            if (!userPageDTO.getTotalPages().equals(0)){
                option1 = scanner.nextInt();
                System.out.println();

                if (option1 != 0) {
                    if (!((option1 > 0) && (option1 <= userPageDTO.getTotalPages()))) {
                        alert.notAvailableOption();
                        this.userListMenu(null);
                    }
                    this.userListMenu(option1-1);
                }
            }

            adminView.menuView();
            int option2 = scanner.nextInt();
            System.out.println();
            switch (option2) {
                case 99:
                    this.insertUser();
                    break;
                case 999:
                    this.userListMenu(null);
                    break;
                case 0:
                    this.adminMainMenu();
                    break;
                default:
                    if ((option2 > 0) && (option2 <= userPageDTO.getUserDTOS().size())) {
                        UserDTO userDTO = UserDTO.builder()
                                .username(userPageDTO.getUserDTOS().get(option2 -1).getUsername())
                                .password(userPageDTO.getUserDTOS().get(option2 -1).getPassword())
                                .email(userPageDTO.getUserDTOS().get(option2 -1).getEmail())
                                .index(option2)
                                .page(page == null ? 0 : page)
                                .build();
                        this.editUserMenu(userDTO);
                    }
                    alert.notAvailableOption();
                    this.userListMenu(null);
            }
        } catch (InputMismatchException e) {
            alert.wrongInputType();
        } catch (OutOfMemoryError | NullPointerException e) {
            alert.allErrorAndExceptionAlert();
        }
        this.userListMenu(null);
    }

    public void editUserMenu(UserDTO userDTO) {
        try {
            scanner.nextLine();
            adminView.editUserMenuVIew(userDTO);
            int option = scanner.nextInt();
            switch (option) {
                case 1:
                    this.updateUser(userDTO);
                    break;
                case 2:
                    userService.deleteUser(userDTO);
                    this.userListMenu(null);
                    break;
                case 0:
                    this.userListMenu(null);
                    break;
                default:
                    this.editUserMenu(userDTO);
            }
        } catch (InputMismatchException e) {
            alert.notAvailableOption();
        } catch (OutOfMemoryError | NullPointerException | IndexOutOfBoundsException e) {
            alert.allErrorAndExceptionAlert();
        }
        this.editUserMenu(userDTO);
    }

    public void updateUser(UserDTO userDTO) {
        try {
            scanner.nextLine();
            System.out.println("Ketik 0 untuk kembali\n");
            System.out.print("Masukan Username : ");
            String username = scanner.next();
            if (username.equals("0")) { this.editUserMenu(userDTO);}
            System.out.println();

            System.out.print("Masukan Password : ");
            String password = scanner.next();
            if (password.equals("0")) { this.editUserMenu(userDTO);}
            System.out.println();

            System.out.print("Masukan email : ");
            String email = scanner.next();
            if (email.equals("0")) { this.editUserMenu(userDTO);}
            System.out.println();

            userDTO = UserDTO.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .index(userDTO.getIndex())
                    .page(userDTO.getPage())
                    .build();
            if (userService.validateUserWithRegex(userDTO)) {
                if (!userService.checkUsernameAvailability(userDTO) && !adminService.checkUsernameAvailability(userDTO)) {
                    userService.updateUser(userDTO);
                    alert.updateSuccess();
                } else {
                    alert.usernameAvailable();
                    this.userListMenu(null);
                }
            } else {
                alert.wrongFormat();
            }
        } catch (OutOfMemoryError | NullPointerException | IndexOutOfBoundsException e) {
            alert.allErrorAndExceptionAlert();
        }
        this.editUserMenu(userDTO);
    }

    public void insertUser() {
        try {
            scanner.nextLine();
            System.out.println("Ketik 0 untuk kembali\n");
            System.out.print("Masukan Username : ");
            String username = scanner.next();
            if (username.equals("0")) { this.userListMenu(null);}
            System.out.println();

            System.out.print("Masukan Password : ");
            String password = scanner.next();
            if (password.equals("0")) { this.userListMenu(null);}
            System.out.println();

            System.out.print("Masukan email : ");
            String email = scanner.next();
            if (email.equals("0")) { this.userListMenu(null);}
            System.out.println();

            UserDTO userDTO = UserDTO.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .build();
            if (userService.validateUserWithRegex(userDTO)) {
                if (!userService.checkUsernameAvailability(userDTO) && !adminService.checkUsernameAvailability(userDTO)) {
                    userService.addUserAccount(userDTO);
                    alert.insertSuccess();
                } else {
                    alert.usernameAvailable();
                }
            } else {
                alert.wrongFormat();
            }
        } catch (OutOfMemoryError | NullPointerException | IndexOutOfBoundsException e) {
            alert.allErrorAndExceptionAlert();
        }
        this.userListMenu(null);
    }
}
