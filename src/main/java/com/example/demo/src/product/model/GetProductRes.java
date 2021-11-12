package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
    private List<String> productImgUrlList;
    private String title;
    private Timestamp updateAt;
    private String status;
    private String content;
    private int price;
}
