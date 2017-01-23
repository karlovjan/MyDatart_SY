package cz.datart.jboss.myDatart.eshop.notification;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

public class PrepareUpdateEntity extends RouteBuilder {

	private static final String LOG_NAME = "PrepareUpdateEntity-Notification";
	
	public void configure() {
		
		from("switchyard://PrepareUpdateEntityService")
			.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Message received from PrepareUpdateEntityService: ${body}")
			.choice()
			  .when(exchangeProperty("deleteEntity").isEqualTo("true"))
			  	.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Delete entity: ${property.entity}")
			  	.to("switchyard://PrepareDeletingEntityService")
			  .otherwise()
			  	.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Prepare update of the entity: ${property.entity}")
			  	.to("switchyard://PrepareGettingDetailService")
			  	
			  .end();
	}

}
