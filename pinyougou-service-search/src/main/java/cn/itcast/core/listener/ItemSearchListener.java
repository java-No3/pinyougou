package cn.itcast.core.listener;

import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.item.ItemQuery;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.List;

/**
 * 消息 自定义处理类
 */
public class ItemSearchListener implements MessageListener {

    //接收消息 并处理
    @Autowired
    private ItemDao itemDao;
    @Autowired
    private SolrTemplate solrTemplate;

    @Override
    public void onMessage(Message message) {
        ActiveMQTextMessage atm = (ActiveMQTextMessage) message;
        try {
            String id = atm.getText();
            System.out.println("搜索项目接收到的ID:" + id);
            //2:保存商品信息到索引库  库存表
            ItemQuery itemQuery = new ItemQuery();
            itemQuery.createCriteria().andGoodsIdEqualTo(Long.parseLong(id)).andStatusEqualTo("1").andIsDefaultEqualTo("1");
            List<Item> itemList = itemDao.selectByExample(itemQuery);
            solrTemplate.saveBeans(itemList, 1000);

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
