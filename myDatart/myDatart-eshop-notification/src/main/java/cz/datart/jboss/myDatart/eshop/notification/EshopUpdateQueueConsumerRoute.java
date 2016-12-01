package cz.datart.jboss.myDatart.eshop.notification;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

public class EshopUpdateQueueConsumerRoute extends RouteBuilder {

	private static final String LOG_NAME = "EshopUpdateQueueConsumerRoute";
	
	public void configure() {
	
//		onException(UnknownOperationException.class).handled(true).log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Unknown operation exception was catched and process is stopped");
		
		from("switchyard://EshopUpdateQueueConsumerService")
		.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Message received from EshopUpdateQueueConsumerService: ${body}")
		  .choice()
		    .when().xpath("boolean(//*[local-name() = 'notifyUser'])")
		    	.setProperty("entity").constant("User")
			    .setProperty("entityId", xpath("//*[local-name() = 'clientId']/text()"))
			    .setProperty("deleteEntity", xpath("//*[local-name() = 'deleted']/text()"))
			    
			    .to("switchyard://PrepareUpdateEntityService")
			    
			.when().xpath("boolean(//*[local-name() = 'notifyOrder'])")
				.setProperty("entity").constant("Order")
				.setProperty("entityId", xpath("//*[local-name() = 'orderId']/text()"))
			    .setProperty("deleteEntity", constant("false"))
			    
			    .to("switchyard://PrepareUpdateEntityService")
			    
			.when().xpath("boolean(//*[local-name() = 'notifyManual'])")
				.setProperty("entity").constant("Manual")
			    .setProperty("entityId", xpath("//*[local-name() = 'manualId']/text()"))
			    .setProperty("deleteEntity", xpath("//*[local-name() = 'deleted']/text()"))
			    
			    .to("switchyard://PrepareUpdateEntityService")
			    
			.when().xpath("boolean(//*[local-name() = 'notifyOffer'])")
				.setProperty("entity").constant("Offer")
			    .setProperty("entityId", xpath("//*[local-name() = 'id']/text()"))
			    .setProperty("deleteEntity", xpath("//*[local-name() = 'deleted']/text()"))
			    
			    .to("switchyard://PrepareUpdateEntityService") 
			    
			.when().xpath("boolean(//*[local-name() = 'updateComplaint'])")
				.setProperty("entity").constant("Complaint")
			    .setProperty("entityId", xpath("//*[local-name() = 'id']/text()"))
			    .setProperty("deleteEntity", constant("false"))
			    .to("switchyard://PrepareUpdateEntityService")
			    
			.otherwise()
			    .log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Unknown operation")
//			    .throwException(new UnknownOperationException("Unknown notification operation"))
			
			    
			.end();
			    
	}

}
