package cz.datart.jboss.myDatart.chunks.queueing;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.InitialContext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.switchyard.component.test.mixins.cdi.CDIMixIn;
import org.switchyard.component.test.mixins.hornetq.HornetQMixIn;
import org.switchyard.test.SwitchYardRunner;
import org.switchyard.test.SwitchYardTestCaseConfig;
import org.switchyard.test.SwitchYardTestKit;

@RunWith(SwitchYardRunner.class)
@SwitchYardTestCaseConfig(config = SwitchYardTestCaseConfig.SWITCHYARD_XML, mixins = {
		CDIMixIn.class, HornetQMixIn.class })
public class ChunkQueueingTest {

	
	private SwitchYardTestKit testKit;

	
	private HornetQMixIn _hqMixIn;
	
	
//	@BeforeDeploy
//    public void setProperties() {
		
//		System.setProperty("cz.datart.jboss.myDatart.segment", MY_TEST_SEGMENT);
		
//        System.setProperty("org.switchyard.component.soap.standalone.port", "8081");
//        System.setProperty("axa.ws.endpoint", "http://localhost:8082/axaptaws");
//        System.setProperty("eshop.update.endpoint", "http://localhost:28080/eShopUpdate");
        
//    }
	
	@Before
    public void getHornetQMixIn() {
        _hqMixIn = testKit.getMixIn(HornetQMixIn.class);
    }
	
	@Test
    public void testChunkUpdateAttributesQueueing() throws Exception {
		
		final String QUEUE_NAME = "myDatart-ChunkUpdateAttributesQueue-testcz";
		
        Session session = _hqMixIn.getJMSSession();
        

        String payload = "Test";
		Message message = _hqMixIn.createJMSMessage(payload);
		
        MessageConsumer consumer = session.createConsumer(HornetQMixIn.getJMSQueue(QUEUE_NAME));
        
        
        MessageProducer producer = session.createProducer(HornetQMixIn.getJMSQueue(QUEUE_NAME));
        
        
        //send message to queue
        producer.send(message);
       

        //received message from queue
        message = consumer.receive(3000);
//        message = consumer.receiveNoWait(); //necekam protoze z fronty se nic nevraci, po prichodu zpravy se provede jen inOnly akce
        
//        producer.close();
//        consumer.close();
//        session.close();
        
        
        //neocekavam zadnou odoved, inOnly akce
//        Assert.assertNotNull("Reply from " + QUEUE_NAME + " is not null.", message);
        
        String reply = _hqMixIn.readStringFromJMSMessage(message);
//        SwitchYardTestKit.compareXMLToString(reply, payload);
        
        Assert.assertNotNull("Reply from " + QUEUE_NAME + " is not null?", reply);
        Assert.assertEquals(payload, reply);
        
        
    }

    
    
//	@Test
//    public void sendTextMessageToOrderJMSQueue() throws Exception {
//        
//		final String QUEUE_NAME = "myDatart-ChunkUpdateAttributesQueue-testcz";
//		
//        // replace existing implementation for testing purposes
//        final MockHandler orderQueueService = mockInOnlyReference("PrepareUpdateEntityService");
//
//        sendTextToQueue(notifyOrder_payload, QUEUE_NAME);
//        // Allow for the JMS Message to be processed.
//        final LinkedBlockingQueue<Exchange> recievedMessages = receiveMessageFromQueue(orderQueueService);
//        
//        testReceivedMessageFromQueue(recievedMessages, notifyOrder_payload);
//    }
	

}
