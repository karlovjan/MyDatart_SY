package cz.datart.jboss.myDatart.chunks.jms;

import org.apache.log4j.Logger;

import cz.datart.jboss.myDatart.chunks.IJMSChunkLoader;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

@Stateless
@Local
@Dependent
public class JMSChunkLoader implements IJMSChunkLoader {
 
//	@Resource(mappedName="jms/ConnectionFactory")
//	private ConnectionFactory cf;
	
	@Resource(mappedName = "java:/JmsXA")
    private ConnectionFactory cf;
	private Connection connection;


//	@Resource(mappedName="jms/queue/" + producerQueue)
//	private static Queue queue;
	
//	@Property(name="chunk.producer.queue")
//	private String producerQueueName;
	
    @Inject
    private Logger log;

//    @Inject
//    @JMSConnectionFactory("java:/JmsXA")
//    @Resource(mappedName = "java:/JmsXA")
    
//    @Inject
//    @JMSConnectionFactory("java:/JmsXA")
//    private JMSContext context;


//    @PostConstruct
//    void initBean(){
//    	
//    }
//
//    @PreDestroy
//    void destroyBean(){
//    	
//    }
        
	
//    @Override
//    public List<String> loadMessages(int count, String thisJobName, String queueName) {
//    	
//    	log.info(String.format("%s: Count of loading Messages from queue %s is %s", thisJobName, queueName, count));
//    	
//    	if(context == null){
//    		log.warn("JMS Context is not injected...");
//    		return null;
//    	}
//    	
//    	Queue queue = null;
//        try {
//       
//            queue = (Queue) new InitialContext().lookup("jms/queue/"+queueName);
//        } catch (NamingException ex) {
//            log.error(ex.getMessage());
//        }
//        
//    	if(queue == null){
//    		log.info(String.format("%s: The queue %s was not found in JNDI", thisJobName, queueName));
//    		return null; // no queue no messages
//    	}
//    	
//        // trim parameters
//        if (count <= 0) {
//        	log.warn("Messab count in chunk is less than 0");
//            return null;
//        }
//
//		List<String> messages = null; 
//
//		JMSConsumer receiver = context.createConsumer(queue);
//       
//        try {
//        	// load messages
//        	messages = receiveMessages(count, thisJobName, queueName, receiver);
//		} catch(Throwable ex){
//			log.error(ex.getMessage(), ex);
//		
//		} 
//		
//
//        return messages;
//    }
    
    @Override
    public List<String> loadMessages(int count, String thisJobName, String queueName) {
    	
    	log.info(String.format("%s: Count of loading Messages from queue %s is %s", thisJobName, queueName, count));
    	
    	Queue queue = null;
        try {
       
            queue = (Queue) new InitialContext().lookup("jms/queue/"+queueName);
        } catch (NamingException ex) {
            log.error(ex.getMessage());
        }
        
    	if(queue == null){
    		log.info(String.format("%s: The queue %s was not found in JNDI", thisJobName, queueName));
    		return null; // no queue no messages
    	}
    	
        // trim parameters
        if (count <= 0) {
        	log.warn("Messab count in chunk is less than 0");
            return null;
        }

		List<String> messages = null; 

		
		try {         

            connection = cf.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageConsumer consumer = null;

            consumer = session.createConsumer(queue);

            connection.start();

            messages = receiveMessages(count, thisJobName, queueName, consumer);


        }
        catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
        finally {         


            if (connection != null)   {
                try {
                    connection.close();
                } catch (JMSException e) {                    
                	log.error(e.getMessage(), e);
                }

            }
        }

        

        return messages;
    }


	protected List<String> receiveMessages(int count, String thisJobName, String queueName, MessageConsumer consumer) {

		List<String> messages = new ArrayList<>(count); 
		
		String item;
		Message receivedMsg;
		
		for (int i = 0 ; messages.size() < count && i < count; i++) {
		    try {
		    	receivedMsg = consumer.receiveNoWait();
				
				
				if (receivedMsg == null){
					log.info(thisJobName+": There is no more messages in " + queueName);
					break;
				}
				
				item = ((TextMessage)receivedMsg).getText();
				
				messages.add(item);
				
			} catch (JMSException e) {
				log.error(e.getMessage(), e);
				
			}

			
			
		}
		
		return messages;
	}


	protected List<String> receiveMessages(int count, String thisJobName, String queueName, JMSConsumer receiver) {
		
		List<String> messages = new ArrayList<>(count); 
		
		String item;
		for (int i = 0 ; messages.size() < count && i < count; i++) {
		    item = receiver.receiveBodyNoWait(String.class);

			if (item == null){
				log.info(thisJobName+": There is no more messages in " + queueName);
				break;
			}
			
			messages.add(item);
			
		}
		
		return messages;
	}

	
}
