package com.example.phone_Shop.Service;

import com.example.phone_Shop.Database.Product;
import com.example.phone_Shop.Database.Product_Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    Product_Dao productDao;

    public List<Product> findAllProducts() {
        return (List<Product>) productDao.findAll();
    }

    public String addProduct(String name, String brand, Double price, Integer quantity, String imgSrc) {
        Product product = new Product();
        product.setName(name);
        product.setBrand(brand);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setImgSrc(imgSrc);
        productDao.save(product);
        return "Produsul " + name + "a fost adaugat in baza de date!";
    }

    public String deleteProduct(int id) {
        productDao.deleteById(id);
        return "Produsul cu id-ul " + id + "a fost sters din baza de date!";
    }

    public Product getProductDetailsById(int id){
        return productDao.findById(id).get();
    }
}
