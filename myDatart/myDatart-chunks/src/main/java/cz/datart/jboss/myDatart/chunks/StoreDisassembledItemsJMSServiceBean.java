package cz.datart.jboss.myDatart.chunks;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.switchyard.component.bean.Service;

import cz.datart.jboss.myDatart.utils.BodyTransformer;

@Service(value = StoreDisassembledItemsJMSService.class, name = "StoreDisassembledItemsJMSService")
public class StoreDisassembledItemsJMSServiceBean implements StoreDisassembledItemsJMSService {

    @Inject
    private Logger log;


	@Resource(mappedName = "java:/JmsXA")
    private ConnectionFactory cf;
	
	
	@Override
	public synchronized void storeItem(final QueueItem item) {
		
		if(item == null){
			log.warn("The queue and message was not set");
			return;
		}
		BodyTransformer body = new BodyTransformer();
		if(body.isEmptyBody(item.getTextMessage())){
			log.warn(String.format("Inserting message is empty for queue %s", item.getQueueName()));
			return;
		}
		
		if(body.isEmptyBody(item.getQueueName())){
			log.warn(String.format("Queue name is empty. Insertint text is %s", item.getTextMessage()));
			return;
		}
		
//		log.info(String.format("Store disassembled items into %s", queueName));
    	
    	Queue queue = null;
        try {
       
            queue = (Queue) new InitialContext().lookup("jms/queue/"+item.getQueueName());
        } catch (NamingException ex) {
            log.error(ex.getMessage());
        }
        
    	if(queue == null){
    		log.info(String.format("The queue %s was not found in JNDI", item.getQueueName()));
    		return; // no queue no messages
    	}
    	
    	Connection connection = null;
    	
		try {         

            connection = cf.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = null;

            producer = session.createProducer(queue);

            TextMessage txtMessage = session.createTextMessage();
            txtMessage.setText(item.getTextMessage());
            
            connection.start();

            producer.send(txtMessage);


        }
        catch (Exception exc) {
            log.error(String.format("Putting into the queue %s failed", item.getQueueName()), exc);
        }
        finally {         


            if (connection != null)   {
                try {
                    connection.close();
                } catch (JMSException e) {                    
                	log.error(String.format("Closing the connection to the queue %s failed", item.getQueueName()), e);
                }

            }
        }

	}
	
	

}
