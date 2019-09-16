package org.test.springcloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.test.springcloud.pojo.Product;
import org.test.springcloud.sercive.ProductService;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    @RequestMapping("/products")
    public Object products() {
        List<Product> ps = productService.listProducts();
        return ps;
    }

    @ResponseBody
    @RequestMapping("/addProduct")
    public int insert() {
        int rows = productService.insert();
        return rows;
    }

}
