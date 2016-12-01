package cz.datart.jboss.myDatart.chunks;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;



public class EshopChunkUpdateRoute extends RouteBuilder {

	private static final String LOG_NAME = "EshopChunkUpdateRoute";
	
	/**
	 * The Camel route is configured via this method.  The from endpoint is required to be a SwitchYard service.
	 */
	public void configure() {
		
		from("switchyard://EshopChunkUpdateService")
		.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Message received from EshopChunkUpdateService: ${body}")
//		.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Headers: ${in.headers}")
		.choice()
			.when(simple("${in.headers[org.switchyard.soap.messageName]} == 'updateProducersRequest'"))
				.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | route updateProducersRequest")
				.setHeader("CamelJmsDestinationName").constant("ChunkUpdateProducersQueue")
				.inOnly("switchyard://DissassembleProducersService")
				.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | send updateProducersResponse")
				.bean(ChunkUpdateResponse.class, "updateProducersResponse")
				.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | response is ${body}")
				
			.when(simple("${in.headers[org.switchyard.soap.messageName]} == 'updateCategoriesRequest'"))
				.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | route updateCategoriesRequest")
				.inOnly("switchyard://DissassembleCategoriesService")
				
//				.setBody(constant("<bam:updateCategoriesResponse xmlns:bam=\"http://etnetera.com/projects/datart/bambino\" />"))
				.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | send updateCategoriesResponse")
				.bean(ChunkUpdateResponse.class, "updateCategoriesResponse")
				.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | response is ${body}")
				
			.when(simple("${in.headers[org.switchyard.soap.messageName]} == 'updateAvailabilityRequest'"))
				.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | route updateAvailabilityRequest")
				.inOnly("switchyard://DissassembleAvailabilitiesService")
//				.setBody(constant("<bam:updateAvailabilityResponse xmlns:bam=\"http://etnetera.com/projects/datart/bambino\" />"))
				.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | send updateAvailabilityResponse")
				.bean(ChunkUpdateResponse.class, "updateAvailabilityResponse")
				.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | updateAvailabilityResponse is ${body}")
				
			.when(simple("${in.headers[org.switchyard.soap.messageName]} == 'updatePricesRequest'"))
				.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | route updatePricesRequest")
				.inOnly("switchyard://DissassemblePricesService")
//				.setBody(constant("<bam:updatePricesResponse xmlns:bam=\"http://etnetera.com/projects/datart/bambino\" />"))
				.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | send updatePricesResponse")
				.bean(ChunkUpdateResponse.class, "updatePricesResponse")
				.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | updatePricesResponse is ${body}")
				
			.when(simple("${in.headers[org.switchyard.soap.messageName]} == 'updateAttributesRequest'"))
				.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | route updateAttributesRequest")
				.inOnly("switchyard://DissassembleAttributesService")
//				.setBody(constant("<bam:updateAttributesResponse xmlns:bam=\"http://etnetera.com/projects/datart/bambino\" />"))
				.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | send updateAttributesResponse")
				.bean(ChunkUpdateResponse.class, "updateAttributesResponse")
				.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | updateAttributesResponse is ${body}")
				
			.when(simple("${in.headers[org.switchyard.soap.messageName]} == 'updateAttributeGroupsRequest'"))
				.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | route updateAttributeGroupsRequest")
				.inOnly("switchyard://DissassembleAttributeGroupsService")
//				.setBody(constant("<bam:updateAttributeGroupsResponse xmlns:bam=\"http://etnetera.com/projects/datart/bambino\" />"))
				.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | send updateAttributeGroupsResponse")
				.bean(ChunkUpdateResponse.class, "updateAttributeGroupsResponse")
				.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | updateAttributeGroupsResponse is ${body}")
				
			.when(simple("${in.headers[org.switchyard.soap.messageName]} == 'updateCatalogueRequest'"))
				.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | route updateCatalogueRequest")
				.inOnly("switchyard://DissassembleCataloguesService")
//				.setBody(constant("<bam:updateCatalogueResponse xmlns:bam=\"http://etnetera.com/projects/datart/bambino\" />"))
				.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | send updateCatalogueResponse")
				.bean(ChunkUpdateResponse.class, "updateCatalogueResponse")
				.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | updateCatalogueResponse is ${body}")
				
			.when(simple("${in.headers[org.switchyard.soap.messageName]} == 'updateRelationsRequest'"))
				.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | route updateRelationsRequest")
				.inOnly("switchyard://DissassembleRelationsService")
//				.setBody(constant("<bam:updateRelationsResponse xmlns:bam=\"http://etnetera.com/projects/datart/bambino\" />"))
				.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | send updateRelationsResponse")
				.bean(ChunkUpdateResponse.class, "updateRelationsResponse")
				.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | updateRelationsResponse is ${body}")
				
			.otherwise()
				.setBody(constant("Operation ${in.headers[org.switchyard.soap.messageName]} is unimplemented!"))
				.log(LoggingLevel.WARN, LOG_NAME, "ID: ${id} | Unimplemented request: ${body}")
				
				;
	}

}
