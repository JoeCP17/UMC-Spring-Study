package com.example.demo.src.product;

import com.example.demo.src.product.model.GetProductListRes;
import com.example.demo.src.product.model.GetProductRes;
import com.example.demo.src.product.model.PostProductReq;
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

        // 이미지 등록
        String createImgQuery = "insert into Image (imgCategory,imgUrl,productIdx) VALUES (?,?,?)"; // 실행될 동적 쿼리문
        for(String imgUrl : postProductReq.getImgUrlList()){
            Object[] createImgParams = new Object[]{"product",imgUrl,productIdx}; // 동적 쿼리의 ?부분에 주입될 값
            this.jdbcTemplate.update(createImgQuery, createImgParams);
        }

        return productIdx;
    }


    //상품 전체 조회
    public List<GetProductListRes> productListRes(){
        return this.jdbcTemplate.query("Select * from Product",
                (rs, rowNum) -> new GetProductListRes(
                        rs.getInt("productIdx"),
                        rs.getString("title"),
                        rs.getString("dong"),
                        rs.getString("imgUrl"),
                        rs.getString("status"),
                        rs.getTimestamp("updateAt"),
                        rs.getInt("price")));


    }

    //상품 삭제
    public int deleteProduct(int productIdx){
        String deleteUserQuery = "DELETE FROM Product WHERE productIdx = ?";
        int getProductParams = productIdx;
        return this.jdbcTemplate.update(deleteUserQuery, getProductParams);
    }
}
