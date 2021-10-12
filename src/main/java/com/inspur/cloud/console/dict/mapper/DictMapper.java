package com.inspur.cloud.console.dict.mapper;

import com.inspur.cloud.console.dict.entity.DictItemBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 一句话功能简介
 *
 * @author: xushiqiang
 * @date: 2019/2/13
 */
@Mapper
@Repository
public interface DictMapper
{
    @Select(value = "select id,group_id,item_id,item_value,item_desc,status,sort,created_time from dict_item t")
    List<DictItemBean> qryDictItemList();

    @Select(value = "select id,group_id,item_id,item_value,item_desc,status,sort,created_time from dict_item t where t.group_id = #{groupId}")
    List<DictItemBean> qryDictItemByGroupId(String groupId);

    @Select(value = "select id,group_id,item_id,item_value,item_desc,status,sort,created_time from dict_item t where t.group_id=#{groupId} and t.item_id=#{itemId}")
    DictItemBean qryDictItemByKey(@Param("groupId") String groupId, @Param("itemId") String itemId);
}
