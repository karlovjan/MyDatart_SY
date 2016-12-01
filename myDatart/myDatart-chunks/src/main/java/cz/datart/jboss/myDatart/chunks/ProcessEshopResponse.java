package cz.datart.jboss.myDatart.chunks;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

import cz.datart.jboss.myDatart.chunks.exception.ReferentialIntegrityException;
import cz.datart.jboss.myDatart.chunks.exception.SoapFaultException;

public class ProcessEshopResponse extends RouteBuilder {

	private static final String LOG_NAME = "ProcessEshopResponse";
	
	/**
	 * The Camel route is configured via this method.  The from endpoint is required to be a SwitchYard service.
	 */
	public void configure() {
		
		// TODO Auto-generated method stub
		from("switchyard://EshopResponseService")
		.errorHandler(noErrorHandler())
		
//		.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | ${property.chunkName}")
		
		.setProperty("chunkOperationName", simple("update${property.chunkName}"))
		
		.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | ${property.chunkOperationName}")
		
		.choice()
		//regex &#39;\\d{4}&#39;   ${body} contains 'updateProducersRequest'
//			.when(or( body().isNull(), body().isEqualTo(""), not(body().startsWith("<"))))
				
//			.when(simple("${body} contains 'updateProducersResponse'"))
//			.when(simple("${body} regex ${property.chunkName}"))
			.when(xpath("boolean(//*[local-name() = function:simple('update${property.chunkName}Response')])"))
				.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Eshop response is OK: ${body}")
				
				.setProperty("isError", constant("false"))
				.setProperty("eshopError", constant(""))
				//nova verze zustane nezmenena
				.to("switchyard://NotifyAxaptaService")
							
				.stop()
				
			.when(xpath("boolean(//*[local-name() = 'referentialIntegrityError'])"))
				.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Eshop response is referentialIntegrityError: ${body}")
				.throwException(new ReferentialIntegrityException("Resend the chunk"))
				.stop()
			.when(xpath("boolean(//*[local-name() = 'DataVersionMismatch'])"))
				.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Eshop response is DataVersionMismatch: ${body}")
				//delete version for the chunk
				.bean(ChunkVersionStorage.class, "removeVersion(${property.chunkName})")
				.setProperty("isError", constant("true"))
				.setProperty("eshopError").xpath("//*[local-name() = 'errorText'][1]/text()")
//				.throwException(new DataVersionMismatchException("My DataVersionMismatchException: ${body}")) nema smysl opakovat volani eshopu,, rovnou poslat chybu axapte
				.stop()
			.when(xpath("boolean(//*[local-name() = 'requestError'])"))
				.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Eshop response is requestError: ${body}")
				.setProperty("isError", constant("true"))
//				.setProperty("eshopError", constant("requestError"))
				.setProperty("eshopError").xpath("//*[local-name() = 'errorText'][1]/text()")
				.to("switchyard://NotifyAxaptaService")
				.stop()
			.when(xpath("boolean(//*[local-name() = 'Fault'])"))
				.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Eshop response is the Fault exception: ${body}")
				.throwException(new SoapFaultException("Test my soap fault exception on eshop response"))
//				.to("log:"+LOG_NAME+"?level=INFO&showAll=true&multiline=true")
//				.setProperty("isError", constant("true"))
//				.setProperty("eshopError", constant("FAULT"))
//				.setProperty("eshopError").xpath("concat(//*[local-name() = 'Fault']//faultcode[position() = 1]/text(), ',',//*[local-name() = 'Fault']//faultcode[position() = 2]/text(), ',',//*[local-name() = 'Fault']//faultstring/text())")
//				.to("switchyard://NotifyAxaptaService")
				.stop()
			.otherwise()
			//commonError
				.log(LoggingLevel.WARN, LOG_NAME, "ID: ${id} | an unexpected error: ${body}")
				.setProperty("isError", constant("true"))
//				.setProperty("eshopError", constant("commonError"))
				.setProperty("eshopError").xpath("//*[local-name() = 'errorText'][1]/text()")
				.to("switchyard://NotifyAxaptaService")
			
		.endChoice();
	}

}
