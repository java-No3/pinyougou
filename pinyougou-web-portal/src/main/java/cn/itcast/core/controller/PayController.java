package cn.itcast.core.controller;
import cn.itcast.core.service.PayService;
import cn.itcast.core.service.SeckillOrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 支付管理
 */
@RestController
@RequestMapping("/pay")
public class PayController {

    @Reference
    private PayService payService;

    @Reference
    private SeckillOrderService seckillOrderService;


    //获取生成二维码Value 地址
    @RequestMapping("/createNative")
    public Map<String, String> createNative() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return payService.createNative1(name);
    }


    @SuppressWarnings("ALL")
    @RequestMapping("/queryPayStatus")
    public Result queryPayStatus(String out_trade_no) {
       //获取当前用户
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        Result result = null;
        Map map = payService.queryPayStatusWhile(out_trade_no);
        int x = 0;
        while (true) {
            try {
                Thread.sleep(3000);//间隔三秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //不让循环无休止地运行定义变量，如果超过了这个值则退出循环，设置时间为 1 分钟
            x++;
            if (x > 20) {
                result = new Result(false, "二维码超时");
                //1.调用微信的关闭订单接口（学员实现）
                Map<String, String> payresult = payService.closePay(out_trade_no);
                if (!"SUCCESS".equals(payresult.get("result_code"))) {
                    if ("ORDERPAID".equals(payresult.get("err_code"))) {
                        seckillOrderService.saveOrderFromRedis(userId, Long.valueOf(out_trade_no), (String) map.get("transaction_id"));
                        result = new Result(true, "支付成功");
                    }
                }
                if (result.isFlag() == false) {
                    System.out.println("超时，取消订单");
                    result = new Result(false,"支付超时，取消订单");
                    //2.调用删除
                    seckillOrderService.deleteOrderFromRedis(userId, Long.valueOf(out_trade_no));
                }
                break;
            }
        }
        return result;

    }
}