package com.example.demo.src.Image;

import com.example.demo.src.Image.model.GetImageRes;
import com.example.demo.src.Image.model.PostImageReq;
import com.example.demo.src.product.model.GetProductListRes;
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

    // 이미지 등록
    public int createImage(PostImageReq postImageReq){
        String createImageQuery = "insert into Image (imgUrl, imgCategory, productIdx, postIdx, userIdx) VALUES (?,?,?,?,?)";
        Object[] createImageParams = new Object[]{postImageReq.getImgUrl(), postImageReq.getImgCategory(), postImageReq.getProductIdx(), postImageReq.getPostIdx(), postImageReq.getUserIdx()}; // 동적 쿼리의 ?부분에 주입될 값
        this.jdbcTemplate.update(createImageQuery, createImageParams);
        String lastInserIdQuery = "select last_insert_id()"; // 가장 마지막에 삽입된(생성된) id값은 가져온다.
        return this.jdbcTemplate.queryForObject(lastInserIdQuery, int.class); // 해당 쿼리문의 결과 마지막으로 삽인된 유저의 userIdx번호를 반환한다.
    }

    //이미지 조회
    public List<String> getImageList(String imgCategory, int idx) {
        String getImageListQuery = "";
        switch (imgCategory) {
            case "post":
                getImageListQuery = "SELECT imgUrl FROM Image WHERE postIdx = ?";
                break;
            case "product":
                getImageListQuery = "SELECT imgUrl FROM Image WHERE productIdx = ?";
                break;
            case "user":
                getImageListQuery = "SELECT imgUrl FROM Image WHERE userIdx = ?";
                break;
            default:
                return null;
        }
        int getIdxParams = idx;
        return this.jdbcTemplate.query(getImageListQuery,
                (rs, rowNum) -> rs.getString("imgUrl"), getIdxParams);
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
