package cn.itcast.core.controller;

import cn.itcast.core.pojo.seckill.SeckillOrder;
import cn.itcast.core.service.SeckillOrderQTService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import entity.PageResult;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 商家后台秒杀订单查询  LH
 */
@RestController
@RequestMapping("/order")
public class SeckillOrderController {
    @Reference
    private SeckillOrderQTService seckillOrderQTService;

    @RequestMapping("/search")
    public PageInfo<SeckillOrder> search(Integer page, Integer rows, @RequestBody SeckillOrder seckillOrder){
        //设置商家id
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        seckillOrder.setSellerId(name);
        return seckillOrderQTService.search(page,rows,seckillOrder);
    }
    //查询所有订单结果集   LH
    @RequestMapping("/findAll")
    public List<SeckillOrder> findAll(){
        return seckillOrderQTService.findAll();
    }
    //查询订单分页对象   LH
    @RequestMapping("/findPage")
    public PageResult findPage(Integer pageNum, Integer pageSize){

        return seckillOrderQTService.findPage(pageNum,pageSize);

    }
}
