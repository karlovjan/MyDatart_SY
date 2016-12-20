package cz.datart.jboss.myDatart.eshop.notification;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

public class ProcessUpdateEshopResponse extends RouteBuilder {

	private static final String LOG_NAME = "ProcessUpdateEshopResponse-Notification";
	
	public void configure() {
		
		from("switchyard://ProcessUpdateEshopResponseService")
		
		.errorHandler(noErrorHandler())
		
		.log(LoggingLevel.INFO, LOG_NAME, "Received message for 'ProcessUpdateEshopResponseService' : ${body}")
		
		.choice()
			.when(xpath("boolean(//*[local-name() = 'Fault'])"))
				.log(LoggingLevel.INFO, LOG_NAME, "Fault response on deletion from Eshop")
				.setHeader("entityId", simple("${property.entityId}", String.class))
				.setHeader("newItemVersion", simple("${property.newItemVersion}", String.class))
				.setHeader("axaResultOk", simple("false", String.class))
				
				.setProperty("FaultErrorMessage").xpath("concat(//*[local-name() = 'Fault']//faultcode[position() = 1]/text(), ',',//*[local-name() = 'Fault']//faultcode[position() = 2]/text(), ',',//*[local-name() = 'Fault']//faultstring/text())", String.class)
				
				.setHeader("axaErrorText").exchangeProperty("FaultErrorMessage")
				
				.throwException(new FaultException("General soap fault exception"))	
			
			.otherwise()
				.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Response from Eshop is OK")
				.setHeader("entityId", simple("${property.entityId}", String.class))
				.setHeader("newItemVersion", simple("${property.newItemVersion}", String.class))
				.setHeader("axaResultOk", simple("true", String.class))
				.setHeader("axaErrorText", simple("OK", String.class))
				
				.to("switchyard://PrepareAxaptaNotificationService")
				.end()
			
		
		
		;
	}

}
