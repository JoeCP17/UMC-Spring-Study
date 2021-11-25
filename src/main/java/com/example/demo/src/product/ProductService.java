package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.src.image.ImageService;
import com.example.demo.src.image.model.PostImageReq;
import com.example.demo.src.product.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class ProductService {

    final Logger logger = LoggerFactory.getLogger(this.getClass()); // Log 처리부분: Log를 기록하기 위해 필요한 함수입니다.

    // *********************** 동작에 있어 필요한 요소들을 불러옵니다. *************************
    private final ProductDao productDao;
    private final ImageService imageService;

    @Autowired
    public ProductService(ProductDao productDao, ImageService imageService) {
        this.productDao = productDao;
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
            imageService.modifyProductImage(productIdx, postProductReq.getImgUrlList()); // 이미지 수정
        } catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 상품 상태 수정
    public void modifyProductStatus(PatchProductReq patchProductReq) throws BaseException {
        String status = patchProductReq.getStatus();
        // 상태는 acitve, reserved, completed 만 가능
        if(status.equals(ProductStatus.active.toString()) || status.equals(ProductStatus.reserved.toString()) || status.equals(ProductStatus.completed.toString())){
            int result = productDao.modifyProductStatus(patchProductReq); // 해당 과정이 무사히 수행되면 True(1), 그렇지 않으면 False(0)입니다.
            if (result == 0) { // result값이 0이면 과정이 실패한 것이므로 에러 메서지를 보냅니다.
                throw new BaseException(MODIFY_FAIL_PRODUCT_STATUS);
            }
            // buyer 에 0 삽입
            try{
                productDao.modifyProductBuyer(patchProductReq);
            } catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
                throw new BaseException(DATABASE_ERROR);
            }
        } else { //유효하지 않은 상태값
            throw new BaseException(PATCH_PRODUCTS_INVALID_STATUS);
        }
    }


    // 상품 구매자 수정
    public void modifyProductBuyer(PatchProductReq patchProductReq) throws BaseException {
        String status = "";
        try{
            status = productDao.getProduct(patchProductReq.getProductIdx()).getStatus();
        } catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
            throw new BaseException(DATABASE_ERROR);
        }
        if (status.equals(ProductStatus.active.toString())){
            throw new BaseException(PATCH_PRODUCTS_ACTIVE_STATUS); //구매자 변경 불가 상태 (active)
        }
        try {
            int result = productDao.modifyProductBuyer(patchProductReq); // 해당 과정이 무사히 수행되면 True(1), 그렇지 않으면 False(0)입니다.
            if (result == 0) { // result값이 0이면 과정이 실패한 것이므로 에러 메서지를 보냅니다.
                throw new BaseException(MODIFY_FAIL_BUYER); //변경 실패
            }
        }
         catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
                throw new BaseException(DATABASE_ERROR);
            }
    }

    public void pullUpProduct(int productIdx) throws BaseException {
        try{
            int result = productDao.pullUpProduct(productIdx);
            if (result == 0) { // result값이 0이면 과정이 실패한 것이므로 에러 메서지를 보냅니다.
                throw new BaseException(PULL_UP_FAIL); //변경 실패
            }
        } catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
            throw new BaseException(DATABASE_ERROR);
        }
    }


    // 해당 productIdx를 갖는 Product 삭제
    public void deleteProduct(int productIdx) throws BaseException{
        try {
            productDao.deleteProduct(productIdx);
            imageService.deleteProductImage(productIdx); // 해당 상품 이미지 전체 삭제
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
