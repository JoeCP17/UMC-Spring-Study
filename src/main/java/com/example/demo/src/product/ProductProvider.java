package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.src.Image.ImageDao;
import com.example.demo.src.Image.ImageProvider;
import com.example.demo.src.product.model.GetProductListRes;
import com.example.demo.src.product.model.GetProductRes;
import com.example.demo.src.user.UserDao;
import com.example.demo.src.user.model.GetUserRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class ProductProvider {

    private final ProductDao productDao;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired //readme 참고
    public ProductProvider(ProductDao productDao) {
        this.productDao = productDao;
    }

    // 특정 사움 상세 조회
    public GetProductRes getProduct(int productIdx){
        GetProductRes productRes = productDao.getProduct(productIdx);
        return productRes;
    }

    // 전체 상품 조회
    public List<GetProductListRes> getProductList(){
        List<GetProductListRes> productListRes = productDao.getProductListRes();
        return productListRes;
    }

    // 해당 제목을 갖는 상품 조회
    public List<GetProductListRes> getProductsByTitle(String title) throws BaseException {
        try {
            List<GetProductListRes> getProductListRes = productDao.getProductListByTitle(title);
            return getProductListRes ;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
