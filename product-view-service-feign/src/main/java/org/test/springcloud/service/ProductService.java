package org.test.springcloud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.test.springcloud.client.ProductClientFeign;
import org.test.springcloud.pojo.Product;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    ProductClientFeign productClientFeign;
    public List<Product> listProducts(){
        return productClientFeign.listProdcuts();
    }
}
