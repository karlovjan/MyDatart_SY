package cz.datart.jboss.myDatart.chunks;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

import cz.datart.jboss.myDatart.chunks.exception.EmptyResponseException;
import cz.datart.jboss.myDatart.chunks.exception.SoapFaultException;

public class PrepareAxaptaNotification extends RouteBuilder {

	private static final String LOG_NAME = "PrepareAxaptaNotification";
	/**
	 * The Camel route is configured via this method.  The from endpoint is required to be a SwitchYard service.
	 */
	public void configure() {
		
		onException(EmptyResponseException.class).handled(true).maximumRedeliveries(2).redeliveryDelay(2000).delay(5000).asyncDelayed();
		
		onException(SoapFaultException.class).handled(true).maximumRedeliveries(2).redeliveryDelay(2000).delay(5000).asyncDelayed();
		
		
		from("switchyard://PrepareAxaptaNotificationService")
		.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Prepare a notification for Axapta for chunk ${property.chunkName}")
		
	  	.setBody(property("recIDs"))
	  	
//	  	.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Rec ids ${body}")
	  	
//	  	.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Rec ids ${property.eshopError}")
	  	
	  	.setHeader("error").property("isError")
		.setHeader("errorText", property("eshopError"))
		
		.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | isError: ${in.header.error}, errorText: ${in.header.errorText}")
		
		
		.to("xslt:xsl/createNotificationError.xsl?saxon=true")
	
		.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Axapta confirmation request xslt transformation: ${body}")
			
		.bean(ChunkUtils.class, "createConfirmTransAxaptaRequest(${property.chunkName}, ${property.newVersion}, ${body})")
		
		.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Axapta confirmation request: ${body}")
		
		.to("switchyard://SendAxaptaNotificationService");
	}

}
