package com.example.demo.src.product.model;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PatchProductReq {
    private int productIdx;
    private String status;
}
