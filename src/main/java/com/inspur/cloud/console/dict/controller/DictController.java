package com.inspur.cloud.console.dict.controller;

import com.inspur.cloud.common.response.entity.ResponseBean;
import com.inspur.cloud.console.dict.cache.DictCache;
import com.inspur.cloud.console.dict.entity.DictItemBean;
import com.inspur.common.operationlog.annotation.OperationLog;
import com.inspur.iam.adapter.annotation.PermissionContext;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * refresh dict cache
 *
 * @author xushiqiang
 * @date 2019/2/13
 */
@RestController
@RequestMapping("/iid/v1/")
public class DictController
{
    private final DictCache dictCache;

    @Autowired
    public DictController(DictCache dictCache)
    {
        this.dictCache = dictCache;
    }

    @GetMapping("/dict/refresh")
    @PermissionContext(loginAccess = true)
    @OperationLog(resourceType = "i-id", resourceIds = "*", eventName = "refreshDictCache")
    public ResponseBean refreshDictCache()
    {
        dictCache.refresh();
        return new ResponseBean<>(null);
    }

    @GetMapping("/dict/refresh/groups/{groupId}")
    @PermissionContext(loginAccess = true)
    @OperationLog(resourceType = "i-id", resourceIds = "#groupId", eventName = "refreshDictGroupCache")
    public ResponseBean refreshDictGroupCache(@PathVariable("groupId") String groupId)
    {
        dictCache.refreshByKey(groupId);
        return new ResponseBean<>(null);
    }

    @GetMapping("/dict/groups/{groupId}")
    @PermissionContext(loginAccess = true)
    @OperationLog(resourceType = "i-id", resourceIds = "#groupId", eventName = "getDictItemByGroupId")
    public ResponseBean<List<DictItemBean>> getDictItemByGroupId(@PathVariable("groupId") String groupId)
    {
        List<DictItemBean> itemList = dictCache.getDictItemList(groupId);
        if (CollectionUtils.isEmpty(itemList)){
            dictCache.refreshByKey(groupId);
            itemList = dictCache.getDictItemList(groupId);
        }
        return new ResponseBean<>(itemList);
    }

    @GetMapping("/dict/groups/{groupId}/items/{itemId}")
    @PermissionContext(loginAccess = true)
    public ResponseBean<DictItemBean> getDictItemByKey(@PathVariable("groupId") String groupId,
                                                       @PathVariable("itemId") String itemId)
    {
        return new ResponseBean<>(DictCache.getItemByKey(groupId,itemId));
    }

    @GetMapping("/dict")
    @PermissionContext(loginAccess = true)
    public ResponseBean<?> getDict(@RequestParam String groupId)
    {
        return new ResponseBean<>(dictCache.getDictItemList(groupId));
    }
}
