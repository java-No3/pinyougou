package cn.itcast.core.service;

import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.pojo.order.OrderSearch;
import entity.PageResult;

public interface OrderService {
    void add(Order order);

    PageResult search(Integer page, Integer rows, Order order);

    PageResult countOrder(Integer page, Integer rows, OrderSearch orderSearch);
}
