package cz.datart.jboss.myDatart.eshop.notification;

import java.io.IOException;
import java.io.InputStream;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.switchyard.Exchange;
import org.switchyard.ExchangeHandler;
import org.switchyard.HandlerException;
import org.switchyard.component.test.mixins.cdi.CDIMixIn;
import org.switchyard.component.test.mixins.hornetq.HornetQMixIn;
import org.switchyard.component.test.mixins.http.HTTPMixIn;
import org.switchyard.test.InvocationFaultException;
import org.switchyard.test.Invoker;
import org.switchyard.test.MockHandler;
import org.switchyard.test.ServiceOperation;
import org.switchyard.test.SwitchYardRunner;
import org.switchyard.test.SwitchYardTestCaseConfig;
import org.switchyard.test.SwitchYardTestKit;

import cz.datart.jboss.myDatart.utils.FileUtils;

@RunWith(SwitchYardRunner.class)
@SwitchYardTestCaseConfig(config = SwitchYardTestCaseConfig.SWITCHYARD_XML, mixins = { CDIMixIn.class,
		HTTPMixIn.class, HornetQMixIn.class  })
public class EshopUpdateQueueConsumerServiceTest {

	private SwitchYardTestKit testKit;
	@ServiceOperation("EshopUpdateQueueConsumerService")
	private Invoker service;

	public String readFile(InputStream input) throws IOException {
//        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input))) {
//            return buffer.lines().collect(Collectors.joining("\n"));
//        }
		
		return FileUtils.readFile(input);
    }
	
	@Test
	public void testDeleteUser() throws Exception {
		
		// initialize your test message
		String deleteUserNOtificationREquest = readFile(testKit.getResourceAsStream("/soaps/notifyDeletedUser-request.xml"));
		
		
		String eshopUpdateResponse = readFile(testKit.getResourceAsStream("/soaps/eshop/eshopUpdateUser_OKResponse.xml")); 
		String axaptaResponse = readFile(testKit.getResourceAsStream("/soaps/axapta/answDetailCustomer-ok.xml")); 
		
        // register the service...
		testKit.removeService("EshopWS");
		final MockHandler mockedEshopWSServiceHandler = testKit.registerInOutService("EshopWS");
		mockedEshopWSServiceHandler.replyWithOut(eshopUpdateResponse);
		
		testKit.removeService("AxaptaWS");
		final MockHandler mockedAxaptaWSHandler = testKit.registerInOutService("AxaptaWS");
		mockedAxaptaWSHandler.replyWithOut(axaptaResponse);
		       
		
		
		service.operation("").sendInOnly(deleteUserNOtificationREquest);

		// validate the results
		Assert.assertTrue("OK", true);
	}
	
	@Test
	public void testDeleteUser_EshopFault() throws Exception {
		
		// initialize your test message
		String deleteUserNOtificationREquest = readFile(testKit.getResourceAsStream("/soaps/notifyDeletedUser-request.xml"));
		
		
		final String eshopUpdateResponse_Fault = readFile(testKit.getResourceAsStream("/soaps/eshop/eshopUpdateFaultResponse.xml")); 
		final String axaptaResponse = readFile(testKit.getResourceAsStream("/soaps/axapta/answDetailCustomer-ok.xml")); 
		final String eshopUpdateResponse_OK = readFile(testKit.getResourceAsStream("/soaps/eshop/eshopUpdateUser_OKResponse.xml"));
        // register the service...
//		testKit.removeService("EshopWS");
		testKit.replaceService("EshopWS", new ExchangeHandler() {
			
			int redeliveryCount = 1;
			
			@Override
			public void handleMessage(Exchange exchange) throws HandlerException {
				if (redeliveryCount == 1){ // First call
	                // Do whatever wants to be done as result of this operation call, and return the expected output
	                //Result result = ...; / Result is return type for operation store
	                exchange.send(exchange.createMessage().setContent(eshopUpdateResponse_Fault));
	            }else if (redeliveryCount == 2){ // Second call
	                // Do whatever wants to be done as result of this operation call, and return the expected output
//	                Result result = ...; / Result is return type for operation store
	                exchange.send(exchange.createMessage().setContent(eshopUpdateResponse_OK));
	            }else{
	                throw new HandlerException("This mock should not be called more than 2 times");
	            }
				
				++redeliveryCount;
			}
			
			@Override
			public void handleFault(Exchange exchange) {
				System.err.println("XXXXXXXXXXXXXXXXXXX");
			}
			
		});
//		mockedEshopWSServiceHandler.replyWithOut(eshopUpdateResponse);
		
		testKit.removeService("AxaptaWS");
		final MockHandler mockedAxaptaWSHandler = testKit.registerInOutService("AxaptaWS");
		mockedAxaptaWSHandler.replyWithOut(axaptaResponse);
		       
		
		try{
			service.operation("").sendInOnly(deleteUserNOtificationREquest);
		} catch (InvocationFaultException ifex){
            if(ifex.isType(cz.datart.jboss.myDatart.eshop.notification.FaultException.class)){
            	Assert.assertTrue("OK " + ifex.getMessage(), true);
            } else if(ifex.isType(HandlerException.class)){
            	Assert.assertTrue("HandlerException " + ifex.getMessage(), true);
            } else {
            	Assert.assertTrue("Error test: " + ifex.getMessage(), false);
            }
        }

		// validate the results
		Assert.assertTrue("OK", true);
	}

