package cz.datart.jboss.myDatart.chunks;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

import cz.datart.jboss.myDatart.chunks.exception.EmptyResponseException;
import cz.datart.jboss.myDatart.chunks.exception.SoapFaultException;
import cz.datart.jboss.myDatart.utils.BodyTransformer;

public class ProcessAxaptaNotificationResponse extends RouteBuilder {

	private static final String LOG_NAME = "ProcessAxaptaNotificationResponse";
	
	/**
	 * The Camel route is configured via this method.  The from endpoint is required to be a SwitchYard service.
	 */
	public void configure() {
		//header http_response_status=500 when Fault comes
		
		from("switchyard://ProcessAxaptaNotificationResponseService")
		.errorHandler(noErrorHandler())
		
		.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Axapta notification response is ${body}")
		
		.choice()
		  .when().method(BodyTransformer.class, "isEmptyBody")
		  	.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Axapta notification response is empty for the chunk ${property.chunkName}")
		  	.throwException(new EmptyResponseException("Empty response"))
		  	.stop()
		  .when().xpath("boolean(//*[local-name() = 'ConfirmTransResponse'])")
		  	.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Axapta notification response is OK for the chunk ${property.chunkName}")
		  	.stop()
		  .when(xpath("boolean(//*[local-name() = 'Fault'])"))
			.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Axapta notification response is the Fault exception for the chunk ${property.chunkName}")
			.throwException(new SoapFaultException("Test my soap exception"))
			//			.throwException(new SoapFaultException(method(ChunkUtils.class, "getBody").))
			//			.to("log:"+LOG_NAME+"?level=INFO&showAll=true&multiline=true")	
			.stop()
		  .otherwise()
			.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Unspecified Axapta notification response for the chunk ${property.chunkName}")
			.to("log:"+LOG_NAME+"?level=INFO&showAll=true&multiline=true")	
		.endChoice();
	}

}
