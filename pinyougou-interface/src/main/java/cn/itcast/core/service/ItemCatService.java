package cn.itcast.core.service;

import cn.itcast.core.pojo.item.ItemCat;

import java.util.List;

public interface ItemCatService {

    List<ItemCat> findByParentId(Long parentId);

    ItemCat findOne(Long id);

    List<ItemCat> findAll();

    void commitManager(Long[] ids);

    void commitShop(Long[] ids);

    List<ItemCat> findByParentIdShop(Long parentId);
}
