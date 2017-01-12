package cz.datart.jboss.myDatart.eshop.notification;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.switchyard.component.test.mixins.cdi.CDIMixIn;
import org.switchyard.component.test.mixins.hornetq.HornetQMixIn;
import org.switchyard.component.test.mixins.http.HTTPMixIn;
import org.switchyard.test.BeforeDeploy;
import org.switchyard.test.SwitchYardRunner;
import org.switchyard.test.SwitchYardTestCaseConfig;
import org.switchyard.test.SwitchYardTestKit;

@RunWith(SwitchYardRunner.class)
@SwitchYardTestCaseConfig(
		config = SwitchYardTestCaseConfig.SWITCHYARD_XML, 
		mixins = {CDIMixIn.class, HTTPMixIn.class, HornetQMixIn.class  })
public class AxaNotificationServiceEndpointTest {

	private static final String AXA_NOTIFICATION_WEB_SERVICE = "http://localhost:8081/testcz/eshop/notify/AxaNotificationServiceEndpoint";
	
	private HTTPMixIn httpMixIn;
	private SwitchYardTestKit testKit;
//	private CDIMixIn cdiMixIn;
//	@ServiceOperation("AxaNotificationServiceEndpoint")
//	private Invoker service;
	
	@BeforeDeploy
    public void setProperties() {
        System.setProperty("org.switchyard.component.soap.standalone.port", "8081");
//        System.setProperty("axa.ws.endpoint", "http://localhost:8082/axaptaws");
//        System.setProperty("eshop.update.endpoint", "http://localhost:28080/eShopUpdate");
        
    }
	
//	@Before
//  public void startAxaptaWebService() throws Exception {
//      _endpoint = Endpoint.publish(AXAPTA_WEB_SERVICE, new ReverseService());
//  }

	
//	@Test
//	public void testNotifyManual() throws Exception {
//		// initialize your test message
//		Object message = null;
//		Object result = service.operation("notifyManual").sendInOut(message)
//				.getContent(Object.class);
//
//		// validate the results
//		Assert.assertTrue("Implement me", false);
//	}

	@Test
    public void testNotifyOrder() throws Exception {
        // Use the HttpMixIn to invoke the SOAP binding endpoint with a SOAP input (from the test classpath)
        // and compare the SOAP response to a SOAP response resource (from the test classpath)...
        //!!!Hodne casove narocny test nacitaji se data ze souboru
		
		mockInOnlyReference("OrderQueueRef");

		httpMixIn.postResourceAndTestXML(AXA_NOTIFICATION_WEB_SERVICE, "/soaps/notifyOrder-request.xml", "/soaps/notifyOrder-response.xml");
    
//		int status = _httpMixIn.postResourceAndGetStatus(SWITCHYARD_WEB_SERVICE, "/xml/soap-request-500.xml");
//     
	}

	private void mockInOnlyReference(String refernceName) {
		// register the service...
		testKit.removeService(refernceName);
	
		testKit.registerInOnlyService(refernceName);
		
//		final MockHandler mockHandler = testKit.registerInOnlyService(refernceName);
//		mockHandler.forwardInToOut();
	}
	

	@Test
	public void testNotifyDeletedUser() throws Exception {
		
		mockInOnlyReference("UserQueueRef");
		
		httpMixIn.postResourceAndTestXML(AXA_NOTIFICATION_WEB_SERVICE, "/soaps/notifyDeletedUser-request.xml", "/soaps/notifyUser-response.xml");
	}
	
	@Test
	public void testNotifyUser() throws Exception {
		mockInOnlyReference("UserQueueRef");
		httpMixIn.postResourceAndTestXML(AXA_NOTIFICATION_WEB_SERVICE, "/soaps/notifyUser-request.xml", "/soaps/notifyUser-response.xml");
	}

	@Test
	public void testNotifyOffer() throws Exception {
		mockInOnlyReference("OfferQueueRef");
		httpMixIn.postResourceAndTestXML(AXA_NOTIFICATION_WEB_SERVICE, "/soaps/notifyOffer-request.xml", "/soaps/notifyOffer-response.xml");
	}
	
	@Test
	public void testNotifyManual() throws Exception {
		mockInOnlyReference("ManualQueueRef");
		httpMixIn.postResourceAndTestXML(AXA_NOTIFICATION_WEB_SERVICE, "/soaps/notifyManual-request.xml", "/soaps/notifyManual-response.xml");
	}

	@Test
    public void testUnknownRequest() throws Exception {
		
        httpMixIn.postResourceAndTestXML(AXA_NOTIFICATION_WEB_SERVICE, "/soaps/notifyUnknownMethod-request.xml", "/soaps/notifyUnknownMethod-response.xml");
    }
}
