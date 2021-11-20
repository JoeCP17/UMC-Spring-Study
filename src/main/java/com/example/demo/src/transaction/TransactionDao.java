package com.example.demo.src.transaction;

import com.example.demo.src.product.model.GetProductPreviewRes;
import com.example.demo.src.product.model.PatchProductReq;
import com.example.demo.src.product.model.PostProductReq;
import com.example.demo.src.transaction.model.PatchTransactionReq;
import com.example.demo.src.transaction.model.PostTransactionReq;
import com.example.demo.src.transaction.model.Transaction;
import com.example.demo.src.user.model.PatchUserReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sound.midi.Patch;
import javax.sql.DataSource;
import java.util.List;

@Repository
public class TransactionDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    /*
    public List<Integer> getTransactionByBuyer(int userIdx){
        return this.jdbcTemplate.query(
                "select productIdx from Transaction where userIdx=?",
                (rs, rowNum) -> rs.getInt("productIdx"),userIdx);

    }
     */

    //거래 생성
    public int createTransaction(PostTransactionReq postTransactionReq) {
        String createTransactionQuery = "insert into Transaction (productIdx, status) VALUES (?,?)"; // 실행될 동적 쿼리문
        Object[] createTransactionParams = new Object[]{postTransactionReq.getProductIdx(), postTransactionReq.getStatus()}; // 동적 쿼리의 ?부분에 주입될 값
        this.jdbcTemplate.update(createTransactionQuery, createTransactionParams);
        String lastInsertIdQuery = "select last_insert_id()"; // 가장 마지막에 삽입된(생성된) id값은 가져온다.
        int transactionIdx = this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class); // 해당 쿼리문의 결과 마지막으로 삽입된 상품의 productIdx번호를 반환한다.
        return transactionIdx;
    }


    // 상품 번호를 통해 거래 조회
    public List<Transaction> getTransactionIdxByProduct(int productIdx) {
        String getTransactionIdxQuery = "select * from Transaction where productIdx = ?";
        int getTransactionIdxParams = productIdx; // 해당(확인할) 휴대폰번호 값
        return this.jdbcTemplate.query(getTransactionIdxQuery,
                (rs, rowNum) -> new Transaction(
                        rs.getInt("transactionIdx"),
                        rs.getInt("productIdx"),
                        rs.getInt("buyer"),
                        rs.getString("status"),
                        rs.getTimestamp("createAt"),
                        rs.getTimestamp("updateAt")
                ),getTransactionIdxParams);
    }

    //구매자 수정
    public int modifyBuyer(PatchTransactionReq patchTransactionReq) {
        String modifyBuyerQuery = "update Transaction set buyer=? where transactionIdx=?"; // 실행될 동적 쿼리문
        Object[] modifyBuyerParams = new Object[]{patchTransactionReq.getBuyer(), patchTransactionReq.getTransactionIdx()}; // 동적 쿼리의 ?부분에 주입될 값
        return this.jdbcTemplate.update(modifyBuyerQuery, modifyBuyerParams);
    }

    //상태 수정
    public int modifyStatus(PatchTransactionReq patchTransactionReq) {
        String modifyStatusQuery = "update Transaction set status=? where productIdx=?"; // 실행될 동적 쿼리문
        Object[] modifyStatusParams = new Object[]{patchTransactionReq.getStatus(), patchTransactionReq.getProductIdx()}; // 동적 쿼리의 ?부분에 주입될 값
        return this.jdbcTemplate.update(modifyStatusQuery, modifyStatusParams);
    }

    //거래 삭제
    public int deleteTransactionByProduct(int productIdx){
        String deleteTransactionQuery = "delete from Transaction where productIdx = ?";
        int getProductParams = productIdx;
        return this.jdbcTemplate.update(deleteTransactionQuery, getProductParams);
    }

    //상품이 거래에 등록되어 있는지 확인
    public int checkProduct(int productIdx) {
        String checkProductQuery = "select exists(select transactionIdx from Transaction where productIdx = ?)"; // User Table에 해당 phoneNum 값을 갖는 유저 정보가 존재하는가?
        int checkProductParams = productIdx; // 해당(확인할) 휴대폰번호 값
        return this.jdbcTemplate.queryForObject(checkProductQuery,
                int.class,
                checkProductParams); // checkPhoneNumQuery, checkPhoneNumParams를 통해 가져온 값(intgud)을 반환한다. -> 쿼리문의 결과(존재하지 않음(False,0),존재함(True, 1))를 int형(0,1)으로 반환됩니다.
    }

}
