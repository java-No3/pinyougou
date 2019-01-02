package cn.itcast.core.service;

import cn.itcast.core.dao.item.ItemCatDao;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.pojo.item.ItemCatQuery;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 分类管理
 */
@Service
@Transactional
public class ItemCatServiceImpl implements  ItemCatService {
    @Autowired
    private ItemCatDao itemCatDao;
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public List<ItemCat> findByParentId(Long parentId) {

        //1:查询Mysql数据中分类结果集 (所有)
        List<ItemCat> itemCats = findAll();
        //2:将所有商品分类结果集保存到缓存中(Hash类型)
        for (ItemCat itemCat : itemCats) {
            redisTemplate.boundHashOps("itemCat").put(itemCat.getName(),itemCat.getTypeId());
        }

        ItemCatQuery query = new ItemCatQuery();
        query.createCriteria().andParentIdEqualTo(parentId).andStatusIsNotZero();
        return itemCatDao.selectByExample(query);
    }

    @Override
    public List<ItemCat> findByParentIdShop(Long parentId) {

        //1:查询Mysql数据中分类结果集 (所有)
        List<ItemCat> itemCats = findAll();
        //2:将所有商品分类结果集保存到缓存中(Hash类型)
        for (ItemCat itemCat : itemCats) {
            redisTemplate.boundHashOps("itemCat").put(itemCat.getName(),itemCat.getTypeId());
        }

        ItemCatQuery query = new ItemCatQuery();
        query.createCriteria().andParentIdEqualTo(parentId);
        return itemCatDao.selectByExample(query);
    }

    @Override
    public ItemCat findOne(Long id) {
        return itemCatDao.selectByPrimaryKey(id);
    }

    @Override
    public List<ItemCat> findAll() {

        List<ItemCat> itemCats = itemCatDao.selectByExample(null);
        return itemCats;
    }

    @Override
    public void commitManager(Long[] ids) {
        for (Long id : ids) {
            ItemCat itemCat = itemCatDao.selectByPrimaryKey(id);
            if ("1".equals(itemCat.getStatus())){
                itemCat.setStatus("2");
                itemCatDao.updateByPrimaryKeySelective(itemCat);
            }
        }


    }

    @Override
    public void commitShop(Long[] ids) {
        for (Long id : ids) {
            ItemCat itemCat = itemCatDao.selectByPrimaryKey(id);
            if ("0".equals(itemCat.getStatus())){
                itemCat.setStatus("1");
                itemCatDao.updateByPrimaryKeySelective(itemCat);
            }
        }
    }
}
