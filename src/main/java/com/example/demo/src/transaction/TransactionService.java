package com.example.demo.src.transaction;

import com.example.demo.config.BaseException;
import com.example.demo.src.image.model.PostImageReq;
import com.example.demo.src.product.ProductDao;
import com.example.demo.src.product.model.PatchProductReq;
import com.example.demo.src.product.model.PostProductReq;
import com.example.demo.src.product.model.PostProductRes;
import com.example.demo.src.transaction.model.PatchTransactionReq;
import com.example.demo.src.transaction.model.PostTransactionReq;
import com.example.demo.src.transaction.model.Transaction;
import com.example.demo.src.user.UserProvider;
import com.example.demo.src.user.model.GetUserRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class TransactionService {

    final Logger logger = LoggerFactory.getLogger(this.getClass()); // Log 처리부분: Log를 기록하기 위해 필요한 함수입니다.

    // *********************** 동작에 있어 필요한 요소들을 불러옵니다. *************************
    private final TransactionDao transactionDao;
    private final TransactionProvider transactionProvider;
    private final UserProvider userProvider;

    @Autowired
    public TransactionService(TransactionDao transactionDao, TransactionProvider transactionProvider, UserProvider userProvider){
        this.transactionDao = transactionDao;
        this.transactionProvider = transactionProvider;
        this.userProvider = userProvider;
    }

    // 거래 생성
    public int createTransaction(PostTransactionReq postTransactionReq) throws BaseException {
        try {
            int transactionIdx = transactionDao.createTransaction(postTransactionReq);
            return transactionIdx;
        } catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 거래 상태 수정
    public int modifyTransactionStatus(PatchTransactionReq patchTransactionReq) throws BaseException {
        try {
            int result =  transactionDao.modifyStatus(patchTransactionReq);// 해당 과정이 무사히 수행되면 True(1), 그렇지 않으면 False(0)입니다.
            if (result == 0) { // result값이 0이면 과정이 실패한 것이므로 에러 메서지를 보냅니다.
                throw new BaseException(MODIFY_FAIL_TRANSACTION_STATUS);
            }
            return result;

        } catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
            throw new BaseException(DATABASE_ERROR);
        }
    }


    // 구매자 수정
    public int modifyTransactionBuyer(PatchTransactionReq patchTransactionReq) throws BaseException {
        try {
            if (userProvider.existUser(patchTransactionReq.getBuyer())==0){
                throw new BaseException(PATCH_TRANSACTIONS_INVALID_BUYER); // 유저 인덱스가 존재하지 않음
            }
            int result =  transactionDao.modifyBuyer(patchTransactionReq);// 해당 과정이 무사히 수행되면 True(1), 그렇지 않으면 False(0)입니다.
            if (result == 0) { // result값이 0이면 과정이 실패한 것이므로 에러 메서지를 보냅니다.
                throw new BaseException(MODIFY_FAIL_TRANSACTION_STATUS);
            }
            return result;

        } catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 해당 productIdx 를 갖는 거래 삭제
    public int deleteTransactionByProduct(int productIdx) throws BaseException{
        try {
            return transactionDao.deleteTransactionByProduct(productIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }



}
