package com.example.demo.src.product.model;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostProductReq {
    private int userIdx;
    private int categoryIdx;
    private String title;
    private List<String> imgUrlList;
    private int price;
    private String content;
}
