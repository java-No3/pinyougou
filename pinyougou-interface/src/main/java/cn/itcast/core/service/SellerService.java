package cn.itcast.core.service;

import cn.itcast.core.pojo.seller.Seller;
import entity.PageResult;

import java.util.List;

public interface SellerService {
    void add(Seller seller);

    Seller findOne(String sellerId);

    // 商家管理 szj
    PageResult search(Integer page, Integer rows, Seller seller);

    // 查询所有卖家信息 szj
    List<Seller> findAll();
}
