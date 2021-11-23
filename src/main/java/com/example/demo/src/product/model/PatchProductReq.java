package com.example.demo.src.product.model;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PatchProductReq {
    private int productIdx;
    private String status;
    private int buyer;

    //상품 상태 변경
    public PatchProductReq(int productIdx, String status){
        this.productIdx = productIdx;
        this.status = status;
    }

    //상품 구매자 변경
    public PatchProductReq(int productIdx, int buyer){
        this.productIdx = productIdx;
        this.buyer = buyer;
    }
}
