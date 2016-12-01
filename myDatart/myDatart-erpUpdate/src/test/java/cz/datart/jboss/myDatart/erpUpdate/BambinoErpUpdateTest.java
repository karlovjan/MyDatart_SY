package cz.datart.jboss.myDatart.erpUpdate;

import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.switchyard.component.test.mixins.cdi.CDIMixIn;
import org.switchyard.component.test.mixins.http.HTTPMixIn;
import org.switchyard.test.BeforeDeploy;
import org.switchyard.test.Invoker;
import org.switchyard.test.MockHandler;
import org.switchyard.test.ServiceOperation;
import org.switchyard.test.SwitchYardRunner;
import org.switchyard.test.SwitchYardTestCaseConfig;
import org.switchyard.test.SwitchYardTestKit;

import cz.datart.jboss.myDatart.utils.ApplicationProperty;
import cz.datart.jboss.myDatart.utils.FileUtils;

@RunWith(SwitchYardRunner.class)
@SwitchYardTestCaseConfig(
		config = SwitchYardTestCaseConfig.SWITCHYARD_XML, 
		mixins = { HTTPMixIn.class, CDIMixIn.class })
public class BambinoErpUpdateTest {

	private static final String ERP_UPDATE_WEB_SERVICE = "http://localhost:8081/test1/erpUpdate/BambinoErpUpdateService";
	
	private HTTPMixIn httpMixIn;
//	protected CDIMixIn _cdimixin;
//	
	@ServiceOperation("BambinoErpUpdateService")
	private Invoker erpUpdateService;

	private SwitchYardTestKit testKit;
	
	@ApplicationProperty(name="myDatart.erp.update.context.path")
	private String contextPath;
	
	@BeforeDeploy
    public void setProperties() {
        System.setProperty("org.switchyard.component.soap.standalone.port", "8081");
    }
	
//	@Before
//  public void startAxaptaWebService() throws Exception {
//      _endpoint = Endpoint.publish(AXAPTA_WEB_SERVICE, new ReverseService());
//  }

	
	@Test
	public void testUnknownRequest() throws Exception {
		
		httpMixIn.postResourceAndTestXML(ERP_UPDATE_WEB_SERVICE, "/soaps/erpUpdateUnknownMethod-request.xml", "/soaps/erpUpdateUnknownMethod-response.xml");
	}
	
	@Test
	public void testUpdateUser() throws IOException {
		 
		String axaResponse = FileUtils.readFile(testKit.getResourceAsStream("/soaps/axapta/receiveDetailCust-response.xml"));
		
        // register the service...
		testKit.removeService("AxaptaWS");

		final MockHandler mockHandler = testKit.registerInOutService("AxaptaWS");
		mockHandler.replyWithOut(axaResponse);
        
		httpMixIn.postResourceAndTestXML(ERP_UPDATE_WEB_SERVICE, "/soaps/updateUser-request.xml", "/soaps/updateUser-response.xml");
		
	}
	
//	@Test TODO soap fault exception from reference
	public void testUpdateUser_Failed() throws IOException {
		 
		String axaResponse = FileUtils.readFile(testKit.getResourceAsStream("/soaps/faultResponse.xml"));
		
        // register the service...
		testKit.removeService("AxaptaWS");

		final MockHandler mockHandler = testKit.registerInOutService("AxaptaWS");
		mockHandler.replyWithFault(axaResponse);
        
		httpMixIn.postResourceAndTestXML(ERP_UPDATE_WEB_SERVICE, "/soaps/updateUser-request.xml", "/soaps/faultResponse.xml");
		
	}
	
//	public class UpdateUserServiceHandler implements ExchangeHandler {
//
//		@Override
//		public void handleFault(Exchange arg0) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void handleMessage(Exchange exchange) throws HandlerException {
//			
//		}
//		
//	}
	
