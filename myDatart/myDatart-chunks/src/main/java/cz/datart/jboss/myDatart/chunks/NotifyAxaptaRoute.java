package cz.datart.jboss.myDatart.chunks;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

public class NotifyAxaptaRoute extends RouteBuilder {

	private static final String LOG_NAME = "NotifyAxaptaRoute";
	
	/**
	 * The Camel route is configured via this method.  The from endpoint is required to be a SwitchYard service.
	 */
	public void configure() {
		
		from("switchyard://NotifyAxaptaService")
		
		
		.choice()
		  .when().method(ChunkGroupConfigurationStorage.class, "notifyAxapta(${property.chunkName})")
		  	.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Send notification to axapta for chunk ${property.chunkName}")
			
			.to("switchyard://PrepareAxaptaNotificationService")//axapta
			
		  .otherwise()
			.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | The chunk ${property.chunkName} is not set for sending notifications to Axapta")
		.endChoice();
	}

}
