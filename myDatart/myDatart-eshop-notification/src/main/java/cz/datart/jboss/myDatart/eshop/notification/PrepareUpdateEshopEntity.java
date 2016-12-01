package cz.datart.jboss.myDatart.eshop.notification;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.xml.sax.SAXException;

import cz.datart.jboss.myDatart.utils.BodyTransformer;

public class PrepareUpdateEshopEntity extends RouteBuilder {

	private static final String LOG_NAME = "PrepareUpdateEshopEntity-Notification";

	public void configure() {
		
		onException(FaultException.class).handled(true).maximumRedeliveries(2).redeliveryDelay(2000).delay(2000).asyncDelayed().log(LoggingLevel.ERROR, LOG_NAME, "Fault exception has happend in Eshop on updating entity ${property.entity}.");
		
//		onException(SAXException.class).handled(true).log(LoggingLevel.ERROR, LOG_NAME, "SAXException has happend in Eshop on updating entity ${property.entity}");
		onException(SAXException.class).handled(true).log(LoggingLevel.ERROR, LOG_NAME, "SAXException has happend in Eshop on updating entity ${property.entity}");

		//default error handler just log the error
		errorHandler(loggingErrorHandler(LOG_NAME));
		
		from("switchyard://PrepareUpdateEshopEntityService")
				
		.log(LoggingLevel.INFO, LOG_NAME, "Received message for 'PrepareUpdateEshopEntityService' : ${body}")
		
//		.setBody(xpath("//*[matches(local-name(), 'SendDetail[a-zA-Z]+Result')][1]/text()").factory(saxonFac).resultType(String.class))
//		.setBody(builder.evaluate(context, "<foo><bar>abc_def_ghi</bar></foo>"))
		
		.setBody(method(EnvelopeEshopUpdateResult.class, "getResult(${body})"))
				
		.choice()
			.when(method(BodyTransformer.class, "notEmptyBody(${body})"))
				
				.log(LoggingLevel.INFO, LOG_NAME, "Result was enveloped: ${body}")
		
				.bean(BodyTransformer.class, "convertXmlStringToXML(${body})")
				  
				.setProperty("newItemVersion", xpath("//*[local-name() = 'newVersion']/text()"))
				
				.to("switchyard://UpdateEshopEntityService")
				
			.otherwise()
				.log(LoggingLevel.INFO, LOG_NAME, "Body is null")
				.end()
		  
		;
	}

}
