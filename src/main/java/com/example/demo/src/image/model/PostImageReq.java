package com.example.demo.src.image.model;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
public class PostImageReq {
    private String imgUrl;
    private int idx;
}
