package cz.datart.jboss.myDatart.eshop.notification;

import java.util.concurrent.LinkedBlockingQueue;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.InitialContext;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.switchyard.Exchange;
import org.switchyard.component.test.mixins.cdi.CDIMixIn;
import org.switchyard.component.test.mixins.hornetq.HornetQMixIn;
import org.switchyard.test.MockHandler;
import org.switchyard.test.SwitchYardRunner;
import org.switchyard.test.SwitchYardTestCaseConfig;
import org.switchyard.test.SwitchYardTestKit;

@RunWith(SwitchYardRunner.class)
@SwitchYardTestCaseConfig(config = SwitchYardTestCaseConfig.SWITCHYARD_XML, mixins = {
		CDIMixIn.class, HornetQMixIn.class })
public class OrderQueueConsumerServiceTest {

	private static final String MY_TEST_SEGMENT = "TEST"; //NAZEV FRONTY JE V KONFIGURACI TESTOVACICH FRONT
	private static final String ORDER_QUEUE_NAME = "EshopOrderUpdateQueue-"+MY_TEST_SEGMENT;
	private static final String USER_QUEUE_NAME = "EshopUserUpdateQueue-"+MY_TEST_SEGMENT;
	private static final String MANUAL_QUEUE_NAME = "EshopManualUpdateQueue-"+MY_TEST_SEGMENT;
	private static final String OFFER_QUEUE_NAME = "EshopOfferUpdateQueue-"+MY_TEST_SEGMENT;
	private static final String COMPLAINT_QUEUE_NAME = "EshopComplaintUpdateQueue-"+MY_TEST_SEGMENT;
	
	private SwitchYardTestKit testKit;

	
//	private HornetQMixIn _hqMixIn;
	
	final String notifyOrder_payload = "<myd:notifyOrder xmlns:myd=\"http://myDatart.doxologic.com/\"><orderId>1317450</orderId><clientId>ESH-106379</clientId></myd:notifyOrder>";
	final String notifyUser_payload = "<myd:notifyUser xmlns:myd=\"http://myDatart.doxologic.com/\"><scope><segment>cz</segment><oldVersion>1</oldVersion><newVersion>2</newVersion></scope><clientId>ESH-106379</clientId><deleted>false</deleted><name>martin.hubenak@datart.cz</name><businessRelationship>B2C</businessRelationship></myd:notifyUser>";
	final String notifyManual_payload = "<myd:notifyManual xmlns:myd=\"http://myDatart.doxologic.com/\"></myd:notifyManual>";
	final String notifyOffer_payload = "<myd:notifyOffer xmlns:myd=\"http://myDatart.doxologic.com/\"></myd:notifyOffer>";
	final String notifyComplaint_payload = "<myd:updateComplaint xmlns:myd=\"http://myDatart.doxologic.com/\"></myd:updateComplaint>";
	
	
//	@BeforeDeploy
//    public void setProperties() {
		
//		System.setProperty("cz.datart.jboss.myDatart.segment", MY_TEST_SEGMENT);
		
//        System.setProperty("org.switchyard.component.soap.standalone.port", "8081");
//        System.setProperty("axa.ws.endpoint", "http://localhost:8082/axaptaws");
//        System.setProperty("eshop.update.endpoint", "http://localhost:28080/eShopUpdate");
        
//    }
	
//	@Before
//    public void getHornetQMixIn() {
//        _hqMixIn = testKit.getMixIn(HornetQMixIn.class);
//    }
	
//	@Test
//    public void testNotifyOrderConsumerService() throws Exception {
//        Session session = _hqMixIn.getJMSSession();
//        
//
//        String payload = "<myd:notifyOrder xmlns:myd=\"http://myDatart.doxologic.com/\"><orderId>1317450</orderId><clientId>ESH-106379</clientId></myd:notifyOrder>";
//		Message message = _hqMixIn.createJMSMessage(payload);
//		
//        MessageConsumer consumer = session.createConsumer(HornetQMixIn.getJMSQueue(QUEUE_NAME));
//        
//        
//        MessageProducer producer = session.createProducer(HornetQMixIn.getJMSQueue(QUEUE_NAME));
//        
//        
//        //send message to queue
//        producer.send(message);
//       
//
//        //received message from queue
//        message = consumer.receive(3000);
////        message = consumer.receiveNoWait(); //necekam protoze z fronty se nic nevraci, po prichodu zpravy se provede jen inOnly akce
//        
////        producer.close();
////        consumer.close();
////        session.close();
//        
//        
//        //neocekavam zadnou odoved, inOnly akce
////        Assert.assertNotNull("Reply from " + QUEUE_NAME + " is not null.", message);
//        
//        String reply = _hqMixIn.readStringFromJMSMessage(message);
////        SwitchYardTestKit.compareXMLToString(reply, payload);
//        
//        Assert.assertNotNull("Reply from " + QUEUE_NAME + " is not null?", reply);
//        Assert.assertEquals(payload, reply);
//        
//        
//    }

    
    
