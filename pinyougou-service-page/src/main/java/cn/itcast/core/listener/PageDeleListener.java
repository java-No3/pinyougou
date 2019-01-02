package cn.itcast.core.listener;

import cn.itcast.core.service.StaticPageService;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ServletContextAware;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.servlet.ServletContext;

/**
 * ZQ自定义消息处理类
 */
public class PageDeleListener implements MessageListener  {
    @Autowired
    private StaticPageService staticPageService;

    @Override
    public void onMessage(Message message) {


        ActiveMQTextMessage atm= (ActiveMQTextMessage) message;
        //删除静态页面
        try {
            //获取需要删除的id
            String id = atm.getText();
            System.out.println("静态化项目删除接收到的ID:" + id);
            //删除id
            staticPageService.delete(Long.parseLong(id));
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }


}
