package cn.itcast.core.controller;

import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.service.OrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import entity.PageResult;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * wph
 * 订单查询
 */
@RestController
@RequestMapping("/orders")
public class ordersController {

    @Reference
    private OrderService orderService;

    //订单分页查询  带条件
    @RequestMapping("/search")
    public PageResult searchOrder(Integer page, Integer rows ,@RequestBody Order order){
        //商家ID
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        order.setSellerId(name);
        return orderService.search(page,rows,order);
    }
}
