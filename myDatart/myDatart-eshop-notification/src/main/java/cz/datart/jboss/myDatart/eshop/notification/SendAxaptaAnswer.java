package cz.datart.jboss.myDatart.eshop.notification;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

public class SendAxaptaAnswer extends RouteBuilder {

	private static final String LOG_NAME = "SendAxaptaAnswer-Notification";
	
	public void configure() {
		
		from("switchyard://SendAxaptaAnswerService")
		
		.errorHandler(noErrorHandler())
		
		.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Message received from 'SendAxaptaAnswerService': ${body}")
		
		.to("switchyard://AxaptaWS")
		
		;
	}

}
