package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.src.product.model.*;
import com.example.demo.src.transaction.TransactionDao;
import com.example.demo.src.transaction.TransactionProvider;
import com.example.demo.src.transaction.model.PostTransactionReq;
import com.example.demo.src.user.model.PostUserRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.PATCH_USERS_INVALID_STATUS;

@Service
public class ProductService {

    final Logger logger = LoggerFactory.getLogger(this.getClass()); // Log 처리부분: Log를 기록하기 위해 필요한 함수입니다.

    // *********************** 동작에 있어 필요한 요소들을 불러옵니다. *************************
    private final ProductDao productDao;
    private final TransactionProvider transactionProvider;
    private final ProductProvider productProvider;

    @Autowired
    public ProductService(ProductDao productDao, TransactionProvider transactionProvider, ProductProvider productProvider){
        this.productDao = productDao;
        this.transactionProvider= transactionProvider;
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

    // 상품수정
    public void modifyProduct(PutProductReq putProductReq) throws BaseException {
        try {
            productDao.modifyProduct(putProductReq);
        } catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 상품 상태 수정
    public void modifyProductStatus(PatchProductReq patchProductReq) throws BaseException {
        try {
            productDao.modifyProductStatus(patchProductReq);
            // 거래 테이블에도 반영
            if (patchProductReq.getStatus().equals(ProductStatus.completed.toString()) | patchProductReq.getStatus().equals(ProductStatus.reserved.toString())){
                if(transactionProvider.checkProductIdx(patchProductReq.getProductIdx()) == 1){ // 이미 Transaction 에 존재하면
                    //수정
                } else {
                    //생성
                }
            } else if(patchProductReq.getStatus().equals(ProductStatus.active.toString())) {
                // 삭제
            } else {
                throw new BaseException(PATCH_USERS_INVALID_STATUS);
            }
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
