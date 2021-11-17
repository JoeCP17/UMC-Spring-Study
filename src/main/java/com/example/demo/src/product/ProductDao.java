package com.example.demo.src.product;

import com.example.demo.src.product.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
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
    public void modifyProduct(PutProductReq putProductReq) {
        String modifyProductQuery = "update Product set categoryIdx=?, title=?, price=?, content=? where productIdx=?"; // 실행될 동적 쿼리문
        Object[] modifyProductParams = new Object[]{putProductReq.getCategoryIdx(),putProductReq.getTitle(), putProductReq.getPrice(), putProductReq.getContent(),putProductReq.getProductIdx()}; // 동적 쿼리의 ?부분에 주입될 값
        this.jdbcTemplate.update(modifyProductQuery, modifyProductParams);

        // 이미지 수정
        this.jdbcTemplate.update("DELETE FROM Image where productIdx = ?",putProductReq.getProductIdx());// 기존 이미지들 삭제
        String createImgQuery = "insert into Image (imgCategory,imgUrl,productIdx) VALUES (?,?,?)"; // 실행될 동적 쿼리문
        for(String imgUrl : putProductReq.getImgUrlList()){
            Object[] createImgParams = new Object[]{"product",imgUrl,putProductReq.getProductIdx()}; // 동적 쿼리의 ?부분에 주입될 값
            this.jdbcTemplate.update(createImgQuery, createImgParams);
        }
    }

    // 상품 상태 수정
    public void modifyProductStatus(PatchProductReq patchProductReq) {
        String modifyProductQuery = "update Product set status=? where productIdx=?"; // 실행될 동적 쿼리문
        Object[] modifyProductParams = new Object[]{patchProductReq.getStatus(), patchProductReq.getProductIdx()}; // 동적 쿼리의 ?부분에 주입될 값
        this.jdbcTemplate.update(modifyProductQuery, modifyProductParams);
    }


    //상품 전체 조회
    public List<GetProductPreviewRes> getProductPreviews(){
        String getProductsQuery = "select * from Product order by updateAt DESC";
        return this.jdbcTemplate.query(getProductsQuery,
                (rs, rowNum) -> new GetProductPreviewRes(new Product(
                        rs.getInt("productIdx"),
                        rs.getInt("userIdx"),
                        rs.getInt("categoryIdx"),
                        rs.getString("title"),
                        rs.getInt("price"),
                        rs.getString("content"),
                        rs.getString("status"),
                        rs.getTimestamp("createAt"),
                        rs.getTimestamp("updateAt") )));
    }

    //상품 title 로 검색 (포함)
    public List<GetProductPreviewRes> getProductListByTitle(String title){
        String getProductsQuery = "select * from Product where title Like ? order by updateAt DESC";

        String getProductTitleParams = "%" + title + "%";
        return this.jdbcTemplate.query(getProductsQuery,
                (rs, rowNum) -> new GetProductPreviewRes(new Product(
                        rs.getInt("productIdx"),
                        rs.getInt("userIdx"),
                        rs.getInt("categoryIdx"),
                        rs.getString("title"),
                        rs.getInt("price"),
                        rs.getString("content"),
                        rs.getString("status"),
                        rs.getTimestamp("createAt"),
                        rs.getTimestamp("updateAt"))),getProductTitleParams);
    }

    //현재 판매중인 상품만 조회
    public List<GetProductPreviewRes> getActiveProductList(){
        String getProductsQuery = "SELECT P.productIdx, P.title, U.userDong, " +
                "(SELECT I.imgUrl from Image I where I.productIdx = P.productIdx limit 1) imgUrl, " +
                "P.status, P.updateAt, P.price " +
                "FROM Product P JOIN User U " +
                "ON P.userIdx = U.userIdx " +
                "WHERE P.status = 'active'" +
                "ORDER BY P.updateAt DESC";

        return this.jdbcTemplate.query(getProductsQuery,
                (rs, rowNum) -> new GetProductPreviewRes(
                        rs.getInt("productIdx"),
                        rs.getString("imgUrl"),
                        rs.getString("title"),
                        rs.getString("userDong"),
                        rs.getString("status"),
                        rs.getTimestamp("updateAt"),
                        rs.getInt("price")));
    }

    //상품 상세 조회
    public GetProductRes getProduct(int productIdx){
        String getProductQuery = "SELECT P.userIdx, (SELECT I.imgUrl FROM Image I WHERE U.userIdx = I.userIdx) userImgUrl, " +
                "U.nickName, U.userDong, U.mannerTemp, P.productIdx, P.title, " +
                "(SELECT C.categoryName FROM Category C WHERE C.categoryIdx = P.categoryIdx) categoryName, "+
                "P.updateAt, P.status, P.content, P.price " +
                "FROM Product P JOIN User U " +
                "ON P.userIdx = U.userIdx " +
                "WHERE P.productIdx = ?;";


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
                        rs.getTimestamp("updateAt"),
                        rs.getString("status"),
                        rs.getString("content"),
                        rs.getInt("price")), // RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
                getProductParams);// 한 개의 회원정보를 얻기 위한 jdbcTemplate 함수(Query, 객체 매핑 정보, Params)의 결과
    }

    //상품 삭제
    public int deleteProduct(int productIdx){
        String deleteUserQuery = "DELETE FROM Product WHERE productIdx = ?";
        int getProductParams = productIdx;
        return this.jdbcTemplate.update(deleteUserQuery, getProductParams);
    }

    // 특정 판매자의 판매 내역 조회
    public List<GetProductPreviewRes> getProductsBySeller(int userIdx){
        return this.jdbcTemplate.query(
                "SELECT P.productIdx, "+
                        "(SELECT I.imgUrl from Image I where I.productIdx = P.productIdx limit 1) imgUrl," +
                        "P.title, U.userDong, P.status, P.updateAt, P.price " +
                        "FROM Product P JOIN User U " +
                        "ON P.userIdx = U.userIdx " +
                        "WHERE P.userIdx = ? " +
                        "ORDER BY P.updateAt DESC",
                (rs, rowNum) -> new GetProductPreviewRes(
                        rs.getInt("productIdx"),
                        rs.getString("imgUrl"),
                        rs.getString("title"),
                        rs.getString("userDong"),
                        rs.getString("status"),
                        rs.getTimestamp("updateAt"),
                        rs.getInt("price")),userIdx);
    }

    // 특정 구매자의 구매 내역 조회
    public List<GetProductPreviewRes> getProductsByBuyer(int userIdx){
        return this.jdbcTemplate.query(
                "SELECT P.productIdx, "+
                        "(SELECT I.imgUrl from Image I where I.productIdx = P.productIdx limit 1) imgUrl," +
                        "P.title, U.userDong, P.status, P.updateAt, P.price " +
                        "FROM Product P JOIN User U " +
                        "ON P.userIdx = U.userIdx " +
                        "WHERE P.productIdx = (SELECT productIdx from Transaction where buyer=? ORDER BY updateAt DESC)",
                (rs, rowNum) -> new GetProductPreviewRes(
                        rs.getInt("productIdx"),
                        rs.getString("imgUrl"),
                        rs.getString("title"),
                        rs.getString("userDong"),
                        rs.getString("status"),
                        rs.getTimestamp("updateAt"),
                        rs.getInt("price")),userIdx);

    }
}
