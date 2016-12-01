package cz.datart.jboss.myDatart.eshop.notification;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

public class DeleteEntity extends RouteBuilder {

	private static final String LOG_NAME = "DeleteEntity-Notification";
	
	
	public void configure() {
		
		from("switchyard://DeleteEntityService")
		.errorHandler(noErrorHandler())
		
		.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Message received from 'DeleteEntityService': ${body}")
		
		.to("switchyard://EshopWS")
		
		.to("switchyard://ProcessUpdateEshopResponseService")
		
		;
		
	}

}
