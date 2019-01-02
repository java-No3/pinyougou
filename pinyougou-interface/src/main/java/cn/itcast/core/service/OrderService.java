package cn.itcast.core.service;

import cn.itcast.core.pojo.order.Order;
import entity.PageResult;

import java.util.Map;

public interface OrderService {
    void add(Order order);

    PageResult search(Integer page, Integer rows, Order order);

    PageResult countOrder(Map<String,String> searchMap);
}
