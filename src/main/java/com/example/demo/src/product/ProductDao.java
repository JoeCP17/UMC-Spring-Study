package com.example.demo.src.product;

import com.example.demo.src.product.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class ProductDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // 상품 등록
    public int createProduct(PostProductReq postProductReq) {
        String createProductQuery = "insert into Product (userIdx, categoryIdx, title, price, content) VALUES (?,?,?,?,?)"; // 실행될 동적 쿼리문
        Object[] createProductParams = new Object[]{postProductReq.getUserIdx(),postProductReq.getCategoryIdx(),postProductReq.getTitle(), postProductReq.getPrice(), postProductReq.getContent()}; // 동적 쿼리의 ?부분에 주입될 값
        this.jdbcTemplate.update(createProductQuery, createProductParams);
        String lastInsertIdQuery = "select last_insert_id()"; // 가장 마지막에 삽입된(생성된) id값은 가져온다.
        int productIdx = this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class); // 해당 쿼리문의 결과 마지막으로 삽입된 상품의 productIdx번호를 반환한다.
        return productIdx;
    }

    // 상품 수정
    public int modifyProduct(int productIdx, PostProductReq postProductReq) {
        String modifyProductQuery = "update Product set categoryIdx=?, title=?, price=?, content=? where productIdx=?"; // 실행될 동적 쿼리문
        Object[] modifyProductParams = new Object[]{postProductReq.getCategoryIdx(),postProductReq.getTitle(), postProductReq.getPrice(), postProductReq.getContent(), productIdx}; // 동적 쿼리의 ?부분에 주입될 값
        return this.jdbcTemplate.update(modifyProductQuery, modifyProductParams);
    }

    // 상품 상태 수정
    public int modifyProductStatus(PatchProductReq patchProductReq) {
        String modifyProductQuery = "update Product set status=? where productIdx=?"; // 실행될 동적 쿼리문
        Object[] modifyProductParams = new Object[]{patchProductReq.getStatus(), patchProductReq.getProductIdx()}; // 동적 쿼리의 ?부분에 주입될 값
        return this.jdbcTemplate.update(modifyProductQuery, modifyProductParams);
    }

    // 상품 구매자 수정
    public int modifyProductBuyer(PatchProductReq patchProductReq) {
        String modifyProductQuery = "update Product set buyer=? where productIdx=?"; // 실행될 동적 쿼리문
        Object[] modifyProductParams = new Object[]{patchProductReq.getBuyer(), patchProductReq.getProductIdx()}; // 동적 쿼리의 ?부분에 주입될 값
        return this.jdbcTemplate.update(modifyProductQuery, modifyProductParams);
    }

    // 상품 끌어올리기(pullUpAt 수정)
    public int pullUpProduct(int productIdx) {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        String modifyProductQuery = "update Product set pullUpAt=?, pullUpCnt=1+Product.pullUpCnt where productIdx=?"; // 실행될 동적 쿼리문
        Object[] modifyProductParams = new Object[]{currentTime, productIdx}; // 동적 쿼리의 ?부분에 주입될 값
        return this.jdbcTemplate.update(modifyProductQuery, modifyProductParams);
    }


    //전체 상품 조회
    public List<GetProductPreviewRes> getProductPreviews(){
        String getProductsQuery = "select P.productIdx, P.title, U.userDong, P.status, P.pullUpAt, P.pullUpCnt, P.price " +
                "from Product P join User U " +
                "on P.userIdx = U.userIdx " +
                "order by P.pullUpAt DESC";

        return this.jdbcTemplate.query(getProductsQuery,
                (rs, rowNum) -> new GetProductPreviewRes(
                        rs.getInt("productIdx"),
                        null,
                        rs.getString("title"),
                        rs.getString("userDong"),
                        rs.getString("status"),
                        rs.getTimestamp("pullUpAt"),
                        rs.getInt("pullUpCnt"),
                        rs.getInt("price")));
    }

    //상품 title 로 검색 (포함)
    public List<GetProductPreviewRes> getProductListByTitle(String title){
        String getProductsQuery =  "select P.productIdx, P.title, U.userDong, P.status, P.pullUpAt, P.pullUpCnt, P.price "+
                "from Product P join User U "+
                "on P.userIdx = U.userIdx "+
                "where title Like ? "+
                "ORDER BY P.pullUpAt DESC";

        String getProductTitleParams = "%" + title + "%";
        return this.jdbcTemplate.query(getProductsQuery,
                (rs, rowNum) -> new GetProductPreviewRes(
                        rs.getInt("productIdx"),
                        null,
                        rs.getString("title"),
                        rs.getString("userDong"),
                        rs.getString("status"),
                        rs.getTimestamp("pullUpAt"),
                        rs.getInt("pullUpCnt"),
                        rs.getInt("price")),getProductTitleParams);
    }

    //현재 판매중인 상품만 조회
    public List<GetProductPreviewRes> getActiveProductList(){
        String getProductsQuery = "select P.productIdx, P.title, U.userDong, P.status, P.pullUpAt, P.pullUpCnt, P.price " +
                "from Product P join User U " +
                "on P.userIdx = U.userIdx " +
                "where P.status = 'active' " +
                "order by P.pullUpAt desc";

        return this.jdbcTemplate.query(getProductsQuery,
                (rs, rowNum) -> new GetProductPreviewRes(
                        rs.getInt("productIdx"),
                        null,
                        rs.getString("title"),
                        rs.getString("userDong"),
                        rs.getString("status"),
                        rs.getTimestamp("pullUpAt"),
                        rs.getInt("pullUpCnt"),
                        rs.getInt("price")));
    }

    //상품 상세 조회
    public GetProductRes getProduct(int productIdx){
        String getProductQuery = "SELECT P.userIdx, (SELECT I.imgUrl FROM Image I WHERE U.userIdx = I.userIdx) userImgUrl, "+
                "U.nickName, U.userDong, U.mannerTemp, P.productIdx, P.title, "+
                "(SELECT C.categoryName FROM Category C WHERE C.categoryIdx = P.categoryIdx) categoryName, "+
                "P.pullUpAt, P.pullUpCnt, P.status, P.content, P.price "+
                "FROM Product P JOIN User U " +
                "ON P.userIdx = U.userIdx " +
                "WHERE P.productIdx = ?";


        int getProductParams = productIdx;

        return this.jdbcTemplate.queryForObject(getProductQuery,
                (rs, rowNum) -> new GetProductRes(
                        rs.getInt("userIdx"),
                        rs.getString("userImgUrl"),
                        rs.getString("nickName"),
                        rs.getString("userDong"),
                        rs.getBigDecimal("mannerTemp"),
                        rs.getInt("productIdx"),
                        rs.getString("categoryName"),
                        null,
                        rs.getString("title"),
                        rs.getTimestamp("pullUpAt"),
                        rs.getInt("pullUpCnt"),
                        rs.getString("status"),
                        rs.getString("content"),
                        rs.getInt("price")), // RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
                getProductParams);// 한 개의 회원정보를 얻기 위한 jdbcTemplate 함수(Query, 객체 매핑 정보, Params)의 결과
    }

    //상품 삭제
    public int deleteProduct(int productIdx){
        String deleteUserQuery = "delete from Product where productIdx = ?";
        int getProductParams = productIdx;
        return this.jdbcTemplate.update(deleteUserQuery, getProductParams);
    }

    // 특정 판매자의 판매 내역 조회
    public List<GetProductPreviewRes> getProductsBySeller(int userIdx){
        return this.jdbcTemplate.query(
                "SELECT P.productIdx, P.title, U.userDong, P.status, P.pullUpAt, P.pullUpCnt, P.price " +
                        "FROM Product P JOIN User U " +
                        "ON P.userIdx = U.userIdx " +
                        "WHERE P.userIdx = ? " +
                        "ORDER BY P.pullUpAt DESC",
                (rs, rowNum) -> new GetProductPreviewRes(
                        rs.getInt("productIdx"),
                        null,
                        rs.getString("title"),
                        rs.getString("userDong"),
                        rs.getString("status"),
                        rs.getTimestamp("pullUpAt"),
                        rs.getInt("pullUpCnt"),
                        rs.getInt("price")),userIdx);
    }

    // 특정 구매자의 구매 내역 조회
    public List<GetProductPreviewRes> getProductsByBuyer(int userIdx){
        return this.jdbcTemplate.query(
                "SELECT P.productIdx, P.title, U.userDong, P.status, P.pullUpAt, P.pullUpCnt, P.price " +
                        "FROM Product P JOIN User U " +
                        "ON P.userIdx = U.userIdx " +
                        "WHERE P.buyer = ? "+
                        "ORDER BY pullUpAt DESC",
                (rs, rowNum) -> new GetProductPreviewRes(
                        rs.getInt("productIdx"),
                        null,
                        rs.getString("title"),
                        rs.getString("userDong"),
                        rs.getString("status"),
                        rs.getTimestamp("pullUpAt"),
                        rs.getInt("pullUpCnt"),
                        rs.getInt("price")),userIdx);

    }


}
