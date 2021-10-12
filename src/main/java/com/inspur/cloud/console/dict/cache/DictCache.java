package com.inspur.cloud.console.dict.cache;

import com.inspur.cloud.console.dict.entity.DictItemBean;
import com.inspur.cloud.console.dict.service.DictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class DictCache implements ApplicationRunner {
    // 缓存存储的结构是：Map<groupId,Map<dictItem,dictValue>>
    private static Map<String, List<DictItemBean>> cache = new HashMap<>();

    private static Map<String, Map<String, DictItemBean>> itemValueCache = new HashMap<>();

    private final DictService dictService;

    @Autowired
    public DictCache(DictService dictService) {
        this.dictService = dictService;
    }

    /**
     * 根据groupId获取字典对象
     *
     * @param groupId 字典组ID
     * @return 字典项
     */
    public List<DictItemBean> getDictItemList(String groupId) {
        return cache.get(groupId);
    }

    /**
     * 根据GroupId和Key获取对应的字典项
     *
     * @param groupId 字典组ID
     * @param itemId  字典项ID
     * @return 字典项对应的值
     */
    public static DictItemBean getItemByKey(String groupId, String itemId) {
        Map<String, DictItemBean> dictItemMap = itemValueCache.get(groupId);
        if (!CollectionUtils.isEmpty(dictItemMap)) {
            return dictItemMap.get(itemId);
        }

        return null;
    }

    /**
     * 根据GroupId和Key获取对应的字典值
     *
     * @param groupId 字典组ID
     * @param itemId  字典项ID
     * @return 字典项对应的值
     */
    public static String getItemValueByKey(String groupId, String itemId) {
        DictItemBean dictItem = getItemByKey(groupId, itemId);
        if (dictItem != null) {
            return dictItem.getItemValue();
        }
        return null;
    }

    public static String getItemDesc(String groupId, String itemId) {
        DictItemBean dictItem = getItemByKey(groupId, itemId);
        if (dictItem != null) {
            return dictItem.getItemDesc();
        }
        return null;
    }

    /**
     * 将字典项的全量数据刷新到缓存中
     */
    public void refresh() {
        log.info("DictCache.refresh()...begin...");
        // 先清空
        clearAll();

        List<DictItemBean> dictItemList = dictService.qryDictItemList();
        for (DictItemBean dictItemBean : dictItemList) {
            String groupId = dictItemBean.getGroupId();
            if (cache.containsKey(groupId)) {
                cache.get(groupId).add(dictItemBean);
                itemValueCache.get(groupId).put(dictItemBean.getItemId(), dictItemBean);
            } else {
                List<DictItemBean> dictItemBeans = new ArrayList<>();
                dictItemBeans.add(dictItemBean);
                cache.put(groupId, dictItemBeans);

                Map<String, DictItemBean> dictItemMap = new HashMap<>();
                dictItemMap.put(dictItemBean.getItemId(), dictItemBean);
                itemValueCache.put(groupId, dictItemMap);
            }
        }

        log.info("DictCache.refresh()...end...");
    }

    /**
     * 指定groupId进行刷新
     *
     * @param groupId 字典组ID
     */
    public void refreshByKey(String groupId) {
        List<DictItemBean> dictItemBeans = dictService.getDictItemByGroupId(groupId);
        cache.put(groupId, dictItemBeans);

        Map<String, DictItemBean> map = new HashMap<>();
        for (DictItemBean dictItemBean : dictItemBeans) {
            map.put(dictItemBean.getItemId(), dictItemBean);
        }
        itemValueCache.put(groupId, map);
    }

    public void clear(String cacheKey) {
        //先将原Map清空，然后再remove这个groupId
        cache.get(cacheKey).clear();
        cache.remove(cacheKey);
        itemValueCache.get(cacheKey).clear();
        itemValueCache.remove(cacheKey);
    }

    public void clearAll() {
        // 对于Map中嵌套Map的，手动进行数据清空
        for (Map.Entry<String, Map<String, DictItemBean>> entry : itemValueCache.entrySet()) {
            entry.getValue().clear();
        }
        itemValueCache.clear();

        // 对于Map中嵌套Map的，手动进行数据清空
        for (Map.Entry<String, List<DictItemBean>> entry : cache.entrySet()) {
            entry.getValue().clear();
        }
        cache.clear();
    }

    public static DictItemBean match(String itemGroupId, String itemValue) {
        List<DictItemBean> dictItems = cache.get(itemGroupId);
        for (DictItemBean dictItem : dictItems) {
            if (dictItem.getItemValue().equals(itemValue)) {
                return dictItem;
            }
        }
        return new DictItemBean();
    }

    @Override
    public void run(ApplicationArguments args) {
        refresh();
    }
}
