package com.example.demo.src.Image.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
@AllArgsConstructor
public class PostImageReq {
    private String imgCategory;
    private String imgUrl;
    private int productIdx;
    private int postIdx;
    private int userIdx;
}
