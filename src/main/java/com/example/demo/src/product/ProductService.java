package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.src.product.model.PostProductReq;
import com.example.demo.src.product.model.PostProductRes;
import com.example.demo.src.user.model.PostUserRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class ProductService {

    final Logger logger = LoggerFactory.getLogger(this.getClass()); // Log 처리부분: Log를 기록하기 위해 필요한 함수입니다.

    // *********************** 동작에 있어 필요한 요소들을 불러옵니다. *************************
    private final ProductDao productDao;
    private final ProductProvider productProvider;

    @Autowired
    public ProductService(ProductDao productDao, ProductProvider productProvider){
        this.productDao = productDao;
        this.productProvider = productProvider;
    }

    // 상품등록(POST)
    public PostProductRes createProduct(PostProductReq postProductReq) throws BaseException {
        try {
            int productIdx = productDao.createProduct(postProductReq);
            return new PostProductRes(productIdx);
        } catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
            throw new BaseException(DATABASE_ERROR);
        }
    }


    // 해당 productIdx를 갖는 Product 삭제
    public int deleteProduct(int productIdx) throws BaseException{
        try {
            int deleteProductCnt = productDao.deleteProduct(productIdx);
            return deleteProductCnt;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
