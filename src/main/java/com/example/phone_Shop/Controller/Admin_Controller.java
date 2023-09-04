package com.example.phone_Shop.Controller;

import com.example.phone_Shop.Database.Product;
import com.example.phone_Shop.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class Admin_Controller {
    @Autowired
    ProductService productService;

    @GetMapping("/admin/products")
    public ModelAndView getProducts() {
        ModelAndView modelAndView = new ModelAndView("/admin/products.html");
        List<Product> productList = productService.findAllProducts();
        modelAndView.addObject("products", productList);
        return modelAndView;
    }

    @PostMapping("/admin/products")
    @ResponseBody
    public String addProduct(@RequestParam("name") String name,
                             @RequestParam("brand") String brand,
                             @RequestParam("price") Double price,
                             @RequestParam("quantity") Integer quantity,
                             @RequestParam("imgSrc") String imgSrc) {
        return productService.addProduct(name, brand, price, quantity, imgSrc);
    }

    @DeleteMapping("/admin/products/{id}")
    @ResponseBody
    public String deleteProduct(@PathVariable("id") int id) {
        return productService.deleteProduct(id);
    }
}
