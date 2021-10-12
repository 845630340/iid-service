package com.inspur.cloud.console.product.controller;

import com.inspur.cloud.common.page.Page;
import com.inspur.cloud.common.response.annotation.ResponseResult;
import com.inspur.cloud.common.response.constant.ApiConstants;
import com.inspur.cloud.console.product.entity.ProductDo;
import com.inspur.cloud.console.product.entity.ProductVo;
import com.inspur.cloud.console.product.entity.ProductsVO;
import com.inspur.cloud.console.product.service.ProductService;
import com.inspur.common.operationlog.annotation.OperationLog;
import com.inspur.iam.adapter.annotation.PermissionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.POST;
import java.util.List;

/**
 * @author jiangll01
 * @Date: 2020/8/27 10:52
 * @Description: 产品管理
 */
@ResponseResult
@RestController
@RequestMapping("/iid/v1")
public class ProductController {

    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * 添加产品
     * @param productVo 产品
     * @return  应答
     */
    @PermissionContext(loginAccess = true)
    @PostMapping("/product")
    @OperationLog(resourceType = ApiConstants.RESOURCETYPE,
            eventName = "addProduct")
    public String addProduct(@RequestBody ProductVo productVo) {
        return productService.addProduct(productVo);
    }

    /**
     * 修改产品
     * @param productId 产品id
     * @param productVo 产品
     * @return 应答
     */
    @PermissionContext(loginAccess = true)
    @PutMapping("/product/{productId}")
    @OperationLog(resourceType = ApiConstants.RESOURCETYPE,
            eventName = "updateProduct")
    public String updateProduct(@PathVariable("productId") String productId, @RequestBody ProductVo productVo) {
        return productService.updateProduct(productId, productVo);
    }

    /**
     * 查看产品详情
     * @param productId 产品id
     * @return  应答
     */
    @PermissionContext(loginAccess = true)
    @GetMapping("/product/{productId}/detail")
    @OperationLog(resourceType = ApiConstants.RESOURCETYPE,
            eventName = "productDetails")
    public ProductDo productDetails(@PathVariable("productId") String productId) {
        return productService.productDetails(productId);
    }

    /**
     * 设备列表
     * @param productId 产品id
     * @return 应答
     */
    @PermissionContext(loginAccess = true)
    @GetMapping("/product/{productId}/detail/{pageNo}/{pageSize}")
    @OperationLog(resourceType = ApiConstants.RESOURCETYPE,
            eventName = "productList")
    public Page productList(@PathVariable("productId") String productId,
                            @PathVariable("pageNo") int pageNo,
                            @PathVariable("pageSize") int pageSize) {
       return productService.qryProductList(pageNo, pageSize, productId);

    }

    /**
     * 产品类型列表（支持分页）
     * @param pageNo
     * @param pageSize
     * @param productName
     * @return
     */
    @PermissionContext(loginAccess = true)
    @OperationLog(resourceType = ApiConstants.RESOURCETYPE,
            eventName = "queryProductList")
    @GetMapping("/product/{pageNo}/{pageSize}")
    public Page queryProductList(@PathVariable("pageNo") int pageNo, @PathVariable("pageSize") int pageSize,
                            @RequestParam(value = "productName", required = false)String productName){
        return productService.queryProductList(pageNo, pageSize, productName);
    }

    /**
     * 产品类型列表查询
     * @return
     */
    @PermissionContext(loginAccess = true)
    @OperationLog(resourceType = ApiConstants.RESOURCETYPE,
            eventName = "productList")
    @GetMapping("/product")
    public List<ProductsVO> productList(){
        return productService.getProducts();
    }

}
