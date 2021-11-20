package com.example.demo.src.transaction.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PatchTransactionReq {
    private int transactionIdx;
    private int productIdx;
    private int buyer;
    private String status;

    // 트랜잭션 인덱스를 이용하여 구매자 변경
    public PatchTransactionReq(int transactionIdx, int buyer){
        this.transactionIdx = transactionIdx;
        this.buyer = buyer;
    }

    // 상품 인덱스를 이용하여 상태 변경
    public PatchTransactionReq(int productIdx, String status){
        this.productIdx = productIdx;
        this.status = status;
    }
}
