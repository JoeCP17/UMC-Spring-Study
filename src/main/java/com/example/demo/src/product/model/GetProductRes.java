package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetProductRes {
    private int productIdx;
    private int userIdx;
    private int categoryIdx;
    private String title;
    private int price;
    private String content;
    private String status;
    private List<String> imgUrlList;
    private Timestamp createAt;
    private Timestamp updateAt;
}
