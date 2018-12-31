package cn.itcast.core.controller;

import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.pojo.order.OrderSearch;
import cn.itcast.core.service.OrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import entity.PageResult;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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


    //订单统计查询
    @RequestMapping("/count")
    public PageResult countOrder(Integer page, Integer rows , @RequestBody OrderSearch orderSearch){
        //商家ID
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        orderSearch.setSellerId(name);

        return orderService.countOrder(page,rows,orderSearch);
    }
}
