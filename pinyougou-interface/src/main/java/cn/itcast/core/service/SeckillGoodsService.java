package cn.itcast.core.service;

import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import entity.PageResult;
import pojogroup.GoodsVo;

import java.util.List;

/**
 * 查询秒杀商品信息   LH
 */
public interface SeckillGoodsService {

    List<SeckillGoods> findList();

    SeckillGoods findOneFromRedis(Long id);

}
