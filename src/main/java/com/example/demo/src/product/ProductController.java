package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.Image.ImageService;
import com.example.demo.src.product.model.*;
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
    private final ImageService imageService;

    public ProductController(ProductProvider productProvider, ProductService productService,ImageService imageService){
        this.productProvider = productProvider;
        this.productService = productService;
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
     * 상품 전체 조회 API
     * [GET] /app/products
     */
    @ResponseBody
    @GetMapping("")
    public List<GetProductListRes> getProduct(){
        List<GetProductListRes> productListRes = productProvider.getProductList();
        return productListRes;
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

}
