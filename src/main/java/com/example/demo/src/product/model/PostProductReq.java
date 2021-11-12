package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class PostProductReq {
    private int userIdx;
    private int categoryIdx;
    private String title;
    private List<String> imgUrlList;
    private int price;
    private String content;
}