	@Test
	public void testUpdateOrder() throws IOException {
		 
		String axaResponse = FileUtils.readFile(testKit.getResourceAsStream("/soaps/axapta/receiveDetailOrder-response.xml"));
		
        // register the service...
		testKit.removeService("AxaptaWS");

		final MockHandler mockHandler = testKit.registerInOutService("AxaptaWS");
		mockHandler.replyWithOut(axaResponse);
        
		httpMixIn.postResourceAndTestXML(ERP_UPDATE_WEB_SERVICE, "/soaps/updateOrder-request.xml", "/soaps/updateOrder-response.xml");
		
	}
	
	@Test
	public void testUpdateComplaint() throws IOException {
		 
		String axaResponse = FileUtils.readFile(testKit.getResourceAsStream("/soaps/axapta/receiveDetailComplaint-response.xml"));
		
        // register the service...
		testKit.removeService("AxaptaWS");

		final MockHandler mockHandler = testKit.registerInOutService("AxaptaWS");
		mockHandler.replyWithOut(axaResponse);
        
		httpMixIn.postResourceAndTestXML(ERP_UPDATE_WEB_SERVICE, "/soaps/updateComplaint-request.xml", "/soaps/updateComplaint-response.xml");
		
	}
	
	@Test
	public void testChangeComplaintState() throws IOException {
		 
		String axaResponse = FileUtils.readFile(testKit.getResourceAsStream("/soaps/axapta/changeComplaintState-response.xml"));
		
        // register the service...
		testKit.removeService("AxaptaWS");

		final MockHandler mockHandler = testKit.registerInOutService("AxaptaWS");
		mockHandler.replyWithOut(axaResponse);
        
		httpMixIn.postResourceAndTestXML(ERP_UPDATE_WEB_SERVICE, "/soaps/changeComplaintState-request.xml", "/soaps/changeComplaintState-response.xml");
		
	}
	