	@Test
	public void testUpdateUserNotify() throws Exception {
		
		// initialize your test message
		String userNotificationRequest = readFile(testKit.getResourceAsStream("/soaps/notifyUser-request.xml"));
		
		
		String eshopUpdateResponse = readFile(testKit.getResourceAsStream("/soaps/eshop/eshopUpdateUser_OKResponse.xml")); 
		String axaptaAnswResponse = readFile(testKit.getResourceAsStream("/soaps/axapta/answDetailCustomer-ok.xml")); 
		String axaptaDetailResponse = readFile(testKit.getResourceAsStream("/soaps/axapta/sendDetailCustResponse_ok.xml"));
        // register the service...
		testKit.removeService("EshopWS");
		final MockHandler mockedEshopWSServiceHandler = testKit.registerInOutService("EshopWS");
		mockedEshopWSServiceHandler.replyWithOut(eshopUpdateResponse);
		
		testKit.replaceService("AxaptaWS", new ExchangeHandler() {
			
			int callServiceCount = 1;
			
			@Override
			public void handleMessage(Exchange exchange) throws HandlerException {
				if (callServiceCount == 1){ // First call
	                // Do whatever wants to be done as result of this operation call, and return the expected output
	                //Result result = ...; / Result is return type for operation store
	                exchange.send(exchange.createMessage().setContent(axaptaDetailResponse));
	            }else if (callServiceCount == 2){ // Second call
	                // Do whatever wants to be done as result of this operation call, and return the expected output
//	                Result result = ...; / Result is return type for operation store
	                exchange.send(exchange.createMessage().setContent(axaptaAnswResponse));
	            }else{
	                throw new HandlerException("This mock should not be called more than 2 times");
	            }
				
				++callServiceCount;
			}
			
			@Override
			public void handleFault(Exchange exchange) {
				System.err.println(exchange.getMessage().getContent(String.class));
			}
			
		});
		       
		
		
		service.operation("").sendInOnly(userNotificationRequest);

		// validate the results
		Assert.assertTrue("OK", true);
	}
	
	@Test
	public void testUpdateOrderNotify() throws Exception {
		
		// initialize your test message
		String userNotificationRequest = readFile(testKit.getResourceAsStream("/soaps/notifyOrder-request.xml"));
		
		
		String eshopUpdateResponse = readFile(testKit.getResourceAsStream("/soaps/eshop/eshopUpdateOrder_OKResponse.xml")); 
		String axaptaAnswResponse = readFile(testKit.getResourceAsStream("/soaps/axapta/answDetailOrder-ok.xml")); 
		String axaptaDetailResponse = readFile(testKit.getResourceAsStream("/soaps/axapta/sendDetailOrderResponse_ok.xml"));
        // register the service...
		testKit.removeService("EshopWS");
		final MockHandler mockedEshopWSServiceHandler = testKit.registerInOutService("EshopWS");
		mockedEshopWSServiceHandler.replyWithOut(eshopUpdateResponse);
		
		testKit.replaceService("AxaptaWS", new ExchangeHandler() {
			
			int callServiceCount = 1;
			
			@Override
			public void handleMessage(Exchange exchange) throws HandlerException {
				if (callServiceCount == 1){ // First call
	                // Do whatever wants to be done as result of this operation call, and return the expected output
	                //Result result = ...; / Result is return type for operation store
	                exchange.send(exchange.createMessage().setContent(axaptaDetailResponse));
	            }else if (callServiceCount == 2){ // Second call
	                // Do whatever wants to be done as result of this operation call, and return the expected output
//	                Result result = ...; / Result is return type for operation store
	                exchange.send(exchange.createMessage().setContent(axaptaAnswResponse));
	            }else{
	                throw new HandlerException("This mock should not be called more than 2 times");
	            }
				
				++callServiceCount;
			}
			
			@Override
			public void handleFault(Exchange exchange) {
				System.err.println(exchange.getMessage().getContent(String.class));
			}
			
		});
		       
		
		
		service.operation("").sendInOnly(userNotificationRequest);

		// validate the results
		Assert.assertTrue("OK", true);
	}
}
