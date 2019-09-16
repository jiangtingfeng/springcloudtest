package org.test.springcloud.client;

import org.springframework.stereotype.Component;
import org.test.springcloud.pojo.Product;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductClientFeignHystrix implements ProductClientFeign{

    public List<Product> listProdcuts(){
        List<Product> result = new ArrayList<>();
        result.add(new Product(0,"产品数据微服务不可用",0));
        return result;
    }

    public int insert() {
        return 0;
    }

}