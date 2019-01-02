package cn.itcast.core.service;
import cn.itcast.core.dao.seckill.SeckillOrderDao;
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
 * 商家后台秒杀订单查询    LH
 */
@Service
@SuppressWarnings("ALL")
public class SeckillOrderQTServiceImpl implements SeckillOrderQTService {

    @Autowired
    private SeckillOrderDao seckillOrderDao;
    @Override
    public PageInfo<SeckillOrder> search(Integer page, Integer rows, SeckillOrder seckillOrder) {
        // 设置分页条件
        PageHelper.startPage(page, rows);
        // 设置查询条件
        SeckillOrderQuery query = new SeckillOrderQuery();
        SeckillOrderQuery.Criteria criteria = query.createCriteria();
        if (seckillOrder.getSellerId()!=null && !"".equals(seckillOrder.getSellerId().trim())){
            criteria.andSellerIdEqualTo(seckillOrder.getSellerId().trim());
        }
        criteria.andStatusEqualTo("1");
        query.setOrderByClause("create_time desc");// 降序
        // 查询
        List<SeckillOrder> seckillOrders = seckillOrderDao.selectByExample(query);
        PageInfo<SeckillOrder> pageInfo=new PageInfo<>(seckillOrders);
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
