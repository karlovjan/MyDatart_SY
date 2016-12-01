package cz.datart.jboss.myDatart.eshop.notification;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.xml.sax.SAXException;

public class PrepareDeletingEntity extends RouteBuilder {

	private static final String LOG_NAME = "PrepareDeletingEntity-Notification";
	
	public void configure() {
		
		onException(FaultException.class).handled(true).maximumRedeliveries(2).redeliveryDelay(2000).delay(2000).asyncDelayed().log(LoggingLevel.ERROR, LOG_NAME, "Fault exception has happend in Eshop on deleting entity ${property.entity}");
		
//		onException(SAXException.class).handled(true).log(LoggingLevel.ERROR, LOG_NAME, "SAXException has happend in Eshop on deleting entity ${property.entity}");
		onException(SAXException.class).handled(true).log(LoggingLevel.ERROR, LOG_NAME, "SAXException has happend in Eshop on deleting entity ${property.entity}");

		//default error handler just log the error
		errorHandler(loggingErrorHandler(LOG_NAME));

		from("switchyard://PrepareDeletingEntityService")
				.log(LoggingLevel.INFO, LOG_NAME, "Received message for 'PrepareDeletingEntityService' : ${body}")
				.choice()
				  .when(property("entity").isEqualTo("User"))
				  	.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Prepere Customer deletion")
//				  	.setProperty("newItemVersion", xpath("//*[local-name() = 'newVersion']/text()"))
//					.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | newItemVersion: ${property.newItemVersion}")
					
					.to("xslt:xsl/deletedCustomerRequest.xsl?saxon=true") 
//					.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | The delete customer request created: ${body}")
					.to("switchyard://DeleteEntityService")
				  .when(property("entity").isEqualTo("Offer"))
				  	.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Prepare Offer deletion")
//				  	.setProperty("newItemVersion", xpath("//*[local-name() = 'newVersion']/text()"))
//					.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | newItemVersion: ${property.newItemVersion}")
					
					.to("xslt:xsl/deleteOfferRequest.xsl?saxon=true") 
//					.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | The delete offer request created: ${body}")
					.to("switchyard://DeleteEntityService")
					
				.when(property("entity").isEqualTo("Manual"))
				  	.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Prepare Manual deletion")
//				  	.setProperty("newItemVersion", xpath("//*[local-name() = 'newVersion']/text()"))
//					.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | newItemVersion: ${property.newItemVersion}")
					
					.to("xslt:xsl/deleteManualRequest.xsl?saxon=true") 
//					.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | The delete manual request created: ${body}")
					.to("switchyard://DeleteEntityService")
				  .otherwise()
				  	.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Unknown entity to the deleting")
				  	.end();
	}

}
