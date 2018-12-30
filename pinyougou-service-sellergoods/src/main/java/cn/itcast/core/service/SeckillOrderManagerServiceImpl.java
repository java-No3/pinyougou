package cn.itcast.core.service;
import cn.itcast.core.dao.seckill.SeckillOrderDao;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.seckill.SeckillOrder;
import cn.itcast.core.pojo.seckill.SeckillOrderQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;


/**
 * 秒杀商品订单运营商查询
 */
@Service
public class SeckillOrderManagerServiceImpl implements SeckillOrderManagerService {

    @Autowired
    private SeckillOrderDao seckillOrderDao;

    /**
     * 根据商家ID查询秒杀商品订单
     * @param page
     * @param rows
     * @param seckillOrder
     * @return
     */
    @Override
    public PageInfo<SeckillOrder> search(Integer page,Integer rows,SeckillOrder seckillOrder) {
        PageHelper.startPage(page, rows);
        SeckillOrderQuery query = new SeckillOrderQuery();
        SeckillOrderQuery.Criteria criteria = query.createCriteria();
        if (seckillOrder.getSellerId()!=null && !"".equals(seckillOrder.getSellerId().trim())){
            criteria.andSellerIdEqualTo(seckillOrder.getSellerId().trim());
        }
        criteria.andStatusEqualTo("1");
        query.setOrderByClause("create_time desc");
        List<SeckillOrder> seckillOrders = seckillOrderDao.selectByExample(query);
        PageInfo<SeckillOrder> pageInfo = new PageInfo<>(seckillOrders);
        return pageInfo;
    }

    @Override
    public List<SeckillOrder> findAll() {
        return seckillOrderDao.selectByExample(null);
    }

    @Override
    public PageResult findPage(Integer pageNum, Integer pageSize) {
        //分页插件
        PageHelper.startPage(pageNum,pageSize);
        //查询
        Page<SeckillOrder> p = (Page<SeckillOrder>) seckillOrderDao.selectByExample(null);
        return new PageResult(p.getTotal(), p.getResult());
    }

}
