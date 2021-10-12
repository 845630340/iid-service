package com.inspur.cloud.console.product.service;

import com.inspur.cloud.common.page.Page;
import com.inspur.cloud.console.BaseTest;
import com.inspur.cloud.console.kgc.service.KgcService;
import com.inspur.cloud.console.product.dao.ProductMapper;
import com.inspur.cloud.console.product.entity.Product;
import com.inspur.cloud.console.product.entity.ProductDo;
import com.inspur.cloud.console.product.entity.ProductVo;
import com.inspur.cloud.console.usercert.dao.UserCertDao;
import com.inspur.cloud.console.usercert.entity.UserCert;
import com.inspur.cloud.console.usercert.service.UserCertService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * @author jiangll01
 * @Date: 2020/9/14 11:37
 * @Description:
 */
public class ProductServiceTest extends BaseTest {

    @Autowired
    private ProductService productService;

    @MockBean
    private ProductMapper productMapper;

    @Test
    public void addProduct() {
        doNothing().when(productMapper).addProduct(any(ProductVo.class));
        ProductVo productVo = new ProductVo();
        productVo.setName("12")
                .setProducer("12")
                .setSpec("12")
                .setStatus(1);
        productService.addProduct(productVo);
    }

    @Test
    public void updateProduct() {
        doNothing().when(productMapper).updateProduct(any(ProductVo.class), anyString());
        ProductVo productVo = new ProductVo();
        productVo.setName("12")
                .setProducer("12")
                .setSpec("12")
                .setStatus(1);
        productService.updateProduct("1", productVo);
    }

    @Test
    public void productDetails() {
        when(productMapper.deviceIsNull(anyString(), anyString())).thenReturn(1);
        given(productMapper.productDetails(anyString(), anyString())).willReturn(new ProductDo());
        productService.productDetails("1");
    }

    @Test
    public void qryProductList() {
        given(productMapper.qryProductListForPage(anyString(),anyString())).willReturn(new ArrayList());
        productService.qryProductList(1,10,"1");
    }

    @Test
    public void queryProductList() {
        ArrayList<Product> products = new ArrayList<>();
        Product product = Product.builder().id("11").status("1").build();
        products.add(product);
        Mockito.when(productMapper.queryProductList(anyString(), anyString())).thenReturn(products);
        productService.queryProductList(1, 2, "");
    }

    @Test
    public void getProducts() {
        productService.getProducts();
    }
}
