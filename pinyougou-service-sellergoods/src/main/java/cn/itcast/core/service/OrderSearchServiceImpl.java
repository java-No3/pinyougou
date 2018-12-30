package cn.itcast.core.service;
import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * 运营商后台  订单的查询
 */
@Service
@Transactional
public class OrderSearchServiceImpl implements OrderSearchService {
    @Autowired
    private OrderDao orderDao;

    //运营商后台订单的新增  LH
    /**@Override
    public void add(Order order) {
        orderDao.insertSelective(order);
    }*/

    //运营商订单管理模块  查询所有  LH
    @Override
    public List<Order> findAll() {
        return orderDao.selectByExample(null);
    }

    //运营商后台订单管理模块    查询分页  LH
    @Override
    public PageResult findPage(Integer pageNum, Integer pageSize) {
        //分页插件
        PageHelper.startPage(pageNum,pageSize);
        //查询
        Page<Order> p = (Page<Order>) orderDao.selectByExample(null);
        return new PageResult(p.getTotal(), p.getResult());
    }


    //运营商管理后台  搜索  带条件 LH
    @Override
    public PageResult search(Integer pageNum, Integer pageSize, Order order) {
        //分页插件
        PageHelper.startPage(pageNum,pageSize);

        OrderQuery orderQuery = new OrderQuery();

        OrderQuery.Criteria criteria = orderQuery.createCriteria();

        //判断订单ID  " "
        if(null != order.getOrderId() && !"".equals(order.getOrderId())){
            criteria.andOrderIdEqualTo(order.getOrderId());
        }
        //判断用户名
        if(null != order.getUserId() && !"".equals(order.getUserId().trim())){
            criteria.andUserIdLike(order.getUserId().trim());
        }
        //查询
        Page<Order> p = (Page<Order>) orderDao.selectByExample(orderQuery);

        return new PageResult(p.getTotal(), p.getResult());
    }


    //运营商管理后台  更新订单   LH
   /** @Override
    public void update(Order order) {
        orderDao.updateByPrimaryKeySelective(order);
    }*/


    //运营商管理后台   查询详情 一个   LH
    @Override
    public Order findOne(Long orderId) {
        return orderDao.selectByPrimaryKey(orderId);
    }
}
