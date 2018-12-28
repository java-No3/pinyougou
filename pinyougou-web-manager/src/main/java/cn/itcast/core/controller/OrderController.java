package cn.itcast.core.controller;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.service.OrderSearchService;
import com.alibaba.dubbo.config.annotation.Reference;
import entity.PageResult;
import entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
