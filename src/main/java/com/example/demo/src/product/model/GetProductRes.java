package com.example.demo.src.product.model;

import com.example.demo.src.image.model.GetImageRes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetProductRes {
    //user
    private int userIdx;
    private String userImgUrl;
    private String nickName;
    private String UserDong;
    private BigDecimal mannerTemp;

    //product
    private int productIdx;
    private String categoryName;
    private List<GetImageRes> productImgList;
    private String title;
    private Timestamp pullUpAt;
    private int pullUpCnt;
    private String status;
    private String content;
    private int price;
}
