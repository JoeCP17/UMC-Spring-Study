package com.example.demo.src.transaction;

import com.example.demo.config.BaseException;
import com.example.demo.src.image.model.GetImageRes;
import com.example.demo.src.product.ProductDao;
import com.example.demo.src.transaction.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class TransactionProvider {

    private final TransactionDao transactionDao;
    private final ProductDao productDao;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public TransactionProvider(TransactionDao transactionDao,ProductDao productDao){
        this.transactionDao = transactionDao;
        this.productDao = productDao;
    }

    // 상품 번호로 거래 가져오기
    public Transaction getTransactionIdxByProduct(int productIdx) throws BaseException {
        try {
            List<Transaction> transaction = transactionDao.getTransactionIdxByProduct(productIdx);
            if (!transaction.isEmpty()){
                return transaction.get(0);
            } else {
                return null;
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 해당 상품이 이미 Transaction Table에 존재하는지 확인
    public int checkProduct(int productIdx) throws BaseException {
        try {
            return transactionDao.checkProduct(productIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


}
