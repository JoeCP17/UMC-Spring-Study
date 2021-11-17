package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.src.product.model.GetProductPreviewRes;
import com.example.demo.src.product.model.GetProductRes;
import com.example.demo.src.product.model.Product;
import com.example.demo.src.user.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class ProductProvider {

    private final ProductDao productDao;
    private final UserDao userDao;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired //readme 참고
    public ProductProvider(ProductDao productDao, UserDao userDao) {
        this.productDao = productDao;
        this.userDao = userDao;
    }

    // 특정 상품 상세 조회
    public GetProductRes getProduct(int productIdx) throws BaseException {
        try{
            GetProductRes productRes = productDao.getProduct(productIdx);
            return productRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 전체 상품 미리보기 조회
    public List<GetProductPreviewRes> getAllProducts() throws BaseException {
        try{
            List<GetProductPreviewRes> GetProductPreviewResList = productDao.getProductPreviews();
            return GetProductPreviewResList;
        } catch (Exception exception) {
             throw new BaseException(DATABASE_ERROR);
        }
    }

    // 해당 제목을 갖는 상품 조회
    public List<GetProductPreviewRes> getProductsByTitle(String title) throws BaseException {
        try {
            List<GetProductPreviewRes> GetProductPreviewResList = productDao.getProductListByTitle(title);
            return GetProductPreviewResList;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //해당 판매자의 판매 상품 조회
    public List<GetProductPreviewRes> getProductsBySeller(int userIdx) throws BaseException {
        try {
            List<GetProductPreviewRes> getProductPreviewRes = productDao.getProductsBySeller(userIdx);
            return getProductPreviewRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //해당 구매자의 구매 상품 조회
    public List<GetProductPreviewRes> getProductsByBuyer(int userIdx) throws BaseException {
        try {
            List<GetProductPreviewRes> getProductPreviewRes = productDao.getProductsByBuyer(userIdx);
            return getProductPreviewRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 판매중인 상품만 조회
    public List<GetProductPreviewRes> getActiveProducts() throws BaseException {
        try {
            List<GetProductPreviewRes> getProductPreviewRes = productDao.getActiveProductList();
            return getProductPreviewRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
