package cz.datart.jboss.myDatart.eshop.notification;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

public class ProcessAxaptaAnswResponse extends RouteBuilder {

	private static final String LOG_NAME = "ProcessAxaptaAnswResponse-Notification";
	
	public void configure() {
		
		from("switchyard://ProcessAxaptaAnswResponseService")
				
		.errorHandler(noErrorHandler())
		
		.log(LoggingLevel.INFO, LOG_NAME, "Received message for 'ProcessAxaptaAnswResponseService' : ${body}")
		
		.choice()
			.when(xpath("boolean(//*[local-name() = 'Fault'])"))
				.log(LoggingLevel.INFO, LOG_NAME, "Fault response from axapta")
				
//			neni videt v rutine na zachycujici vyjimku	.setProperty("FaultErrorMessage").xpath("concat(//*[local-name() = 'Fault']//faultcode[position() = 1]/text(), ',',//*[local-name() = 'Fault']//faultcode[position() = 2]/text(), ',',//*[local-name() = 'Fault']//faultstring/text())", String.class)
				
				.throwException(new FaultException("Genera soap fault exception from Axapta"))	
			
			.otherwise()
				.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Axapta response is OK")
				
		.end()
		
		;
	}

}