	@Test
	public void testChangeOrderOwner() throws IOException {
		 
		String axaResponse = FileUtils.readFile(testKit.getResourceAsStream("/soaps/axapta/addUserOrder-response.xml"));
		
        // register the service...
		testKit.removeService("AxaptaWS");

		final MockHandler mockHandler = testKit.registerInOutService("AxaptaWS");
		mockHandler.replyWithOut(axaResponse);
        
		httpMixIn.postResourceAndTestXML(ERP_UPDATE_WEB_SERVICE, "/soaps/changeOrderOwner-request.xml", "/soaps/changeOrderOwner-response.xml");
		
	}
	
//	@Test
//  public void testUpdateComplaintWebMethod() throws Exception {
//      // Use the HttpMixIn to invoke the SOAP binding endpoint with a SOAP input (from the test classpath)
//      // and compare the SOAP response to a SOAP response resource (from the test classpath)...
//      //!!!Hodne casove narocny test nacitaji se data ze souboru
//		httpMixIn.postResourceAndTestXML(ERP_UPDATE_WEB_SERVICE, "/soaps/updateComplaint-request.xml", "/soaps/updateComplaint-response.xml");
//  }
	

//	@Test
//	public void testUpdateUser() throws Exception {
//		
//		Object message = "<bam:updateUser  xmlns:bam=\"http://etnetera.com/projects/datart/bambino\"><segment>CZ</segment><user deleted=\"false\"><id>ESH-478478</id><businessRelationship>B2C</businessRelationship></user></bam:updateUser>";
//		String result = service.property("org.switchyard.soap.messageName", "updateUserRequest").operation("updateUser").sendInOut(message)
//				.getContent(String.class);
//
//		// validate the results <bam:updateUserResponse/>
//		Assert.assertTrue("<bam:updateUserResponse xmlns:bam=\"http://etnetera.com/projects/datart/bambino\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"/>".equals(result));
//	}
	
//	@Test
//    public void testUpdateUserWebMethod() throws Exception {
//        // Use the HttpMixIn to invoke the SOAP binding endpoint with a SOAP input (from the test classpath)
//        // and compare the SOAP response to a SOAP response resource (from the test classpath)...
//        //!!!Hodne casove narocny test nacitaji se data ze souboru
//		httpMixIn.postResourceAndTestXML(ERP_UPDATE_WEB_SERVICE, "/soaps/updateUser-request.xml", "/soaps/updateUser-response.xml");
//    
////		int status = _httpMixIn.postResourceAndGetStatus(SWITCHYARD_WEB_SERVICE, "/xml/soap-request-500.xml");
////        Assert.assertEquals(500, status);
//	}
	
//	@Test
//    public void testUpdateOrderWebMethod() throws Exception {
//        // Use the HttpMixIn to invoke the SOAP binding endpoint with a SOAP input (from the test classpath)
//        // and compare the SOAP response to a SOAP response resource (from the test classpath)...
//        //!!!Hodne casove narocny test nacitaji se data ze souboru
//		httpMixIn.postResourceAndTestXML(ERP_UPDATE_WEB_SERVICE, "/soaps/updateOrder-request.xml", "/soaps/updateOrder-response.xml");
//    }
//	
////	@Test
////    public void testUpdateNeededWebMethod() throws Exception {
////        // Use the HttpMixIn to invoke the SOAP binding endpoint with a SOAP input (from the test classpath)
////        // and compare the SOAP response to a SOAP response resource (from the test classpath)...
////        //!!!Hodne casove narocny test nacitaji se data ze souboru
////		httpMixIn.postResourceAndTestXML(ERP_UPDATE_WEB_SERVICE, "/soaps/updateNeeded-request.xml", "/soaps/updateNeeded-response.xml");
////    }
//	
//	@Test
//    public void testChangeOrderOwnerWebMethod() throws Exception {
//        // Use the HttpMixIn to invoke the SOAP binding endpoint with a SOAP input (from the test classpath)
//        // and compare the SOAP response to a SOAP response resource (from the test classpath)...
//        //!!!Hodne casove narocny test nacitaji se data ze souboru
//		httpMixIn.postResourceAndTestXML(ERP_UPDATE_WEB_SERVICE, "/soaps/changeOrderOwner-request.xml", "/soaps/changeOrderOwner-response.xml");
//    }
//	
//	@Test
//    public void testUpdateComplaintWebMethod() throws Exception {
//        // Use the HttpMixIn to invoke the SOAP binding endpoint with a SOAP input (from the test classpath)
//        // and compare the SOAP response to a SOAP response resource (from the test classpath)...
//        //!!!Hodne casove narocny test nacitaji se data ze souboru
//		httpMixIn.postResourceAndTestXML(ERP_UPDATE_WEB_SERVICE, "/soaps/updateComplaint-request.xml", "/soaps/updateComplaint-response.xml");
//    }
//	
//	@Test
//    public void testChangeComplaintStateWebMethod() throws Exception {
//        // Use the HttpMixIn to invoke the SOAP binding endpoint with a SOAP input (from the test classpath)
//        // and compare the SOAP response to a SOAP response resource (from the test classpath)...
//        //!!!Hodne casove narocny test nacitaji se data ze souboru
//		httpMixIn.postResourceAndTestXML(ERP_UPDATE_WEB_SERVICE, "/soaps/changeComplaintState-request.xml", "/soaps/changeComplaintState-response.xml");
//    }
//	
////	@Test
////    public void testUpdateExpressOrderWebMethod() throws Exception {
////        // Use the HttpMixIn to invoke the SOAP binding endpoint with a SOAP input (from the test classpath)
////        // and compare the SOAP response to a SOAP response resource (from the test classpath)...
////        //!!!Hodne casove narocny test nacitaji se data ze souboru
////		httpMixIn.postResourceAndTestXML(ERP_UPDATE_WEB_SERVICE, "/soaps/updateUpdateExpressOrder-request.xml", "/soaps/updateUpdateExpressOrder-response.xml");
////    }
//	
//	@Test
//    public void testUnknownRequest() throws Exception {
//        // Use the HttpMixIn to invoke the SOAP binding endpoint with a SOAP input (from the test classpath)
//        // and compare the SOAP response to a SOAP response resource (from the test classpath)...
//        //!!!Hodne casove narocny test nacitaji se data ze souboru
//		httpMixIn.postResourceAndTestXML(ERP_UPDATE_WEB_SERVICE, "/soaps/erpUpdateUnknownMethod-request.xml", "/soaps/erpUpdateUnknownMethod-response.xml");
//    }
}
