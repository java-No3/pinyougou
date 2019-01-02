package cn.itcast.core.service;
import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.dao.seckill.SeckillGoodsDao;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.good.GoodsQuery;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.item.ItemQuery;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.pojo.seckill.SeckillGoodsQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;


/**
 * 秒杀商品申请  LH
 */
@Service
public class SeckillGoodsSellerServiceImpl implements SeckillGoodsSellerService {

    @Autowired
    private ItemDao itemDao;

    @Autowired
    private SeckillGoodsDao seckillGoodsDao;
    @Override
    public void add(Goods goods) {
        SeckillGoods seckillGoods = new SeckillGoods();

        // 1.保存秒杀商品,设置状态,返回自增主键
        seckillGoods.setStatus("0");//设置审核状态

        Long goodsId = goods.getId();
        seckillGoods.setGoodsId(goodsId);//设置秒杀商品id
        seckillGoods.setSmallPic(goods.getSmallPic());//商品图片
        seckillGoods.setPrice(goods.getPrice());//商品原价
        seckillGoods.setSellerId(goods.getSellerId());//商家ID
        seckillGoods.setCreateTime(new Date());//创建时间

        ItemQuery query=new ItemQuery();
        ItemQuery.Criteria criteria = query.createCriteria();
        criteria.andGoodsIdEqualTo(goodsId);
        List<Item> itemList = itemDao.selectByExample(query);
        Item item = itemList.get(0);
        seckillGoods.setTitle(item.getTitle());// 秒杀商品标题
        seckillGoods.setCostPrice(item.getCostPirce());//秒杀价格
        seckillGoods.setNum(item.getNum());//秒杀商品数
        seckillGoods.setStockCount(item.getStockCount());//剩余

        seckillGoodsDao.insertSelective(seckillGoods);
    }

    @Override
    public PageResult search(Integer page, Integer rows, SeckillGoods seckillGoods) {
        //分页插件
        PageHelper.startPage(page, rows);
        //排序
        PageHelper.orderBy("id desc");

        SeckillGoodsQuery seckillGoodsQuery = new SeckillGoodsQuery();
         seckillGoodsQuery.createCriteria().andStatusEqualTo("0");
        Page<SeckillGoods> p = (Page<SeckillGoods>) seckillGoodsDao.selectByExample(seckillGoodsQuery);
        return new PageResult(p.getTotal(), p.getResult());
    }

    @Override
    public void updateStatus(Long[] ids, String status) {

    }
}
