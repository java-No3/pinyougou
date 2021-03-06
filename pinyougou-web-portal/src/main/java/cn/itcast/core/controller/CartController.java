package cn.itcast.core.controller;

import cn.itcast.core.pojo.Cart;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.service.CartService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车管理
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    @Reference
    private CartService cartService;

    //加入购物车
    @RequestMapping("/addGoodsToCartList")
    /*@CrossOrigin(origins = {"http://localhost:9003"},allowCredentials = "true")*/
    @CrossOrigin(origins = {"http://localhost:9003"})
    public Result addGoodsToCartList(Long itemId, Integer num, HttpServletRequest request, HttpServletResponse response) {

        try {
            List<Cart> cartList = null;

            //Cookie中是否有购物车
            boolean k = false;

            /*1:获取Cookie 数组*/
            Cookie[] cookies = request.getCookies();
            if (null != cookies && cookies.length > 0) {
                for (Cookie cookie : cookies) {
                    /*2:获取Cookie中的购物车*/
                    if ("CART".equals(cookie.getName())) {
                        cartList = JSON.parseArray(cookie.getValue(), Cart.class);
                        k = true;
                        break;
                    }

                }
            }
            /*3:没有 创建购物车对象*/
            if (null == cartList) {
                cartList = new ArrayList<>();
            }
            /*4:追加当前款*/
            //Long itemId 库存ID, Integer num 数量 商家ID
            Cart newCart = new Cart();
            //1:通过库存ID查询库存对象
            Item item = cartService.findItemById(itemId);
            //商家ID
            newCart.setSellerId(item.getSellerId());
            //商家名称 (不能设置)

            //库存ID
            OrderItem newOrderItem = new OrderItem();
            newOrderItem.setItemId(itemId);
            //数量
            newOrderItem.setNum(num);

            //商品结果集
            List<OrderItem> newOrderItemList = new ArrayList<>();
            newOrderItemList.add(newOrderItem);

            newCart.setOrderItemList(newOrderItemList);

            //1:添加新购物车
            //2:判断新购物车的商家是谁 在当前购物车结果集中是否已经存在了
            int newIndexOf = cartList.indexOf(newCart);// -1 不存在  >=0 存在 (角标)
            if (newIndexOf != -1) {
                //-存在  从老购物车结果集中找出那个跟新购物车是同一个商家的老购物车
                Cart oldCart = cartList.get(newIndexOf);
                // 判断新购物车中 新商品 在老购物车中商品结果集是否存在
                List<OrderItem> oldOrderItemList = oldCart.getOrderItemList();
                int indexOf = oldOrderItemList.indexOf(newOrderItem);
                if (indexOf != -1) {
                    //--存在  老商品结果集中哪个商品是与新商品一样 追加数量
                    OrderItem oldOrderItem = oldOrderItemList.get(indexOf);
                    oldOrderItem.setNum(oldOrderItem.getNum() + newOrderItem.getNum());
                } else {
                    //--不存在 当新商品添加
                    oldOrderItemList.add(newOrderItem);
                }

            } else {
                //-不存在  添加新购物车
                cartList.add(newCart);

            }

            //判断是否登陆
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            //名称:是真名  匿名
            if (!"anonymousUser".equals(name)) {
                //已登陆

                //5:将当前购物车合并到原来的购物车中
                cartService.merge(cartList,name);
                //6:清空Cookie
                if(k){
                    Cookie cookie = new Cookie("CART",null);
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
            } else {
                /*未登陆  请求*/
                /* 5:将当前购物车 保存到Cookie
                 *   JSON.parseArray 将Json串转对象
                 *   将对象转串
                 *   request  http://localhost:8080/cart/addGoodsToCart.do
                 *   request  http://localhost:8080/shopping/addGoodsToCart.do
                 * */
                Cookie cookie = new Cookie("CART", JSON.toJSONString(cartList));
                cookie.setMaxAge(60 * 60 * 24 * 5);
                // http://localhost:8080/cart/addGoodsToCart.do
                cookie.setPath("/");
                /* 6:回写到浏览器中*/
                response.addCookie(cookie);
            }
            return new Result(true, "加入购物车成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "加入购物车失败");
        }
    }

    //跳转购物车页面之后 查询购物车结果集
    @RequestMapping("/findCartList")
    public List<Cart> findCartList(HttpServletRequest request,HttpServletResponse response) {

        List<Cart> cartList = null;
        //1:获取Cookie数组
        Cookie[] cookies = request.getCookies();
        if (null != cookies && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                //2:获取Cookie中购物车结果集
                if ("CART".equals(cookie.getName())) {
                    cartList = JSON.parseArray(cookie.getValue(), Cart.class);
                    break;
                }

            }
        }
        //判断是否登陆
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        //名称:是真名  匿名
        if (!"anonymousUser".equals(name)) {
            //已登陆
            //3:有 将购物车合并到帐户中原购物车 清空Cookie
            if(null != cartList){
                cartService.merge(cartList,name);
                Cookie cookie = new Cookie("CART",null);
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
            //4:将帐户中购物车取出来
            cartList = cartService.findCartListFromRedis(name);

        }
        //5:有 将购物车结果集装满
        if(null != cartList){
            cartList = cartService.findCartList(cartList);
        }
        // 6:回显
        return cartList;
    }
}
