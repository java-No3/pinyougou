package cn.itcast.core.service.seckill;
import cn.itcast.common.utils.IdWorker;
import cn.itcast.core.dao.seckill.SeckillGoodsDao;
import cn.itcast.core.dao.seckill.SeckillOrderDao;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.pojo.seckill.SeckillOrder;
import cn.itcast.core.pojo.seckill.SeckillOrderQuery;
import cn.itcast.core.service.SeckillOrderService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;


/**
 * 秒杀商品订单相关   LH
 */
@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {


    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SeckillGoodsDao seckillGoodsDao;

    @Autowired
    private SeckillOrderDao seckillOrderDao;


    public void submitOrder(Long seckillId, String userId) {


        //获取商品信息
        SeckillGoods seckillGoods = (SeckillGoods) redisTemplate.boundHashOps("seckillGoods").get(seckillId);
        if (seckillGoods==null || seckillGoods.getNum()<=0){
            throw new RuntimeException("商品已抢购一空");
        }

        //删减库存
        seckillGoods.setStockCount(seckillGoods.getStockCount()+1);
        seckillGoods.setNum(seckillGoods.getNum()-1);
        redisTemplate.boundHashOps("seckillGoods").put(seckillId,seckillGoods);

        if (seckillGoods.getNum()==0){
            seckillGoodsDao.updateByPrimaryKey(seckillGoods);
            redisTemplate.boundHashOps("seckillGoods").delete(seckillId);
        }

        SeckillOrder seckillOrder=new SeckillOrder();
        IdWorker idWorker=new IdWorker();
        seckillOrder.setId(idWorker.nextId());
        seckillOrder.setCreateTime(new Date());
        seckillOrder.setMoney(seckillGoods.getCostPrice());
        seckillOrder.setSeckillId(Long.valueOf(seckillId));
        seckillOrder.setSellerId(seckillGoods.getSellerId());
        seckillOrder.setUserId(userId);
        seckillOrder.setStatus("0");
        redisTemplate.boundHashOps("seckillOrder").put(userId,seckillOrder);

    }

    @Override
    public SeckillOrder searchOrderFromRedis(String userId) {
        return (SeckillOrder) redisTemplate.boundHashOps("seckillOrder").get(userId);
    }

    @Override
    @Transactional
    /**
     * 秒杀订单对缓存的操作   LH
     */
    public void saveOrderFromRedis(String userId, Long orderId, String transactionId) {
        SeckillOrder seckillOrder = searchOrderFromRedis(userId);
        if (seckillOrder==null){
            throw new RuntimeException("订单不存在");
        }
        if (seckillOrder.getId().longValue()!=orderId.longValue()){
            throw new RuntimeException("订单不是你的");
        }
        //设置属性
        seckillOrder.setStatus("1");
        seckillOrder.setPayTime(new Date());
        seckillOrder.setTransactionId(transactionId);
        seckillOrderDao.insert(seckillOrder);

        //清空redis
        redisTemplate.boundHashOps("seckillOrder").delete(userId);
    }

    @Override

    /**
     * 秒杀中缓存的操作  LH
     */
    public void deleteOrderFromRedis(String userId, Long orderId) {
        //取出redis中的订单
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.boundHashOps("seckillOrder").get(userId);
        if (seckillOrder!=null && seckillOrder.getId().longValue()==orderId.longValue()){
            //修改库存
            SeckillGoods seckillGoods = (SeckillGoods) redisTemplate.boundHashOps("seckillGoods").get(seckillOrder.getSeckillId());
            if (seckillGoods!=null){
                seckillGoods.setNum(seckillGoods.getNum()+1);
                seckillGoods.setStockCount(seckillGoods.getStockCount()-1);
                redisTemplate.boundHashOps("seckillGoods").put(seckillOrder.getSeckillId(),seckillGoods);
            }else {
                seckillGoods=seckillGoodsDao.selectByPrimaryKey(seckillOrder.getSeckillId());
                if (seckillGoods!=null) {
                    seckillGoods.setNum(seckillGoods.getNum() + 1);
                    seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
                    seckillGoodsDao.updateByPrimaryKeySelective(seckillGoods);
                }
            }
            //删除订单
            redisTemplate.boundHashOps("seckillOrder").delete(userId);
        }

    }
}