package com.example.demo.src.product.model;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PutProductReq {
    private int productIdx;
    private int categoryIdx;
    private String title;
    private List<String> imgUrlList;
    private int price;
    private String content;
}
