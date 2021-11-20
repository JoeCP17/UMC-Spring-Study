package com.example.demo.src.transaction.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class Transaction {
    private int transactionIdx;
    private int productIdx;
    private int buyer;
    private String status;
    private Timestamp createAt;
    private Timestamp updateAt;
}
