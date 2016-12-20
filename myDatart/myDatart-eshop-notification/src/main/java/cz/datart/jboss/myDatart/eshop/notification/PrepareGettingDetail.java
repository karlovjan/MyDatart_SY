package cz.datart.jboss.myDatart.eshop.notification;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.xml.sax.SAXException;

public class PrepareGettingDetail extends RouteBuilder {

	private static final String LOG_NAME = "PrepareGettingDetail-Notification";
	
	public void configure() {
		
		onException(FaultException.class).handled(true).maximumRedeliveries(2).redeliveryDelay(2000).delay(2000).asyncDelayed().log(LoggingLevel.ERROR, LOG_NAME, "Fault exception has happend in Axapta on getting ${property.entity} detail.");
		
//		onException(SAXException.class).handled(true).log(LoggingLevel.ERROR, LOG_NAME, "SAXException has happend in Axapta on getting ${property.entity} detail");
		onException(SAXException.class).handled(true).log(LoggingLevel.ERROR, LOG_NAME, "SAXException has happend in Axapta on getting ${property.entity} detail.");
		
		//default error handler just log the error
		errorHandler(loggingErrorHandler(LOG_NAME));
		
		from("switchyard://PrepareGettingDetailService")
		
		.log(LoggingLevel.INFO, LOG_NAME, "Received message for 'PrepareGettingDetail' : ${body}")
		
		.choice()
		  .when(exchangeProperty("entity").isEqualTo("User"))
		  	.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Prepere a sending get user detail reguest")

		  	.to("xslt:xsl/sendCustomerDetailRequest.xsl?saxon=true")
		  	
		  	.to("switchyard://GetDetailService")

		  .when(exchangeProperty("entity").isEqualTo("Order"))
		  	.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Prepere a sending get order detail reguest")

		  	.to("xslt:xsl/sendOrderDetailRequest.xsl?saxon=true")

		  	.to("switchyard://GetDetailService")
			
		  .when(exchangeProperty("entity").isEqualTo("Manual"))
		  	.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Prepere a sending get manual detail reguest")

		  	.to("xslt:xsl/sendManualDetailRequest.xsl?saxon=true")

		  	.to("switchyard://GetDetailService")
			
		  .when(exchangeProperty("entity").isEqualTo("Offer"))
		  	.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Prepere a sending get offer detail reguest")

			.to("xslt:xsl/sendOfferDetailRequest.xsl?saxon=true") 

			.to("switchyard://GetDetailService")
			
		  .when(exchangeProperty("entity").isEqualTo("Complaint"))
		  	.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Prepere a sending get complaint detail reguest")

			.to("xslt:xsl/sendComplaintDetailRequest.xsl?saxon=true") 

			.to("switchyard://GetDetailService")
		  .otherwise()
		  	.log(LoggingLevel.WARN, LOG_NAME, "ID: ${id} | Unknown entity to the deleting")
		  	
		  	
		  .end();
	}

}
