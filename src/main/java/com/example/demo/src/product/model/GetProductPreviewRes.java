package com.example.demo.src.product.model;

import lombok.*;

import java.sql.Timestamp;

@Setter
@Getter
@AllArgsConstructor
public class GetProductPreviewRes {
    private int productIdx;
    private String imgUrl;
    private String title;
    private String userDong;
    private String status;
    private Timestamp updateAt;
    private int price;
    //private int likeCnt;
    //private int commentCnt;

    public GetProductPreviewRes(Product product){
        this.productIdx = product.getProductIdx();
        this.title = product.getTitle();
        this.status  = product.getStatus();
        this.updateAt = product.getUpdateAt();
        this.price = product.getPrice();
    }
}
