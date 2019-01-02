package cn.itcast.core.controller;
import cn.itcast.common.utils.ExportExcelUtils;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.seller.Seller;
import cn.itcast.core.service.OrderSearchService;
import com.alibaba.dubbo.config.annotation.Reference;
import entity.PageResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * 运营商后台订单管理  LH
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderSearchService orderSearchService;


    //查询所有订单结果集   LH
    @RequestMapping("/findAll")
    public List<Order> findAll(){
        return orderSearchService.findAll();
    }
    //查询订单分页对象   LH
    @RequestMapping("/findPage")
    public PageResult findPage(Integer pageNum,Integer pageSize){

        return orderSearchService.findPage(pageNum,pageSize);

    }
    //查询订单分页对象  带条件   LH
    @RequestMapping("/search")
    public PageResult search(Integer pageNum, Integer pageSize, @RequestBody Order order){
        return orderSearchService.search(pageNum,pageSize,order);

    }
    //添加订单   LH
    /**
    @RequestMapping("/add")
    public Result add(@RequestBody Order order){
        try {
            orderSearchService.add(order);
            return new Result(true,"保存成功");
        } catch (Exception e) {
            //e.printStackTrace();
            return new Result(false,"保存失败");
        }
    }
    //修改订单   LH
    @RequestMapping("/update")
    public Result update(@RequestBody Order order){

        try {
            orderSearchService.update(order);
            return new Result(true,"修改成功");
        } catch (Exception e) {
            //e.printStackTrace();
            return new Result(false,"修改失败");
        }

    }*/
    //查询一个订单   LH
    @RequestMapping("/findOne")
    public Order findOne(Long orderId){
        return orderSearchService.findOne(orderId);
    }


    // 导出订单信息到excel szj
    @RequestMapping("/exportOrdersInfoExcel")
    public void exportOrdersInfoExcel (HttpServletRequest request, HttpServletResponse response){
        // 获取需要导出的数据
        List<Order> orderList = orderSearchService.findAll();
        //定义导出excel的名字
        String excelName="卖家统计表";
        // 获取需要转出的excle表头的map字段
        LinkedHashMap<String, String> fieldMap =new LinkedHashMap<String, String>() ;
        fieldMap.put("orderId", "订单id");
        fieldMap.put("payment", "实付金额");
        fieldMap.put("paymentType", "支付类型");
        fieldMap.put("status", "状态");
        fieldMap.put("createTime", "订单创建时间");
        fieldMap.put("updateTime", "订单更新时间");
        fieldMap.put("paymentTime", "付款时间");
        fieldMap.put("consignTime", "发货时间");
        fieldMap.put("endTime", "交易完成时间");
        fieldMap.put("closeTime", "交易关闭时间");
        fieldMap.put("shippingName", "物流名称");
        fieldMap.put("shippingCode", "物流单号");
        fieldMap.put("userId", "用户id");
        fieldMap.put("buyerMessage", "买家留言");
        fieldMap.put("buyerNick", "买家昵称");
        fieldMap.put("buyerRate", "买家是否已经评价");
        fieldMap.put("receiverAreaName", "收货人地区名称");
        fieldMap.put("receiverZipCode", "收货人邮编");
        fieldMap.put("receiver", "收货人");
        fieldMap.put("expire", "过期时间");
        fieldMap.put("invoiceType", "发票类型");
        fieldMap.put("sourceType", "订单来源");
        fieldMap.put("sellerId", "商家ID");
        //导出用户相关信息
        new ExportExcelUtils().export(excelName, orderList, fieldMap, response);
    }

}
