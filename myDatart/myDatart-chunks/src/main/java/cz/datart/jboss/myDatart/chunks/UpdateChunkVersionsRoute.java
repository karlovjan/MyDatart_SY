package cz.datart.jboss.myDatart.chunks;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

public class UpdateChunkVersionsRoute extends RouteBuilder {

	private static final String LOG_NAME = "UpdateChunkVersionsRoute";
	
	/**
	 * The Camel route is configured via this method.  The from endpoint is required to be a SwitchYard service.
	 */
	public void configure() {
		
//		onException(java.net.ConnectException.class).handled(true).maximumRedeliveries(2).delay(5000).setBody().constant("").log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | redelivery Get chunk versions");
		
		onException(java.net.ConnectException.class).handled(true).maximumRedeliveries(2).delay(5000).log(LoggingLevel.ERROR, LOG_NAME, "ID: ${id} | redelivery Get chunk versions");
		
		onException(javax.xml.soap.SOAPException.class).handled(true).maximumRedeliveries(2).delay(5000).log(LoggingLevel.ERROR, LOG_NAME, "ID: ${id} | redelivery Get chunk versions");
		
		//deadLetterChannel neni nastaven na wildfly
//		errorHandler(deadLetterChannel("jms:queue:dead").allowRedeliveryWhileStopping(false).maximumRedeliveries(2).redeliveryDelay(5000));
	    
//		onException(Throwable.class).handled(true).logHandled(true);
		
		errorHandler(loggingErrorHandler(LOG_NAME));
		
		from("switchyard://UpdateChunkVersionService")
		.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Get chunk versions for segment: ${body}")
//		.convertBodyTo(String.class)
//		.removeHeader("Accept-Encoding")
//		.setHeader(Exchange.CONTENT_TYPE, constant("text/html"))
//		.setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
//		.setHeader(Exchange.HTTP_CHARACTER_ENCODING, constant("utf-8"))
//		.setHeader(Exchange.CONTENT_ENCODING, constant("deflate"))
//		.setProperty(Exchange.CHARSET_NAME, "iso-8859-1");
		.bean(ChunkUtils.class, "getVersionSoapRequest(${body})")
		.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Send chunk versions request: ${body}")
		.to("switchyard://EshopWS?operationName=getVersions")
		
//		.convertBodyTo(String.class)
//		.to("log:"+LOG_NAME+"?level=INFO&showAll=true&multiline=true");
		.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Response for a GetVersions request: ${body}");
	}

}
