package com.example.demo.src.transaction;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.product.model.PatchProductReq;
import com.example.demo.src.transaction.model.PatchTransactionReq;
import com.example.demo.src.transaction.model.Transaction;
import com.example.demo.src.user.UserProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/transactions")
public class TransactionController {
    final Logger logger = LoggerFactory.getLogger(this.getClass()); // Log를 남기기: 일단은 모르고 넘어가셔도 무방합니다.

    @Autowired
    private final TransactionService transactionService;
    @Autowired
    private final UserProvider userProvider;

    public TransactionController(TransactionService transactionService, UserProvider userProvider) {
        this.transactionService = transactionService;
        this.userProvider = userProvider;
    }


    /**
     * 구매자 수정 페이지
     * [PATCH] /app/transaction/:transactionIdx
     **/
    // Body
    @ResponseBody
    @PatchMapping("/{transactionIdx}")
    public BaseResponse<String> modifyBuyer(@PathVariable("transactionIdx") int transactionIdx, @RequestBody Transaction transaction) {
        try {
            PatchTransactionReq patchTransactionReq = new PatchTransactionReq(transactionIdx, transaction.getBuyer());
            transactionService.modifyTransactionBuyer(patchTransactionReq);
            String result = "수정되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }






}
