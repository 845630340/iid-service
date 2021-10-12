package com.inspur.cloud.console.dict.cache;

import com.inspur.cloud.console.BaseTest;
import com.inspur.cloud.console.dict.entity.DictItemBean;
import com.inspur.cloud.console.dict.service.DictService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


public class DictCacheTest extends BaseTest {

    @Autowired
    private DictCache dictCache;

    @MockBean
    private DictService dictService;

    @Before
    public void before() {
        Map<String, List<DictItemBean>> cache = new HashMap<>();
        Map<String, Map<String, DictItemBean>> itemValueCache = new HashMap<>();
        Map<String, DictItemBean> value2 = new HashMap<>();
        value2.put("test", new DictItemBean());
        List<DictItemBean> value1 = new ArrayList<>();
        cache.put("test", value1);
    }

    @Test
    public void test01() {
        DictCache.getItemByKey("test", "test");
    }









    @Test
    public void test06() {
        dictCache.refreshByKey("x");
        dictCache.getDictItemList("test");
    }

    @Test
    public void test07() {
        List<DictItemBean> dictItemBeans = new ArrayList<>();
        DictItemBean dictItemBean = new DictItemBean();
        dictItemBean.setItemId("1");
        dictItemBeans.add(dictItemBean);
        when(dictService.getDictItemByGroupId(anyString())).thenReturn(dictItemBeans);
        dictCache.refreshByKey("test1");
        dictCache.clear("test1");
    }

}
