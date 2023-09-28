package controllerTest;

import org.example.controller.MenuView;
import org.example.model.OrderDetail;
import org.example.model.Product;
import org.example.service.ProductService;
import org.example.service.ProductServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class MenuViewTest {
    private MenuView menuView;

    @BeforeEach
    void init() {
        menuView = new MenuView();
    }

    @AfterEach
    void tearDown() {
        menuView = null;
    }

    @Test
    void mainMenuViewTest() {
        List<Product> listTest = Arrays.asList(
                new Product(1, "Mie Goreng", 6000, "Makanan"),
                new Product(2, "Es Teh", 3000, "Minuman")
        );

        Assertions.assertDoesNotThrow(() -> menuView.mainMenuView(listTest));
    }

    @Test
    void addToOrderListViewTest() {
        Assertions.assertDoesNotThrow(() -> menuView.addToOrderListView("Mie Goreng", 6000));
    }

    @Test
    void payAndConfirmViewTest() {
        ProductService productService = new ProductServiceImpl();
        productService.setProductFromDatabase();

        List<OrderDetail> listOrderDetailTest = Arrays.asList(
                new OrderDetail(1, 2, 24000, 1, 1)
        );

        Assertions.assertDoesNotThrow(() -> menuView.payAndConfirmView(productService, listOrderDetailTest, 12000));
    }
}
