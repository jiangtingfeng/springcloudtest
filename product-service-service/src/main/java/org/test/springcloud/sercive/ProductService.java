package org.test.springcloud.sercive;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.test.springcloud.dao.ProductMapper;
import org.test.springcloud.pojo.Product;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService extends ServiceImpl<ProductMapper,Product> {

    @Value("${server.port}")
    String port;

    @Autowired
    private ProductMapper productMapper;

    public List<Product> listProducts(){
        List<Product> ps =  productMapper.selectList(null);
        return ps;
    }


    public int insert () {
        Product product = new Product();
        product.setName("sa");
        product.setPrice(12);
        int rows = productMapper.insert(product);
        return rows;
    }
}
