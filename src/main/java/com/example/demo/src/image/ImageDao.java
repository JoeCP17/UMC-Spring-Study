package com.example.demo.src.image;

import com.example.demo.src.image.model.GetImageRes;
import com.example.demo.src.image.model.PostImageReq;
import com.example.demo.src.user.model.GetUserRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ImageDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // 상품 이미지 등록
    public int createProductImage(PostImageReq postImageReq){
        String createProductImageQuery = "insert into Image (imgUrl, imgCategory, productIdx) VALUES (?,?,?)";
        Object[] createImageParams = new Object[]{postImageReq.getImgUrl(), "product", postImageReq.getIdx()}; // 동적 쿼리의 ?부분에 주입될 값
        this.jdbcTemplate.update(createProductImageQuery, createImageParams);
        String lastInserIdQuery = "select last_insert_id()"; // 가장 마지막에 삽입된(생성된) id값은 가져온다.
        return this.jdbcTemplate.queryForObject(lastInserIdQuery, int.class); // 해당 쿼리문의 결과 마지막으로 삽인된 유저의 userIdx번호를 반환한다.
    }

    // 상품 이미지들 조회
    public List<GetImageRes> getProductImages(int productIdx){
        String getProductImagesQuery = "select * from Image where productIdx = ?";
        return this.jdbcTemplate.query(getProductImagesQuery,
                (rs, rowNum) -> new GetImageRes(
                        rs.getInt("imgIdx"),
                        rs.getString("imgUrl")
                ), productIdx);
    }

    // 대표 상품 이미지 1개 조회
    public GetImageRes getOneProductImage(int productIdx){
        String getProductImageQuery = "select * from Image where productIdx = ? limit 1";
        return this.jdbcTemplate.queryForObject(getProductImageQuery,
                (rs, rowNum) -> new GetImageRes(
                        rs.getInt("imgIdx"),
                        rs.getString("imgUrl")
                ), productIdx);
    }


    // 유저 이미지 조회
    public GetImageRes getUserImage(int userIdx) {
        String getUserImageQuery = "select * from Image where userIdx = ?";
        return this.jdbcTemplate.queryForObject(getUserImageQuery,
                (rs, rowNum) -> new GetImageRes(
                        rs.getInt("imgIdx"),
                        rs.getString("imgUrl")
                ), userIdx);
    }

    // 이미지 삭제
    public int deleteImage(String imgCategory, int idx) {
        String deleteImageQuery = "";
        switch (imgCategory) {
            case "post":
                deleteImageQuery = "DELETE FROM Image WHERE postIdx = ?";
                break;
            case "product":
                deleteImageQuery = "DELETE FROM Image WHERE productIdx = ?";
                break;
            case "user":
                deleteImageQuery = "DELETE FROM Image WHERE userIdx = ?";
                break;
            default:
                return 0;
        }
        int getIdxParams = idx;
        return this.jdbcTemplate.update(deleteImageQuery, getIdxParams);
    }

}
