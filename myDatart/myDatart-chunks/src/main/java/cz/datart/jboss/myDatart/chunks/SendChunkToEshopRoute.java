package cz.datart.jboss.myDatart.chunks;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

public class SendChunkToEshopRoute extends RouteBuilder {

	private static final String LOG_NAME = "SendChunkToEshopRoute";
	
	
//	@EJB
//	private ChunkVersionStorage versionStorage;
	
	/**
	 * The Camel route is configured via this method.  The from endpoint is required to be a SwitchYard service.
	 */
	public void configure() {
		
		
		from("switchyard://SendChunkToEshopService")
		.errorHandler(noErrorHandler())
			
			
			.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Send chunk  ${property.chunkName} to eshop")
			
//			.removeHeader("Accept-Encoding")
			
//			.to("log:"+LOG_NAME+"?level=INFO&showAll=true&multiline=true")
			
//			.removeProperty("recIDs")
//			.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Headers: ${in.headers}")
			
//				.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Operation name is ${property.chunkOperationName}")
				
//					.setProperty("eshopURL", constant("switchyard://EshopWS?operationName=${property.chunkOperationName}"))
					
//				.setProperty("eshopURL", constant("switchyard://EshopWS?operationName=updateProducers"))
					//send update to eshop
//					.to("switchyard://EshopWS?operationName=updateProducers")
//					.to("${property.eshopURL}")
			.to("switchyard://EshopWS")
//					.recipientList(simple("${property.eshopURL}"), "false")
				
					.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | update ${property.chunkName} chunk eshop response: ${body}")
		
					.to("switchyard://EshopResponseService");
//					.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Get recids from eshop response")
//					
//					.to("xslt:xsl/getRecId.xsl?saxon=true");
//			.otherwise()
//				.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Unknown chunk type");
			
					
					
	}

}
