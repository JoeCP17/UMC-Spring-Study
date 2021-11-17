package com.example.demo.src.image.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class Image {
    private int imgIdx;
    private String imgCategory;
    private String imgUrl;
    private int productIdx;
    private int postIdx;
    private int userIdx;
    private String status;
    private Timestamp createAt;
    private Timestamp updateAt;
}
