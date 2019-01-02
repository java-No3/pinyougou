package cn.itcast.core.controller;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.seckill.SeckillOrder;
import cn.itcast.core.service.SeckillOrderManagerService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import entity.PageResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 运营商后台秒杀订单查询  LH
 */
@RestController
@RequestMapping("/seckillorder")
public class SeckillOrderManagerController {
    @Reference
    private SeckillOrderManagerService seckillOrderManagerService;

    @RequestMapping("/search")
    public PageInfo<SeckillOrder> search(Integer page, Integer rows, @RequestBody SeckillOrder seckillOrder){
        return seckillOrderManagerService.search(page,rows,seckillOrder);
    }
    //查询所有订单结果集   LH
    @RequestMapping("/findAll")
    public List<SeckillOrder> findAll(){
        return seckillOrderManagerService.findAll();
    }
    //查询订单分页对象   LH
    @RequestMapping("/findPage")
    public PageResult findPage(Integer pageNum, Integer pageSize){

        return seckillOrderManagerService.findPage(pageNum,pageSize);

    }
}
