package cz.datart.jboss.myDatart.eshop.notification;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.xml.sax.SAXException;

public class PrepareAxaptaNotification extends RouteBuilder {

	private static final String LOG_NAME = "PrepareAxaptaNotification-Notification";
	
	public void configure() {
		
		onException(FaultException.class).handled(true).maximumRedeliveries(2).redeliveryDelay(2000).delay(2000).asyncDelayed().log(LoggingLevel.INFO, LOG_NAME, "Fault exception has happend in Axapta answer response for entity ${property.entity}");
		
		onException(SAXException.class).handled(true).log(LoggingLevel.ERROR, LOG_NAME, "SAXException has happend in Axapta on sending axa notification for entity ${property.entity}");
		
		//default error handler just log the error
		errorHandler(loggingErrorHandler(LOG_NAME));
		
		
		from("switchyard://PrepareAxaptaNotificationService")
		
		.choice()
		  .when(exchangeProperty("entity").isEqualTo("User"))
		  	.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Prepare a sending User update result to axapta")

		  	.to("xslt:xsl/answDetailCustomerRequest.xsl?saxon=true")
		  	
		  	.to("switchyard://SendAxaptaAnswerService")
		  	
		  .when(exchangeProperty("entity").isEqualTo("Order"))
		  	.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Prepare a sending Order update result to axapta")

		  	.to("xslt:xsl/answDetailOrderRequest.xsl?saxon=true")
		  	
		  	.to("switchyard://SendAxaptaAnswerService")
		  	
		  .when(exchangeProperty("entity").isEqualTo("Manual"))
		  	.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Prepare a sending Manual update result to axapta")

		  	.to("xslt:xsl/answDetailManualRequest.xsl?saxon=true")
		  	
		  	.to("switchyard://SendAxaptaAnswerService")
		  	
		  .when(exchangeProperty("entity").isEqualTo("Offer"))
		  	.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Prepare a sending Offer update result to axapta")

		  	.to("xslt:xsl/answDetailOfferRequest.xsl?saxon=true")
		  	
		  	.to("switchyard://SendAxaptaAnswerService")
		  	
		  .when(exchangeProperty("entity").isEqualTo("Complaint"))
		  	.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Prepare a sending Complaint update result to axapta")

		  	.to("xslt:xsl/answDetailComplaintRequest.xsl?saxon=true")
		  	
		  	.to("switchyard://SendAxaptaAnswerService")
			
		  .otherwise()
		  	.log(LoggingLevel.WARN, LOG_NAME, "ID: ${id} | Unknown entity to the deleting")
		  	
		.end()
		
		
		
		;
	}

}
