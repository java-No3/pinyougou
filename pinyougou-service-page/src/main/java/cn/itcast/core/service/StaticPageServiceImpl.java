package cn.itcast.core.service;

import cn.itcast.core.dao.good.GoodsDao;
import cn.itcast.core.dao.good.GoodsDescDao;
import cn.itcast.core.dao.item.ItemCatDao;
import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.good.GoodsDesc;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.item.ItemQuery;
import com.alibaba.dubbo.config.annotation.Service;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.opensaml.ws.wspolicy.All;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.servlet.ServletContext;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 静态化处理实现类
 */
@Service
public class StaticPageServiceImpl implements StaticPageService,ServletContextAware {

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Autowired
    private GoodsDescDao goodsDescDao;
    @Autowired
    private ItemDao itemDao;
    @Autowired
    private ItemCatDao itemCatDao;
    @Autowired
    private GoodsDao goodsDao;

    //静态化处理的方法
    public void index(Long id) {
        //输出路径 (相对路径)
        String allPath = getPath("/"+id+".html");

        //创建Freemarker
        Configuration conf = freeMarkerConfigurer.getConfiguration();

        //数据
        Map<String, Object> root = new HashMap<>();
        //商品详情表
        GoodsDesc goodsDesc = goodsDescDao.selectByPrimaryKey(id);
        root.put("goodsDesc",goodsDesc);

        //库存结果集
        ItemQuery itemQuery = new ItemQuery();
        itemQuery.createCriteria().andGoodsIdEqualTo(id).andStatusEqualTo("1");
        List<Item> itemList = itemDao.selectByExample(itemQuery);
        root.put("itemList",itemList);

        //商品对象
        Goods goods = goodsDao.selectByPrimaryKey(id);
        root.put("goods",goods);
        //一级分类
        root.put("itemCat1",itemCatDao.selectByPrimaryKey(goods.getCategory1Id()).getName());
        root.put("itemCat2",itemCatDao.selectByPrimaryKey(goods.getCategory2Id()).getName());
        root.put("itemCat3",itemCatDao.selectByPrimaryKey(goods.getCategory3Id()).getName());

        //输出
        Writer out = null;
        try {
            //读取模板
            Template template = conf.getTemplate("item.ftl");
            //输出
            out = new OutputStreamWriter(new FileOutputStream(allPath) , "UTF-8");
            //处理
            template.process(root, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != out) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
//删除静态页
    @Override
    public void delete(Long id) {
        //输出路径 (相对路径)
        String allPath = getPath("/"+id+".html");
        //读入
        try {
            File file = new File(allPath);
            if (file.isFile()){
                file.delete();
            }
        } catch ( Exception e) {
            e.printStackTrace();
        }

    }

    //获取全路径
    public String getPath(String path){
        return servletContext.getRealPath(path);
    }

    private ServletContext servletContext;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
