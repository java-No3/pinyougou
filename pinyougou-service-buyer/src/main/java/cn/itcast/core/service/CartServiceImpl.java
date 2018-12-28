package cn.itcast.core.service;

import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.pojo.Cart;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.order.OrderItem;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车管理
 */
@Service
@SuppressWarnings("all")
public class CartServiceImpl implements CartService {


    @Autowired
    private ItemDao itemDao;
    //查询库存
    @Override
    public Item findItemById(Long itemId) {
        return itemDao.selectByPrimaryKey(itemId);
    }

    //将购物车装满
    @Override
    public List<Cart> findCartList(List<Cart> cartList) {

        for (Cart cart : cartList) {
            //商家ID不用了
            Item item = null;
            //商家名称
            List<OrderItem> orderItemList = cart.getOrderItemList();
            for (OrderItem orderItem : orderItemList) {
                //查询库存
                item = findItemById(orderItem.getItemId());//库存ID
                //orderItem.getNum()//数量
                //图片
                orderItem.setPicPath(item.getImage());
                //标题
                orderItem.setTitle(item.getTitle());
                //单价
                orderItem.setPrice(item.getPrice());
                //小计
                orderItem.setTotalFee(new BigDecimal(orderItem.getPrice().doubleValue()*orderItem.getNum()));
            }
            //商家名称
            cart.setSellerName(item.getSeller());
        }
        return cartList;
    }

    @Autowired
    private RedisTemplate redisTemplate;
    //合并 新购物车集合到老购物车集合中
    @Override
    public void merge(List<Cart> newCartList, String name) {
        //1:获取缓存中购物车结果集
        List<Cart> oldCartList = (List<Cart>) redisTemplate.boundHashOps("CART").get(name);
        //2:合并新老购物车集合  二大集合在合并
        oldCartList = merge1(newCartList,oldCartList);
        //3:将老购物车结果集保存到缓存中
        redisTemplate.boundHashOps("CART").put(name,oldCartList);


    }

    @Override
    public List<Cart> findCartListFromRedis(String name) {
        return (List<Cart>) redisTemplate.boundHashOps("CART").get(name);
    }

    // 合并新老购物车集合  二大集合在合并
    public List<Cart> merge1(List<Cart> newCartList,List<Cart> oldCartList){
        //判断新车有值
        if(null != newCartList && newCartList.size() > 0){
            //判断老车有值
            if(null != oldCartList && oldCartList.size() > 0) {
                //新车有值  老车还有值 合并
                //新车结果集 (商家ID,库存ID ,数量)
                for (Cart newCart : newCartList) {
                    //1:添加新购物车
                    //2:判断新购物车的商家是谁 在当前购物车结果集中是否已经存在了
                    int newIndexOf = oldCartList.indexOf(newCart);// -1 不存在  >=0 存在 (角标)
                    if (newIndexOf != -1) {
                        //-存在  从老购物车结果集中找出那个跟新购物车是同一个商家的老购物车
                        Cart oldCart = oldCartList.get(newIndexOf);
                        // 判断新购物车中 新商品 在老购物车中商品结果集是否存在
                        List<OrderItem> oldOrderItemList = oldCart.getOrderItemList();

                        //新购物车中 有商品集合
                        List<OrderItem> newOrderItemList = newCart.getOrderItemList();
                        for (OrderItem newOrderItem : newOrderItemList) {
                            int indexOf = oldOrderItemList.indexOf(newOrderItem);
                            if (indexOf != -1) {
                                //--存在  老商品结果集中哪个商品是与新商品一样 追加数量
                                OrderItem oldOrderItem = oldOrderItemList.get(indexOf);
                                oldOrderItem.setNum(oldOrderItem.getNum() + newOrderItem.getNum());
                            } else {
                                //--不存在 当新商品添加
                                oldOrderItemList.add(newOrderItem);
                            }
                        }
                    } else {
                        //-不存在  添加新购物车
                        oldCartList.add(newCart);
                    }
                }
            }else{
                //新车
                return newCartList;
            }
        }
        return oldCartList;
    }
}
