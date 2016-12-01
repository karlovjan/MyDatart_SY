package cz.datart.jboss.myDatart.chunks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.switchyard.Exchange;
import org.switchyard.ExchangeHandler;
import org.switchyard.HandlerException;
import org.switchyard.component.test.mixins.cdi.CDIMixIn;
import org.switchyard.component.test.mixins.hornetq.HornetQMixIn;
import org.switchyard.component.test.mixins.http.HTTPMixIn;
import org.switchyard.component.test.mixins.transaction.TransactionMixIn;
import org.switchyard.test.BeforeDeploy;
import org.switchyard.test.Invoker;
import org.switchyard.test.MockHandler;
import org.switchyard.test.ServiceOperation;
import org.switchyard.test.SwitchYardRunner;
import org.switchyard.test.SwitchYardTestCaseConfig;
import org.switchyard.test.SwitchYardTestKit;

@RunWith(SwitchYardRunner.class)
@SwitchYardTestCaseConfig(
		config = SwitchYardTestCaseConfig.SWITCHYARD_XML, 
		mixins = { CDIMixIn.class, HTTPMixIn.class, HornetQMixIn.class, TransactionMixIn.class })
public class EshopChunkUpdateServiceTest {

	private static final String CHUNK_WEB_SERVICE = "http://localhost:8081/test1/chunks/eshop/update/EshopChunkUpdateService";
	
	
//	private static final String MY_TEST_SEGMENT = "CZ"; //NAZEV FRONTY JE V KONFIGURACI TESTOVACICH FRONT
	
	private static final String PRODUCER_QUEUE_NAME = "ChunkUpdateProducerQueue";
//	private static final String Relations_QUEUE_NAME = "ChunkUpdateRelationsQueue-"+MY_TEST_SEGMENT;
//	private static final String Catalogue_QUEUE_NAME = "ChunkUpdateCatalogueQueue-"+MY_TEST_SEGMENT;
//	private static final String AttributeGroups_QUEUE_NAME = "ChunkUpdateAttributeGroupsQueue-"+MY_TEST_SEGMENT;
//	private static final String Attributes_QUEUE_NAME = "ChunkUpdateAttributesQueue-"+MY_TEST_SEGMENT;
//	private static final String Prices_QUEUE_NAME = "ChunkUpdatePricesQueue-"+MY_TEST_SEGMENT;
//	private static final String Availability_QUEUE_NAME = "ChunkUpdateAvailabilityQueue-"+MY_TEST_SEGMENT;
//	private static final String Categories_QUEUE_NAME = "ChunkUpdateCategoriesQueue-"+MY_TEST_SEGMENT;
	
	private HTTPMixIn httpMixIn;
	private HornetQMixIn _hqMixIn;
	private SwitchYardTestKit testKit;
	
	
	@ServiceOperation("EshopChunkUpdateService")
	private Invoker chunkUpdateReceiver;
	
	@BeforeDeploy
    public void setProperties() {
		
        System.setProperty("org.switchyard.component.soap.standalone.port", "8081");
//        System.setProperty("axa.ws.endpoint", "http://localhost:8082/axaptaws");
//        System.setProperty("eshop.update.endpoint", "http://localhost:28080/eShopUpdate");
        
//        _hqMixIn = testKit.getMixIn(HornetQMixIn.class);
        
    }
	
	private MockHandler mockInOnlyReference(String referenceName) {
		testKit.removeService(referenceName);
		final MockHandler orderQueueService = testKit.registerInOnlyService(referenceName);
		return orderQueueService;
	}
	
	public String readFile(InputStream input) throws IOException {
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input))) {
            return buffer.lines().collect(Collectors.joining("\n"));
        }
    }
	
	@Test
    public void testProducerUpdateRequestRecived() throws Exception {
       
		mockInOnlyReference("DissassembleProducersService");

		httpMixIn.postResourceAndTestXML(CHUNK_WEB_SERVICE, "/soaps/update-producers-request.xml", "/soaps/update-producers-response.xml");
    
//		int status = _httpMixIn.postResourceAndGetStatus(SWITCHYARD_WEB_SERVICE, "/xml/soap-request-500.xml");
//     
		
	}
	
//	@Test
	public void test_disassambly_updateProducersRequest() throws Exception {
		
		// initialize your test message
		String updateRequest = readFile(testKit.getResourceAsStream("/soaps/update-producers-request.xml"));
		String updateResponse = readFile(testKit.getResourceAsStream("/soaps/update-producers-response.xml"));
		
		// register the service...
		
		testKit.replaceService("StoreDissassembledCategoriesRef", new ExchangeHandler() {
			
			@Override
			public void handleMessage(Exchange exchange) throws HandlerException {
				String chunks = exchange.getMessage().getContent(String.class);
				String[] chunksArray = chunks.split("</eProducer>");
				Assert.assertEquals(2, chunksArray.length);
			}
			
			@Override
			public void handleFault(Exchange exchange) {
				String msg = exchange.getMessage().getContent(String.class);
				System.err.println(msg);
				Assert.assertTrue("Error - " + msg, false);
			}
			
		});
		       
		
		
		String response = chunkUpdateReceiver.operation("").sendInOut(updateRequest).getContent(String.class);

		// validate the results
		Assert.assertEquals(updateResponse, response);
	}
	
//	@Test
    public void sendTextMessageToProducersJMSQueue() throws Exception {
        
		String chunkUpdateRequest = "<c1>Test msg</c1>";//readFile(testKit.getResourceAsStream("/soaps/update-producers-request.xml"));
		
		Session session = _hqMixIn.getJMSSession();
        MessageProducer producer = session.createProducer(HornetQMixIn.getJMSQueue(PRODUCER_QUEUE_NAME));
        Message message = _hqMixIn.createJMSMessage(chunkUpdateRequest);
        producer.send(message);

        MessageConsumer consumer = session.createConsumer(HornetQMixIn.getJMSQueue(PRODUCER_QUEUE_NAME));
        message = consumer.receive(3000);
        String reply = _hqMixIn.readStringFromJMSMessage(message);
        SwitchYardTestKit.compareXMLToString(reply, chunkUpdateRequest);
    }

}
