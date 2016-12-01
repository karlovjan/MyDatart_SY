package cz.datart.jboss.myDatart.chunks;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

import cz.datart.jboss.myDatart.chunks.exception.ReferentialIntegrityException;
import cz.datart.jboss.myDatart.chunks.exception.SoapFaultException;

public class PrepareChunkSending extends RouteBuilder {

	private static final String LOG_NAME = "PrepareChunkSending";
	
	/**
	 * The Camel route is configured via this method.  The from endpoint is required to be a SwitchYard service.
	 */
	public void configure() {
		
//		onException(DataVersionMismatchException.class).handled(true).maximumRedeliveries(2).delay(5000).asyncDelayed();
		
		//two times resend the chunk to eshop
		onException(ReferentialIntegrityException.class).handled(true).maximumRedeliveries(2).delay(5000).asyncDelayed().log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Ref ERROR:Resend the chunk ${property.chunkName}");;
		
		onException(SoapFaultException.class).handled(true).maximumRedeliveries(2).delay(5000).asyncDelayed().log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Soap fault ERROR:Resend the chunk ${property.chunkName}");
		
		
//		errorHandler(deadLetterChannel("jms:queue:dead").allowRedeliveryWhileStopping(false).maximumRedeliveries(2).redeliveryDelay(5000));
		errorHandler(loggingErrorHandler(LOG_NAME));
		
		
		from("switchyard://PrepareChunkSendingService")
		
		
		
		
//			.to("log:"+LOG_NAME+"?level=INFO&showAll=true&multiline=true")
//				.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Send update${property.chunkName} chunk to eshop")
				
		.setProperty("chunkName", method(ChunkUtils.class, "getChunkNameFromRequestMessage"))
		
		.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Prepare sending of the chunk ${property.chunkName}")
		
		.setHeader(Exchange.HTTP_CHARACTER_ENCODING, constant("utf-8"))
		
		.setProperty("oldVersion", xpath("(//oldVersion)[1]/text()"))
		.setProperty("newVersion", xpath("(//newVersion)[1]/text()"))
		
		.setProperty("chunkRequest", body())
		
		.to("xslt:xsl/getRecId.xsl?saxon=true")
	
		.setProperty("recIDs", body())
		
		
		.setBody(property("chunkRequest"))
		
		//pokud tohle neodstranim pred odeslanim chunku do eshopu tak zpusobuje problem - error 413 full head
		.removeProperty("chunkRequest") 
		
		.to("xslt:xsl/removeRecId.xsl?saxon=true")
		
		.to("switchyard://SendChunkToEshopService");
	}

}
