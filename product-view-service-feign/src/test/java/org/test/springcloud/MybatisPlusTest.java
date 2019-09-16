package org.test.springcloud;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.test.springcloud.pojo.Product;
import org.test.springcloud.service.ProductService;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MybatisPlusTest {

    @Autowired
    ProductService productService;

    @Test
    public void select() {
        List<Product> productList = productService.listProducts();
        Assert.assertEquals(13,productList.size());
        productList.forEach(System.out::println);
    }
}