	@Test
    public void sendTextMessageToOrderJMSQueue() throws Exception {
        
        // replace existing implementation for testing purposes
        final MockHandler orderQueueService = mockInOnlyReference("PrepareUpdateEntityService");

        sendTextToQueue(notifyOrder_payload, ORDER_QUEUE_NAME);
        // Allow for the JMS Message to be processed.
        final LinkedBlockingQueue<Exchange> recievedMessages = receiveMessageFromQueue(orderQueueService);
        
        testReceivedMessageFromQueue(recievedMessages, notifyOrder_payload);
    }
	
	@Test
    public void sendTextMessageToUserJMSQueue() throws Exception {
        
        // replace existing implementation for testing purposes
        final MockHandler orderQueueService = mockInOnlyReference("PrepareUpdateEntityService");

        sendTextToQueue(notifyUser_payload, USER_QUEUE_NAME);
        // Allow for the JMS Message to be processed.
        final LinkedBlockingQueue<Exchange> recievedMessages = receiveMessageFromQueue(orderQueueService);
        
        testReceivedMessageFromQueue(recievedMessages, notifyUser_payload);
    }
	
	@Test
    public void sendTextMessageToManualJMSQueue() throws Exception {
        
        // replace existing implementation for testing purposes
        final MockHandler orderQueueService = mockInOnlyReference("PrepareUpdateEntityService");

        sendTextToQueue(notifyManual_payload, MANUAL_QUEUE_NAME);
        // Allow for the JMS Message to be processed.
        final LinkedBlockingQueue<Exchange> recievedMessages = receiveMessageFromQueue(orderQueueService);
        
        testReceivedMessageFromQueue(recievedMessages, notifyManual_payload);
    }
	
	@Test
    public void sendTextMessageToOfferJMSQueue() throws Exception {
        
        // replace existing implementation for testing purposes
        final MockHandler orderQueueService = mockInOnlyReference("PrepareUpdateEntityService");

        sendTextToQueue(notifyOffer_payload, OFFER_QUEUE_NAME);
        // Allow for the JMS Message to be processed.
        final LinkedBlockingQueue<Exchange> recievedMessages = receiveMessageFromQueue(orderQueueService);
        
        testReceivedMessageFromQueue(recievedMessages, notifyOffer_payload);
    }
	
	@Test
    public void sendTextMessageToComplaintJMSQueue() throws Exception {
        
        // replace existing implementation for testing purposes
        final MockHandler orderQueueService = mockInOnlyReference("PrepareUpdateEntityService");

        sendTextToQueue(notifyComplaint_payload, COMPLAINT_QUEUE_NAME);
        // Allow for the JMS Message to be processed.
        final LinkedBlockingQueue<Exchange> recievedMessages = receiveMessageFromQueue(orderQueueService);
        
        testReceivedMessageFromQueue(recievedMessages, notifyComplaint_payload);
    }

	private void testReceivedMessageFromQueue(final LinkedBlockingQueue<Exchange> recievedMessages, String expectedMessage) {
		Assert.assertNotNull(recievedMessages);
	//        assertEq(recievedMessages, is(notNullValue()));
		final Exchange recievedExchange = recievedMessages.iterator().next();
	//        assertThat(recievedExchange.getMessage().getContent(String.class), is(equalTo(payload)));
		Assert.assertEquals(expectedMessage, recievedExchange.getMessage().getContent(String.class));
	}
	
	private LinkedBlockingQueue<Exchange> receiveMessageFromQueue(final MockHandler orderQueueService)
			throws InterruptedException {
		Thread.sleep(3000);
	
		final LinkedBlockingQueue<Exchange> recievedMessages = orderQueueService.getMessages();
		return recievedMessages;
	}
	
	private MockHandler mockInOnlyReference(String referenceName) {
		testKit.removeService(referenceName);
		final MockHandler orderQueueService = testKit.registerInOnlyService(referenceName);
		return orderQueueService;
	}

    private void sendTextToQueue(final String text, final String queueName) throws Exception {
        InitialContext initialContext = null;
        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;
        try {
            initialContext = new InitialContext();
            final Queue testQueue = (Queue) initialContext.lookup(queueName);
            final ConnectionFactory connectionFactory = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            producer = session.createProducer(testQueue);
            producer.send(session.createTextMessage(text));
        } finally {
            if (producer != null) {
                producer.close();
            }
            if (session != null) {
                session.close();
            }
            if (connection != null) {
                connection.close();
            }
            if (initialContext != null) {
                initialContext.close();
            }
        }
    }

}
