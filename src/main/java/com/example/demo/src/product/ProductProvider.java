package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.src.image.ImageProvider;
import com.example.demo.src.product.model.GetProductPreviewRes;
import com.example.demo.src.product.model.GetProductRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class ProductProvider {

    private final ProductDao productDao;
    private final ImageProvider imageProvider;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired //readme 참고
    public ProductProvider(ProductDao productDao, ImageProvider imageProvider) {
        this.productDao = productDao;
        this.imageProvider = imageProvider;
    }

    // 특정 상품 상세 조회
    public GetProductRes getProduct(int productIdx) throws BaseException {
        try{
            GetProductRes productRes = productDao.getProduct(productIdx);
            productRes.setProductImgList(imageProvider.getProductImages(productIdx));
            return productRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 전체 상품 미리보기 조회
    public List<GetProductPreviewRes> getAllProducts() throws BaseException {
        try{
            List<GetProductPreviewRes> getProductPreviewResList = productDao.getProductPreviews();
            for (GetProductPreviewRes getProductPreviewRes : getProductPreviewResList) {
                getProductPreviewRes.setProductImgUrl(imageProvider.getOneProductImage(getProductPreviewRes.getProductIdx()));
            }
            return getProductPreviewResList;
        } catch (Exception exception) {
             throw new BaseException(DATABASE_ERROR);
        }
    }

    // 해당 제목을 갖는 상품 조회
    public List<GetProductPreviewRes> getProductsByTitle(String title) throws BaseException {
        try {
            List<GetProductPreviewRes> getProductPreviewResList = productDao.getProductListByTitle(title);
            for (GetProductPreviewRes getProductPreviewRes : getProductPreviewResList) {
                getProductPreviewRes.setProductImgUrl(imageProvider.getOneProductImage(getProductPreviewRes.getProductIdx()));
            }
            return getProductPreviewResList;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //해당 판매자의 판매 상품 조회
    public List<GetProductPreviewRes> getProductsBySeller(int userIdx) throws BaseException {
        try {
            List<GetProductPreviewRes> getProductPreviewResList = productDao.getProductsBySeller(userIdx);
            for (GetProductPreviewRes getProductPreviewRes : getProductPreviewResList) {
                getProductPreviewRes.setProductImgUrl(imageProvider.getOneProductImage(getProductPreviewRes.getProductIdx()));
            }
            return getProductPreviewResList;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //해당 구매자의 구매 상품 조회
    public List<GetProductPreviewRes> getProductsByBuyer(int userIdx) throws BaseException {
        try {
            List<GetProductPreviewRes> getProductPreviewResList = productDao.getProductsByBuyer(userIdx);
            for (GetProductPreviewRes getProductPreviewRes : getProductPreviewResList) {
                getProductPreviewRes.setProductImgUrl(imageProvider.getOneProductImage(getProductPreviewRes.getProductIdx()));
            }
            return getProductPreviewResList;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 판매중인 상품만 조회
    public List<GetProductPreviewRes> getActiveProducts() throws BaseException {
        try {
            List<GetProductPreviewRes> getProductPreviewResList = productDao.getActiveProductList();
            for (GetProductPreviewRes getProductPreviewRes : getProductPreviewResList) {
                getProductPreviewRes.setProductImgUrl(imageProvider.getOneProductImage(getProductPreviewRes.getProductIdx()));
            }
            return getProductPreviewResList;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


}
