package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.src.image.ImageService;
import com.example.demo.src.image.model.PostImageReq;
import com.example.demo.src.product.model.*;
import com.example.demo.src.transaction.TransactionProvider;
import com.example.demo.src.transaction.TransactionService;
import com.example.demo.src.transaction.model.PatchTransactionReq;
import com.example.demo.src.transaction.model.PostTransactionReq;
import com.example.demo.src.transaction.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.PATCH_PRODUCTS_INVALID_STATUS;

@Service
public class ProductService {

    final Logger logger = LoggerFactory.getLogger(this.getClass()); // Log 처리부분: Log를 기록하기 위해 필요한 함수입니다.

    // *********************** 동작에 있어 필요한 요소들을 불러옵니다. *************************
    private final ProductDao productDao;
    private final TransactionProvider transactionProvider;
    private final TransactionService transactionService;
    private final ImageService imageService;

    @Autowired
    public ProductService(ProductDao productDao, TransactionProvider transactionProvider, TransactionService transactionService, ImageService imageService) {
        this.productDao = productDao;
        this.transactionProvider = transactionProvider;
        this.transactionService = transactionService;
        this.imageService = imageService;
    }

    // 상품등록(POST)
    public PostProductRes createProduct(PostProductReq postProductReq) throws BaseException {
        try {
            int productIdx = productDao.createProduct(postProductReq);
            for(String imgUrl: postProductReq.getImgUrlList()){
                imageService.createProductImage(new PostImageReq(
                        imgUrl,
                        productIdx
                ));
            }
            return new PostProductRes(productIdx);
        } catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 상품수정
    public void modifyProduct(int productIdx, PostProductReq postProductReq) throws BaseException {
        try {
            productDao.modifyProduct(productIdx, postProductReq); // 상품 정보 수정
            imageService.modifyProductImage(productIdx, postProductReq.getImgUrlList()); //이미지 수정
        } catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 상품 상태 수정
    public void modifyProductStatus(PatchProductReq patchProductReq) throws BaseException {
        try {
            productDao.modifyProductStatus(patchProductReq);
            int productIdx = patchProductReq.getProductIdx();
            //판매중으로 변경하려할 경우
            if(patchProductReq.getStatus().equals(ProductStatus.active.toString())){
                // 거래 테이블에 있으면 삭제
                if(transactionProvider.checkProduct(productIdx) != 0){
                    transactionService.deleteTransactionByProduct(patchProductReq.getProductIdx());
                }
            } else { // 예약중이나 거래 완료로 바꾼다면 거래테이블 생성
                //거래 테이블에 없으면 생성
                if(transactionProvider.checkProduct(productIdx) == 0){
                    PostTransactionReq postTransactionReq = new PostTransactionReq(patchProductReq.getProductIdx(), patchProductReq.getStatus());
                    transactionService.createTransaction(postTransactionReq);
                } else{ // 있으면 수정
                    PatchTransactionReq patchTransactionReq = new PatchTransactionReq(
                            patchProductReq.getProductIdx(),
                            patchProductReq.getStatus()
                    );
                    transactionService.modifyTransactionStatus(patchTransactionReq);
                }

            }
        } catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
            throw new BaseException(DATABASE_ERROR);
        }
    }



    // 해당 productIdx를 갖는 Product 삭제
    public int deleteProduct(int productIdx) throws BaseException{
        try {
            int deleteProductCnt = productDao.deleteProduct(productIdx);
            imageService.deleteProductImage(productIdx); // 해당 상품 이미지 전체 삭제
            return deleteProductCnt;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
