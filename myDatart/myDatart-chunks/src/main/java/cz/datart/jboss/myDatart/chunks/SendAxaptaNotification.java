package cz.datart.jboss.myDatart.chunks;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

public class SendAxaptaNotification extends RouteBuilder {

	private static final String LOG_NAME = "SendAxaptaNotification";
	/**
	 * The Camel route is configured via this method.  The from endpoint is required to be a SwitchYard service.
	 */
	public void configure() {
		// TODO Auto-generated method stub
		from("switchyard://SendAxaptaNotificationService")
		.errorHandler(noErrorHandler())
		.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Send a notification to Axapta for chunk ${property.chunkName}")
		.to("switchyard://AxaptaWS")
		.to("switchyard://ProcessAxaptaNotificationResponseService");
	}

}
