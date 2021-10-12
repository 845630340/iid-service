package com.inspur.cloud.console.dict.service;

import com.inspur.cloud.console.dict.entity.DictItemBean;
import com.inspur.cloud.console.dict.mapper.DictMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 一句话功能简介
 *
 * @author xushiqiang
 * @date 2019/2/13
 */
@Service("hsDictService")
public class DictService
{
    private final DictMapper dictMapper;

    @Autowired
    public DictService(DictMapper dictMapper)
    {
        this.dictMapper = dictMapper;
    }

    /**
     * 查询全量的字典项列表
     */
    public List<DictItemBean> qryDictItemList()
    {
        return dictMapper.qryDictItemList();
    }

    /**
     * 根据字典组获取字典项列表
     */
    @Cacheable(value = "dictCache", key = "#groupId")
    public List<DictItemBean> getDictItemByGroupId(String groupId)
    {
        return dictMapper.qryDictItemByGroupId(groupId);
    }

    /**
     * query dict data by groupId and itemId
     *
     * @param groupId dict group id
     * @param itemId  dict item id
     * @return object of dict
     */
    @Cacheable(value = "dictCache", key = "#groupId+#itemId")
    public DictItemBean getDictItemByKey(String groupId, String itemId)
    {
        return dictMapper.qryDictItemByKey(groupId, itemId);
    }

    @Cacheable(value = "dictCache", key = "#itemGroupId+ 'item_value_match'+ #itemValue")
    public DictItemBean match(String itemGroupId, String itemValue) {
        List<DictItemBean> dictItems = dictMapper.qryDictItemByGroupId(itemGroupId);
        for (DictItemBean dictItem : dictItems) {
            if (dictItem.getItemValue().equals(itemValue)) {
                return dictItem;
            }
        }
        return new DictItemBean();
    }
}
