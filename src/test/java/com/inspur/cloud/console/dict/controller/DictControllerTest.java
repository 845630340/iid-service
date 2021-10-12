package com.inspur.cloud.console.dict.controller;

import com.inspur.cloud.console.BaseTest;
import com.inspur.cloud.console.dict.cache.DictCache;
import com.inspur.cloud.console.dict.entity.DictItemBean;
import com.inspur.cloud.console.dict.service.DictService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


public class DictControllerTest extends BaseTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DictService dictService;
    private static HttpHeaders headers = new HttpHeaders();

    @MockBean
    private DictCache dictCache;


    @Test
    public void refreshDictCache() throws Exception {
        doNothing().when(dictCache).refresh();
        mockMvc.perform(MockMvcRequestBuilders.get("/iid/v1/dict/refresh").headers(headers));
    }

    @Test
    public void refreshDictGroupCache() throws Exception {
        doNothing().when(dictCache).refreshByKey(anyString());
        mockMvc.perform(MockMvcRequestBuilders.get("/iid/v1/dict/refresh/groups/123").headers(headers));
    }

    @Test
    public void getDictItemByGroupId() throws Exception {
        DictItemBean dictItemBean = new DictItemBean();
        List<DictItemBean> itemList = new ArrayList<>();
        itemList.add(dictItemBean);
        when(dictCache.getDictItemList(anyString())).thenReturn(itemList);
        mockMvc.perform(MockMvcRequestBuilders.get("/iid/v1/dict/groups/123").headers(headers));
    }

    @Test
    public void getDictItemByGroupId02() throws Exception {
        when(dictCache.getDictItemList(anyString())).thenReturn(new ArrayList<>());
        doNothing().when(dictCache).refreshByKey(anyString());
        mockMvc.perform(MockMvcRequestBuilders.get("/iid/v1/dict/groups/123").headers(headers));
    }

    @Test
    public void getDict() throws Exception {
        when(dictCache.getDictItemList(anyString())).thenReturn(new ArrayList<>());
        mockMvc.perform(MockMvcRequestBuilders.get("/iid/v1/dict").headers(headers).param("groupId","1234"));
    }
}
