package com.example.demo.src.Image;

import com.example.demo.config.BaseException;
import com.example.demo.src.Image.model.GetImageRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class ImageService {
    final Logger logger = LoggerFactory.getLogger(this.getClass()); // Log 처리부분: Log를 기록하기 위해 필요한 함수입니다.

    // *********************** 동작에 있어 필요한 요소들을 불러옵니다. *************************
    private final ImageDao imageDao;
    private final ImageProvider imageProvider;

    @Autowired
    public ImageService(ImageDao imageDao, ImageProvider imageProvider) {
        this.imageDao = imageDao;
        this.imageProvider = imageProvider;
    }

    // 해당 productIdx를 갖는 이미지 삭제
    public int deleteProductImage(int productIdx) throws BaseException {
        try {
            int deleteImageCnt = imageDao.deleteImage("product", productIdx);
            return deleteImageCnt;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
