package com.example.demo.src.product;

import com.example.demo.src.product.model.GetProductListRes;
import com.example.demo.src.product.model.GetProductRes;
import com.example.demo.src.user.UserDao;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductProvider {

    private final ProductDao productDao;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired //readme 참고
    public ProductProvider(ProductDao productDao) {
        this.productDao = productDao;
    }

    /*
    public List<GetProductRes> getProduct(){
        List<GetProductRes> productRes = productDao.productRes();
        return productRes;
    }
     */


    public List<GetProductListRes> getProductList(){
        List<GetProductListRes> productListRes = productDao.productListRes();
        return productListRes;
    }

}
