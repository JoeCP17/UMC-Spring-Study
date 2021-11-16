package com.example.demo.src.Image;

import com.example.demo.config.BaseException;
import com.example.demo.src.Image.model.GetImageRes;
import com.example.demo.src.user.model.GetUserRes;
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

    // Image 조회
    public List<String> getImages(String category, int idx) throws BaseException {
        try {
            List<String> getImageRes = imageDAO.getImageList(category, idx);
            return getImageRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
