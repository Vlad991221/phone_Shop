package com.example.phone_Shop.Controller;

import com.example.phone_Shop.Database.*;
import com.example.phone_Shop.Service.Order_Service;
import com.example.phone_Shop.Service.ProductService;
import com.example.phone_Shop.Service.User_Exception;
import com.example.phone_Shop.Security.User_Session;
import com.example.phone_Shop.Service.User_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Controller
public class User_Controller {
    @Autowired
    User_Service userService;

    @Autowired
    ProductService productService;

    @Autowired
    User_Session userSession;

    @Autowired
    Order_Lines_DAO orderLinesDao;

    @Autowired
    Order_Service orderService;

    int items = 0;

    @GetMapping("/register-form")
    public ModelAndView register(@RequestParam("email") String email,
                                 @RequestParam("password1") String pass1,
                                 @RequestParam("password2") String pass2) {
        ModelAndView modelAndView = new ModelAndView("register");

        try {
            userService.registerUser(email, pass1, pass2);
        } catch (User_Exception e) {
            modelAndView.addObject("message", e.getMessage());
            return modelAndView;
        }

        return new ModelAndView("redirect:index.html");
    }

    @GetMapping("/register")
    public ModelAndView register() {
        return new ModelAndView("register");
    }

    @GetMapping("/login")
    public ModelAndView login(@RequestParam("email") String email,
                              @RequestParam("password") String pass) {
        ModelAndView modelAndView = new ModelAndView("index");

        List<User> userList;

        try {
            userList = userService.loginUser(email, pass);
        } catch (User_Exception e) {
            modelAndView.addObject("message", e.getMessage());
            return modelAndView;
        }

        userSession.setId(userList.get(0).getId());
        return new ModelAndView("redirect:dashboard");
    }

    @GetMapping("dashboard")
    public ModelAndView dashboard() {
        ModelAndView modelAndView = new ModelAndView("index");

        if (userSession.getId() <= 0) {
            return modelAndView;
        }

        List<Product> productList = productService.findAllProducts();
        modelAndView = new ModelAndView("dashboard");
        modelAndView.addObject("productList", productList);
        return modelAndView;
    }

    @GetMapping("/add-to-cart")
    public ModelAndView addToCart(@RequestParam("productId") int productId) {
        ModelAndView modelAndView = new ModelAndView("dashboard");
        if (userSession.getId()<=0){
            return new ModelAndView("index");
        }
        List<Product> productList = productService.findAllProducts();
        modelAndView.addObject("productList", productList);

        userSession.addToCart(productId);
        items = userSession.getCartSize();
        modelAndView.addObject("items", items);
        return new ModelAndView("redirect:dashboard");
    }

    @GetMapping("/cart")
    public ModelAndView getCart(){
        ModelAndView modelAndView = new ModelAndView("cart");

        if (userSession.getId()<=0){
            return new ModelAndView("index");
        }

        List<Product> produseBD = productService.findAllProducts();
        List<CartProduct> produseCos = new ArrayList<>();
        double totalOrderAmount = 0;

        for(int idProdusCos : userSession.getCart().keySet()){
            for (Product product:produseBD){
                if (product.getId() == idProdusCos){
                    CartProduct cartProduct = new CartProduct();
                    cartProduct.setCantitate(userSession.getCart().get(idProdusCos));
                    cartProduct.setId(product.getId());
                    cartProduct.setBrand(product.getBrand());
                    cartProduct.setName(product.getName());
                    cartProduct.setPrice(product.getPrice());
                    cartProduct.setPretTotal(userSession.getCart().get(idProdusCos)*product.getPrice());
                    totalOrderAmount = totalOrderAmount + userSession.getCart().get(idProdusCos)*product.getPrice();
                    produseCos.add(cartProduct);
                }
            }
        }
        modelAndView.addObject("productList", produseCos);
        modelAndView.addObject("totalPretComanda", totalOrderAmount);
        return modelAndView;
    }

    @GetMapping("/logout")
    public ModelAndView logout(){
        userSession.setId(0);
        return new ModelAndView("index");
    }

    @PostMapping("/sendOrder")
    public ModelAndView sendOrder() {
        ModelAndView modelAndView = new ModelAndView("orderSuccess");

        List<Product> produseBD = productService.findAllProducts();

        Order order = new Order();

        for(int idProdusCos : userSession.getCart().keySet()){
            for (Product product:produseBD) {
                if (product.getId() == idProdusCos) {
                    Order_Lines orderLines = new Order_Lines();
                    orderLines.setProductId(idProdusCos); //id produs
                    orderLines.setQuantity(userSession.getCart().get(idProdusCos)); //cantitate din cos
                    orderLines.setTotalPrice(userSession.getCart().get(idProdusCos)*product.getPrice()); //pret total per tip produs

                    Scanner scanner = new Scanner(System.in);

                    order.setUserId(userSession.getId());
                    order.setAdresa(scanner.toString());

                    orderLines.setOrder(order);

                    orderLinesDao.save(orderLines);
                }
            }
        }
        userSession.getCart().clear();

        return new ModelAndView("orderSuccess");
    }

    @GetMapping("/detailsProduct")
    public ModelAndView getProductDetails(@RequestParam("productId") int productId) {
        ModelAndView modelAndView = new ModelAndView("productDetails");

        if (userSession.getId() <= 0) {
            return new ModelAndView("index");
        }

        Product p = productService.getProductDetailsById(productId);
        modelAndView.addObject("p", p);
        return modelAndView;
    }

    @GetMapping("/history")
    public ModelAndView showOrderHistory() {
        ModelAndView modelAndView = new ModelAndView("orderHistory");
        List<Order> orders = orderService.getOrdersByUserId(userSession.getId());
        modelAndView.addObject("orders", orders);
        return modelAndView;
    }

    @GetMapping("/detailsOrder")
    public ModelAndView showDetailsOrder(@RequestParam("orderId") int orderId) {
        ModelAndView modelAndView = new ModelAndView("orderDetails");
        Iterable<Order_Lines> orderLines = orderLinesDao.findAll();
        List<OrderDetails> orderDetailsPerUser = new ArrayList<>();

        for (Order_Lines orderLinesDetails : orderLines){
            if (orderLinesDetails.getOrder().getId() == orderId) {
                Product product = productService.getProductDetailsById(orderLinesDetails.getProductId());
                OrderDetails orderDetails = new OrderDetails();
                orderDetails.setProductName(product.getName());
                orderDetails.setCategory(product.getCategory());
                orderDetails.setPricePerUnit(product.getPrice());
                orderDetails.setQuantity(orderLinesDetails.getQuantity());
                orderDetails.setTotalPrice(orderLinesDetails.getTotalPrice());
                orderDetailsPerUser.add(orderDetails);
            }
        }
        modelAndView.addObject("orderLines", orderDetailsPerUser);
        return modelAndView;
    }
}
