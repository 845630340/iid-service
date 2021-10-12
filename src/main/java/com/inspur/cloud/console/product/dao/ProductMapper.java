package com.inspur.cloud.console.product.dao;

import com.inspur.cloud.console.product.entity.DeviceDo;
import com.inspur.cloud.console.product.entity.Product;
import com.inspur.cloud.console.product.entity.ProductDo;
import com.inspur.cloud.console.product.entity.ProductVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author jiangll01
 * @Date: 2020/8/27 11:08
 * @Description:
 */
@Repository
@Mapper
public interface ProductMapper {
    void addProduct(ProductVo productVo);

    void updateProduct(@Param("productVo") ProductVo productVo, @Param("userId") String userId);

    Integer deviceIsNull(@Param("productId") String productId, @Param("userId")String userId);

    ProductDo productDetails(@Param("productId") String productId, @Param("userId")String userId);

    List<DeviceDo> qryProductListForPage(@Param("userId")String userId,@Param("productId") String productId);


    List<Product> queryProductList(@Param("userId") String userId, @Param("productName") String productName);

    List<Product> getProducts(@Param("userId") String userId);

    ProductDo productDetail(@Param("id")String id, @Param("userId") String userId);

    List<String> getProductByName(@Param("userId")String userId, @Param("name") String name);
}
