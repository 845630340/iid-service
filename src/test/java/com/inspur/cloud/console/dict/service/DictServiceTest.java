package com.inspur.cloud.console.dict.service;

import com.inspur.cloud.console.BaseTest;
import com.inspur.cloud.console.dict.entity.DictItemBean;
import com.inspur.cloud.console.dict.mapper.DictMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


public class DictServiceTest extends BaseTest {
    @Autowired
    private DictService dictService;

    @MockBean
    private DictMapper dictMapper;

    @Test
    public void test01() {
        when(dictMapper.qryDictItemByGroupId(anyString())).thenReturn(null);
        dictService.getDictItemByGroupId("test");
    }

    @Test
    public void test02() {
        when(dictMapper.qryDictItemByKey(anyString(), anyString())).thenReturn(null);
        dictService.getDictItemByKey("test", "test");
    }

    @Test
    public void test03() {
        List<DictItemBean> dictItems = new ArrayList<>();
        DictItemBean dictItemBean = new DictItemBean();
        dictItemBean.setItemValue("test");
        dictItems.add(dictItemBean);
        when(dictMapper.qryDictItemByGroupId(anyString())).thenReturn(dictItems);
        dictService.match("test", "test");
    }

    @Test
    public void test04() {
        List<DictItemBean> dictItems = new ArrayList<>();
        DictItemBean dictItemBean = new DictItemBean();
        dictItemBean.setItemValue("test01");
        dictItems.add(dictItemBean);
        when(dictMapper.qryDictItemByGroupId(anyString())).thenReturn(dictItems);
        dictService.match("test", "test");
    }
}
