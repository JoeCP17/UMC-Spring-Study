package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.product.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

@RestController
@RequestMapping("/app/products")
public class ProductController {
    final Logger logger = LoggerFactory.getLogger(this.getClass()); // Log를 남기기: 일단은 모르고 넘어가셔도 무방합니다.

    @Autowired
    private final ProductProvider productProvider;
    @Autowired
    private final ProductService productService;
    @Autowired
    private final JwtService jwtService;

    public ProductController(ProductProvider productProvider, ProductService productService, JwtService jwtService){
        this.productProvider = productProvider;
        this.productService = productService;
        this.jwtService = jwtService;
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
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(postProductReq.getUserIdx() != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            PostProductRes postProductRes = productService.createProduct(postProductReq);
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
    public BaseResponse<String> modifyProduct(@PathVariable("productIdx") int productIdx, @RequestBody PostProductReq postProductReq){
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 변경하려는 상품의 userIdx 같은지 확인
            if(productProvider.getProduct(productIdx).getUserIdx() != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            productService.modifyProduct(productIdx, postProductReq);
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
    public BaseResponse<List<GetProductPreviewRes>> getProduct(@RequestParam(required = false) String search,
                                                               @RequestParam(required = false, defaultValue = "1") int page,
                                                               @RequestParam(required = false, defaultValue = "10") int size){
        try {
            if (search == null) { // query string인 search가 없을 경우, 그냥 전체 상품정보를 불러온다.
                List<GetProductPreviewRes> getProductPreviewResList = productProvider.getAllProducts(page,size);
                return new BaseResponse<>(getProductPreviewResList);
            }
            // query string인 search가 있을 경우, title 에서 검색한다..
            List<GetProductPreviewRes> getProductPreviewResList = productProvider.getProductsByTitle(search,page,size);
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
    public BaseResponse<List<GetProductPreviewRes>> getActiveProduct(@RequestParam(required = false, defaultValue = "1") int page,
                                                                     @RequestParam(required = false, defaultValue = "10")int size){
        try {
            List<GetProductPreviewRes> productListRes = productProvider.getActiveProducts(page,size);
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
    public BaseResponse<String> modifyProductStatus(@PathVariable("productIdx") int productIdx, @RequestBody Product product) {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 변경하려는 상품의 userIdx 같은지 확인
            if(productProvider.getProduct(productIdx).getUserIdx() != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            // 판매중으로 변경
            String status = product.getStatus();
            productService.modifyProductStatus(new PatchProductReq(productIdx, status));
            String result = "상품 상태가 수정되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 상품 구매자 변경 API
     * [PATCH] /app/products/:productIdx/buyer
     */
    //body
    @ResponseBody
    @PatchMapping("/{productIdx}/buyer")
    public BaseResponse<String> modifyProductBuyer(@PathVariable("productIdx") int productIdx, @RequestBody Product product) {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 변경하려는 상품의 userIdx 같은지 확인
            if(productProvider.getProduct(productIdx).getUserIdx() != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            int buyer = product.getBuyer();
            productService.modifyProductBuyer(new PatchProductReq(productIdx, buyer));
            String result = "상품 구매자가 수정되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 상품 끌어올리기 (pullUpAt 변경)
     * [PATCH] /app/products/:productIdx/pullup
     */
    //body
    @ResponseBody
    @PatchMapping("/{productIdx}/pull-up")
    public BaseResponse<String> pullUpProduct(@PathVariable("productIdx") int productIdx) {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 변경하려는 상품의 userIdx 같은지 확인
            if(productProvider.getProduct(productIdx).getUserIdx() != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            productService.pullUpProduct(productIdx);
            String result = "끌어올려졌습니다.";
            return new BaseResponse<>(result);
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
    public BaseResponse<String> deleteUser(@PathVariable("productIdx") int productIdx) {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 변경하려는 상품의 userIdx 같은지 확인
            if(productProvider.getProduct(productIdx).getUserIdx() != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            productService.deleteProduct(productIdx);
            String result = "삭제되었습니다.";
            return new BaseResponse<>(result);
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
            return new BaseResponse<>(productRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /**
     * 특정 판매자의 판매 내역 조회
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
