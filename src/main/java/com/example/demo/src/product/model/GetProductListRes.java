package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class GetProductListRes {
    private int productIdx;
    private String imgUrl;
    private String title;
    private String dong;
    private String status;
    private Timestamp updateAt;
    private int price;
    //private int likeCnt;
    //private int commentCnt;
}
