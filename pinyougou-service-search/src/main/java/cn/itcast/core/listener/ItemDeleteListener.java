package cn.itcast.core.listener;

import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.item.ItemQuery;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SolrDataQuery;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.List;

/**
 * 消息 自定义处理类
 */
public class ItemDeleteListener implements MessageListener {

    @Autowired
    private SolrTemplate solrTemplate;
    @Override
    public void onMessage(Message message) {
        ActiveMQTextMessage atm = (ActiveMQTextMessage) message;
        try {
            String id = atm.getText();
            System.out.println("为了删除,搜索项目接收到的ID:" + id);
            //2:删除索引库中索引
            Criteria criteria = new Criteria("item_goodsid").is(id);
            SolrDataQuery query = new SimpleQuery(criteria);
            solrTemplate.delete(query);
            solrTemplate.commit();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
