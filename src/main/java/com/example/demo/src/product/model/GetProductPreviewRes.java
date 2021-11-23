package com.example.demo.src.product.model;

import com.example.demo.src.image.model.GetImageRes;
import lombok.*;

import java.sql.Timestamp;

@Setter
@Getter
@AllArgsConstructor
public class GetProductPreviewRes {
    private int productIdx;
    private String productImgUrl;
    private String title;
    private String userDong;
    private String status;
    private Timestamp pullUpAt;
    private int pullUpCnt;
    private int price;
    //private int likeCnt;
    //private int commentCnt;
}
