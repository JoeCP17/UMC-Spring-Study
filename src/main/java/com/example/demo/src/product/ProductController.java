package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.Image.ImageProvider;
import com.example.demo.src.Image.ImageService;
import com.example.demo.src.product.model.*;
import com.example.demo.src.user.model.GetUserRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public BaseResponse<GetProductRes> editProduct(@PathVariable("productIdx") int productIdx, @RequestBody PutProductReq putProductReq){
        try {
            GetProductRes getProductRes = productService.editProduct(putProductReq);
            return new BaseResponse<>(getProductRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 상품 전체 조회 API
     * [GET] /app/products
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetProductListRes>> getProduct(@RequestParam(required = false) String search){
        try {
            if (search == null) { // query string인 search가 없을 경우, 그냥 전체 상품정보를 불러온다.
                List<GetProductListRes> productListRes = productProvider.getProductList();
                return new BaseResponse<>(productListRes);
            }
            System.out.println(search);
            // query string인 search가 있을 경우, title 에서 검색한다..
            List<GetProductListRes> productListRes = productProvider.getProductsByTitle(search);
            return new BaseResponse<>(productListRes);
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
    public BaseResponse<DeleteProductRes > deleteUser(@PathVariable("productIdx") int productIdx) {
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
            productRes.setProductImgUrlList(imageProvider.getImages("product",productIdx));
            return new BaseResponse<>(productRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
