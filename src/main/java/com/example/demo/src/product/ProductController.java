package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.image.ImageProvider;
import com.example.demo.src.image.ImageService;
import com.example.demo.src.image.model.GetImageRes;
import com.example.demo.src.image.model.PostImageReq;
import com.example.demo.src.product.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.PATCH_USERS_INVALID_STATUS;

@RestController
@RequestMapping("/app/products")
public class ProductController {
    final Logger logger = LoggerFactory.getLogger(this.getClass()); // Log를 남기기: 일단은 모르고 넘어가셔도 무방합니다.

    @Autowired
    private final ProductProvider productProvider;
    @Autowired
    private final ProductService productService;
    @Autowired
    private final ImageProvider imageProvider;
    @Autowired
    private final ImageService imageService;

    public ProductController(ProductProvider productProvider, ProductService productService,ImageProvider imageProvider, ImageService imageService){
        this.productProvider = productProvider;
        this.productService = productService;
        this.imageProvider = imageProvider;
        this.imageService = imageService;
    }

    /**
     * 상품등록 API
     * [POST] /app/products
     */
    //Body
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostProductRes> createProduct(@RequestBody PostProductReq postProductReq){
        try {
            PostProductRes postProductRes = productService.createProduct(postProductReq);
            // 이미지 등록
            for (String imgUrl : postProductReq.getImgUrlList()){
                imageService.createProductImage(new PostImageReq(
                        imgUrl,
                        postProductRes.getProductIdx()
                ));
            }
            return new BaseResponse<>(postProductRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 상품수정 API
     * [PUT] /app/products/:productIdx
     */
    //Body
    @ResponseBody
    @PutMapping("/{productIdx}")
    public BaseResponse<String> editProduct(@PathVariable("productIdx") int productIdx, @RequestBody PutProductReq putProductReq){
        try {
            productService.modifyProduct(putProductReq);
            String result = "상품정보가 수정되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 상품 전체 조회 API
     * [GET] /app/products
     * 
     * 상품 검색
     * /app/products?search=검색타이틀
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetProductPreviewRes>> getProduct(@RequestParam(required = false) String search){
        try {
            if (search == null) { // query string인 search가 없을 경우, 그냥 전체 상품정보를 불러온다.
                List<GetProductPreviewRes> getProductPreviewResList = productProvider.getAllProducts();
                for(GetProductPreviewRes getProductPreviewRes : getProductPreviewResList){
                    getProductPreviewRes.setImgUrl(
                            imageProvider.getOneProductImage(getProductPreviewRes.getProductIdx()).getImgUrl()
                    );
                }
                return new BaseResponse<>(getProductPreviewResList);
            }
            // query string인 search가 있을 경우, title 에서 검색한다..
            List<GetProductPreviewRes> getProductPreviewResList = productProvider.getProductsByTitle(search);
            for(GetProductPreviewRes getProductPreviewRes : getProductPreviewResList){
                getProductPreviewRes.setImgUrl(
                        imageProvider.getOneProductImage(getProductPreviewRes.getProductIdx()).getImgUrl()
                );
            }
            return new BaseResponse<>(getProductPreviewResList);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 현재 판매중인 상품만 조회 API
     * [GET] /app/products/active
     */
    @ResponseBody
    @GetMapping("/active")
    public BaseResponse<List<GetProductPreviewRes>> getActiveProduct(){
        try {
            List<GetProductPreviewRes> productListRes = productProvider.getActiveProducts();
            return new BaseResponse<>(productListRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 상품 상태 변경 API
     * [PATCH] /app/products/:productIdx/status
     */
    //body
    @ResponseBody
    @PatchMapping("/{productIdx}/status")
    public BaseResponse<String> modifyProductStatus(@PathVariable("productIdx") int productIdx, @RequestBody Product product){
        try {
            PatchProductReq patchProductReq = new PatchProductReq(productIdx, product.getStatus());
            String status = patchProductReq.getStatus();
            if (status.equals(ProductStatus.active.toString()) || status.equals(ProductStatus.reserved.toString())){
                productService.modifyProductStatus(patchProductReq);
                String result = "상품 상태가 수정되었습니다.";
                return new BaseResponse<>(result);
            } else {
                throw new BaseException(PATCH_USERS_INVALID_STATUS);
            }
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }



    /**
     * 상품 삭제 API
     *  [DELETE] /products/:productIdx
     */
    @ResponseBody
    @DeleteMapping("/{productIdx}")
    public BaseResponse<DeleteProductRes> deleteUser(@PathVariable("productIdx") int productIdx) {
        try {
            DeleteProductRes deleteProductRes = new DeleteProductRes(productService.deleteProduct(productIdx),imageService.deleteProductImage(productIdx));
            return new BaseResponse<>(deleteProductRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 특정 상품 상세 조회 API
     * [GET] /app/products/:productIdx
     */
    @ResponseBody
    @GetMapping("/{productIdx}")
    public BaseResponse<GetProductRes> getProduct(@PathVariable("productIdx") int productIdx){
        try {
            GetProductRes productRes = productProvider.getProduct(productIdx);
            List<String> imgUrlList = new ArrayList<>();
            for(GetImageRes getImageRes : imageProvider.getProductImages(productIdx)){
                imgUrlList.add(getImageRes.getImgUrl());
            }
            productRes.setProductImgUrlList(imgUrlList);
            return new BaseResponse<>(productRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /**
     * 특정 판매자의 구매 내역 조회
     * [GET] /app/products/seller/:userIdx
     */
    @ResponseBody
    @GetMapping("/seller/{userIdx}")
    public BaseResponse<List<GetProductPreviewRes>> getProductsBySeller(@PathVariable("userIdx") int userIdx){
        try {
            List<GetProductPreviewRes> productListRes = productProvider.getProductsBySeller(userIdx);
            return new BaseResponse<>(productListRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 특정 구매자의 구매 내역 조회
     * [GET] /app/products/buyer/:userIdx
     */
    @ResponseBody
    @GetMapping("/buyer/{userIdx}")
    public BaseResponse<List<GetProductPreviewRes>> getProductsByBuyer(@PathVariable("userIdx") int userIdx){
        try {
            List<GetProductPreviewRes> productListRes = productProvider.getProductsByBuyer(userIdx);
            return new BaseResponse<>(productListRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
