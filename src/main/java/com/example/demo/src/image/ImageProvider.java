package com.example.demo.src.image;

import com.example.demo.config.BaseException;
import com.example.demo.src.image.model.GetImageRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class ImageProvider {
    private final ImageDao imageDAO;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ImageProvider(ImageDao imageDAO){
        this.imageDAO = imageDAO;
    }

    // 상품 이미지들 전체 조회
    public List<GetImageRes> getProductImages(int productIdx) throws BaseException {
        try {
            List<GetImageRes> getImagesRes = imageDAO.getProductImages(productIdx);
            return getImagesRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 상품 대표 이미지 url 조회
    public String getOneProductImage(int productIdx) throws BaseException {
        try {
            List<GetImageRes> getImageRes = imageDAO.getOneProductImage(productIdx);
            if (!getImageRes.isEmpty()){
                return getImageRes.get(0).getImgUrl();
            } else {
                return null;
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 유저 이미지 조회
    public GetImageRes getUserImage(int userIdx) throws BaseException {
        try {
            List<GetImageRes> getImageRes = imageDAO.getUserImage(userIdx);
            if (!getImageRes.isEmpty()){
                return getImageRes.get(0);
            } else {
                return null;
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
