package com.inspur.cloud.console.product.controller;

import com.inspur.cloud.common.page.Page;
import com.inspur.cloud.console.BaseTest;
import com.inspur.cloud.console.product.entity.ProductDo;
import com.inspur.cloud.console.product.entity.ProductVo;
import com.inspur.cloud.console.product.entity.ProductsVO;
import com.inspur.cloud.console.product.service.ProductService;
import com.inspur.cloud.console.usercert.entity.UserCert;
import com.inspur.cloud.console.usercert.service.UserCertService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author jiangll01
 * @Date: 2020/9/14 11:26
 * @Description:
 */

public class ProductControllerTest extends BaseTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;
    private static HttpHeaders headers = new HttpHeaders();

    private static final String AUTHORIZATION = "Authorization";


    @Test
    public void addProduct() throws Exception {
        given(productService.addProduct(any(ProductVo.class))).willReturn("1");
        mockMvc.perform(MockMvcRequestBuilders.post("/iid/v1/product").headers(headers));
    }

    @Test
    public void updateProduct() throws Exception {
        given(productService.updateProduct(anyString(), any(ProductVo.class))).willReturn("12");
        mockMvc.perform(MockMvcRequestBuilders.put("/iid/v1/product/{productId}","1").headers(headers));
    }

    @Test
    public void productDetails() throws Exception {
        given(productService.productDetails(anyString())).willReturn(new ProductDo());
        mockMvc.perform(MockMvcRequestBuilders.get("/iid/v1/product/{productId}/detail","1").headers(headers));
    }

    @Test
    public void productList() throws Exception {
        given(productService.qryProductList(any(int.class),any(int.class),anyString())).willReturn(new Page());
        mockMvc.perform(MockMvcRequestBuilders.get("/iid/v1/product/{productId}/detail/{pageNo}/{pageSize}","1",1,10).headers(headers));
    }

    @Test
    public void queryProductList() throws Exception{
        Page page = new Page();
        Mockito.when(productService.queryProductList(anyInt(), anyInt(), anyString())).thenReturn(page);
        mockMvc.perform(MockMvcRequestBuilders.get("/iid/v1/product/1/10")
                .header(AUTHORIZATION, "123"))
                .andExpect(status().isOk());
    }

    @Test
    public void productList1() throws Exception{
        List<ProductsVO> productsVOS = new ArrayList<>();
        Mockito.when(productService.getProducts()).thenReturn(productsVOS);
        mockMvc.perform(MockMvcRequestBuilders.get("/iid/v1/product")
                .header(AUTHORIZATION, "123"))
                .andExpect(status().isOk());
    }
}
