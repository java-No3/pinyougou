package cn.itcast.core.controller;

import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.service.ItemCatService;
import com.alibaba.dubbo.config.annotation.Reference;
import entity.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 商品分类管理
 */
@RestController
@RequestMapping("/itemCat")
public class ItemCatController {

    @Reference
    private ItemCatService itemCatService;
    //通过父Id查询商品分类结果集
    @RequestMapping("/findByParentId")
    public List<ItemCat> findByParentId(Long parentId){
        List<ItemCat> manager = itemCatService.findByParentId(parentId);
        return manager;
    }

    //查询一个
    @RequestMapping("/findOne")
    public ItemCat findOne(Long id){
        return itemCatService.findOne(id);
    }

    //查询所有
    @RequestMapping("/findAll")
    public List<ItemCat> findAll(){
        return itemCatService.findAll();
    }

    //ZQ规格批量审核
    @RequestMapping("/commit")
    public Result commit(Long[] ids){
        try {
            itemCatService.commitManager(ids);
            return new Result(true,"审核成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"审核失败");
        }

    }
}
