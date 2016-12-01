package cz.datart.jboss.myDatart.eshop.notification;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

public class UpdateEshopEntity extends RouteBuilder {

	private static final String LOG_NAME = "UpdateEshopEntity-Notification";
	
	public void configure() {
		
		from("switchyard://UpdateEshopEntityService")
		
		.errorHandler(noErrorHandler())
		
		.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Message received from 'UpdateEshopEntityService': ${body}")
		
		.to("switchyard://EshopWS")
		
		.to("switchyard://ProcessUpdateEshopResponseService")
		
		;
	}

}
