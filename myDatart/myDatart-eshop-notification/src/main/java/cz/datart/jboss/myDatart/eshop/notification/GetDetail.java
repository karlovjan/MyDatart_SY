package cz.datart.jboss.myDatart.eshop.notification;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

public class GetDetail extends RouteBuilder {

	private static final String LOG_NAME = "GetDetail-Notification";
	
	public void configure() {
				
		from("switchyard://GetDetailService")
		
		.errorHandler(noErrorHandler())
		
		.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Message received from 'GetDetailService': ${body}")
		
		.to("switchyard://AxaptaWS")
		
		.to("switchyard://ProcessAxaptaAnswResponseService")
		
		.to("switchyard://PrepareUpdateEshopEntityService")
		
		;
	}

}
