package cn.itcast.core.service;

import cn.itcast.core.pojo.seckill.SeckillOrder;



/**
 *关于秒杀商品订单
 */
public interface SeckillOrderService {

    void submitOrder(Long seckillId,String userId);

    SeckillOrder searchOrderFromRedis(String userId);

    void saveOrderFromRedis(String userId,Long orderId,String transactionId);

    void deleteOrderFromRedis(String userId,Long orderId);
}