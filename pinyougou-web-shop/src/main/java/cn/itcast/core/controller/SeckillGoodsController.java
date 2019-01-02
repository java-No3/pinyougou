package cn.itcast.core.controller;

import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.service.SeckillGoodsSellerService;
import entity.Result;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 秒杀商品申请  LH
 */
@RestController
@RequestMapping("/seckillGoods")
public class SeckillGoodsController {

    @Reference
    private SeckillGoodsSellerService seckillGoodsSellerService;

    @RequestMapping("/add")
    public Result add(@RequestBody Goods goods){
        try {
            seckillGoodsSellerService.add(goods);
            return new Result(true, "申请添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "申请添加失败");
        }
    }
}
