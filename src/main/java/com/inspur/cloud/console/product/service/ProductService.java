package com.inspur.cloud.console.product.service;

import com.github.pagehelper.PageHelper;
import com.inspur.cloud.common.exception.AssertUtils;
import com.inspur.cloud.common.exception.ExceptionCode;
import com.inspur.cloud.common.http.RequestHolder;
import com.inspur.cloud.common.page.Page;
import com.inspur.cloud.console.product.dao.ProductMapper;
import com.inspur.cloud.console.product.entity.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author jiangll01
 * @Date: 2020/8/27 10:54
 * @Description:
 */
@Service
public class ProductService {

    private ProductMapper productMapper;

    public ProductService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public String addProduct(ProductVo productVo) {
        verification(productVo);
        productVo.setId(UUID.randomUUID().toString())
                .setAccountId(RequestHolder.getUserId())
                .setCreatorId(RequestHolder.getCreatorId())
                .setCreatedBy(RequestHolder.getUserName())
                .setCreatedTime(new Date())
                .setStatus(1);
        productMapper.addProduct(productVo);
        return null;
    }

    public String updateProduct(String productId, ProductVo productVo) {
        verification(productVo);
        productVo.setId(productId)
                .setModifiedBy(RequestHolder.getUserName())
                .setUpdatedTime(new Date());
        productMapper.updateProduct(productVo,RequestHolder.getUserId());
        return null;

    }

    public ProductDo productDetails(String productId) {
        String userId = RequestHolder.getUserId();
        if (productMapper.deviceIsNull(productId,userId)!=0) {
            return productMapper.productDetails(productId,userId);
        }else {
            return productMapper.productDetail(productId,userId);
        }
    }

    /**
     * 校验参数
     */
    private void verification(ProductVo productVo) {
        AssertUtils.isTrue(productVo.getName().length() <= 36, ExceptionCode.LENGTH_OUT_OF_BOUNDS);
        if (StringUtils.isNotBlank(productVo.getProducer())&&StringUtils.isNotBlank(productVo.getSpec())) {
            AssertUtils.isTrue(productVo.getProducer().length() < 100 && productVo.getSpec().length() < 100, ExceptionCode.LENGTH_OUT_OF_BOUNDS);
        }
    }

    public Page qryProductList(int pageNo, int pageSize, String productId) {
        Page page = new Page();
        page.setPageNo(pageNo);
        page.setPageSize(pageSize);
        com.github.pagehelper.Page<Object> pageInfo = PageHelper.startPage(pageNo, pageSize);
        String userId = RequestHolder.getUserId();
        List<DeviceDo> deviceDosList = productMapper.qryProductListForPage(userId, productId);
        page.setTotalCount((int)pageInfo.getTotal());
        page.setData(deviceDosList);
        return page;
    }


    public Page queryProductList(int pageNo, int pageSize, String productName) {
        Page page = new Page();
        page.setPageNo(pageNo);
        page.setPageSize(pageSize);
        com.github.pagehelper.Page<Object> pageInfo = PageHelper.startPage(pageNo, pageSize);
        String userId = RequestHolder.getUserId();
        List<Product> productList = productMapper.queryProductList(userId, productName);
        ArrayList<ProductsVO> productVOList = new ArrayList<>();
        for (Product product : productList) {
            ProductsVO productVO = new ProductsVO();
            productVO.setId(product.getId());
            productVO.setName(product.getName());
            productVO.setProducer(product.getProducer());
            productVO.setSpec(product.getSpec());
            productVO.setSource(product.getProductSource());
            productVO.setStatus(Integer.valueOf(product.getStatus()));
            productVO.setCreatedTime(product.getCreatedTime());
            productVOList.add(productVO);
        }
        page.setTotalCount((int)pageInfo.getTotal());
        page.setData(productVOList);
        return page;
    }

    public List<ProductsVO> getProducts() {
        String userId = RequestHolder.getUserId();
        List<Product> productList = productMapper.getProducts(userId);
        ArrayList<ProductsVO> productVOList = new ArrayList<>();
        for (Product product : productList) {
            List<String> list = productMapper.getProductByName(userId, product.getName());
            if (product.getId().equals(list.get(0))) {
                ProductsVO productVO = new ProductsVO();
                productVO.setId(product.getId());
                productVO.setName(product.getName());
                productVO.setProducer(product.getProducer());
                productVO.setSpec(product.getSpec());
                productVO.setSource(product.getProductSource());
                productVO.setStatus(Integer.valueOf(product.getStatus()));
                productVO.setCreatedTime(product.getCreatedTime());
                productVOList.add(productVO);
            }
        }
        return productVOList;
    }
}
