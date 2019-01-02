package cn.itcast.core.service;

import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import entity.PageResult;

public interface SeckillGoodsSellerService {
    void add(Goods goods);

    PageResult search(Integer page, Integer rows, SeckillGoods seckillGoods);

    void updateStatus(Long[] ids, String status);
}
