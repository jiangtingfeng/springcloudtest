package org.test.springcloud.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.test.springcloud.pojo.Product;

import java.util.List;

@FeignClient(value = "PRODUCT-DATA-SERVICE",fallback = ProductClientFeignHystrix.class)
public interface ProductClientFeign {
    @GetMapping("/products")
    List<Product> listProdcuts();

    @GetMapping("/addProduct")
    int insert();
}