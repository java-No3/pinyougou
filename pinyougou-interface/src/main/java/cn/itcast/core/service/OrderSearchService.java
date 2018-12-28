package cn.itcast.core.service;
import cn.itcast.core.pojo.order.Order;
import entity.PageResult;
import java.util.List;

public interface OrderSearchService {

//    void add(Order order);

    List<Order> findAll();

    PageResult findPage(Integer pageNum, Integer pageSize);

    PageResult search(Integer pageNum, Integer pageSize, Order order);

//    void update(Order order);

    Order findOne(Long orderId);
}
