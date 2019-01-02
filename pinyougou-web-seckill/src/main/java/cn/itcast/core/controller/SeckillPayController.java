package cn.itcast.core.controller;
import cn.itcast.core.pojo.seckill.SeckillOrder;
import cn.itcast.core.service.PayService;
import cn.itcast.core.service.SeckillOrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;


/**
 * 秒杀商品支付  LH
 */
@RestController
@RequestMapping("pay")
public class SeckillPayController {

    @Reference
    private SeckillOrderService seckillOrderService;

    @Reference
    private PayService payService;


    @RequestMapping("createNative")
    public Map<String, String> createNative(){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        SeckillOrder seckillOrder = seckillOrderService.searchOrderFromRedis(userId);
        if (seckillOrder!=null){
            long fen = (long)(seckillOrder.getMoney().doubleValue()*100);
            return payService.createNative(seckillOrder.getId()+"",fen+"");
        }else {
            return new HashMap();
        }
    }

    @RequestMapping("queryPayStatus")
    public Result queryPayStatus(String out_trade_no) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        Map map = payService.queryPayStatusWhile(out_trade_no);
        if (map == null) {
            return new Result(false, "系统正在维护中");
        } else {
            if ("SUCCESS".equals(map.get("trade_state"))) {
                try {
                    seckillOrderService.saveOrderFromRedis(userId, Long.valueOf(out_trade_no), (String) map.get("transaction_id"));
                    return new Result(true, "支付成功");
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return new Result(false, e.getMessage());
                }catch (Exception e){
                    e.printStackTrace();
                    return new Result(false, "支付失败");
                }
            }else {
                seckillOrderService.deleteOrderFromRedis(userId,Long.valueOf(out_trade_no));
                return new Result(false, "二维码超时");
            }
        }
    }
}