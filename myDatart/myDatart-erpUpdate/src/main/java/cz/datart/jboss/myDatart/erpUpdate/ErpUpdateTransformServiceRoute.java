package cz.datart.jboss.myDatart.erpUpdate;

import org.apache.camel.LoggingLevel;
import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;

public class ErpUpdateTransformServiceRoute extends RouteBuilder {

	private final String LOG_NAME = "ErpUpdateTransformationServiceRoute";
	
	private Predicate isUpdateUserRequest = header("org.switchyard.soap.messageName").isEqualTo("updateUserRequest");
	private Predicate isUpdateOrderRequest = header("org.switchyard.soap.messageName").isEqualTo("updateOrderRequest");
//	private Predicate isUpdateNeededRequest = header("org.switchyard.soap.messageName").isEqualTo("updateNeeded");
	private Predicate isChangeOrderOwnerRequest = header("org.switchyard.soap.messageName").isEqualTo("orderOwnerChangeRequest");
//	private Predicate isUpdateExpressOrderRequest = header("org.switchyard.soap.messageName").isEqualTo("updateExpressOrder");
	private Predicate isUpdateComplaintRequest = header("org.switchyard.soap.messageName").isEqualTo("updateComplaintRequest");
	private Predicate isChangeComplaintStateRequest = header("org.switchyard.soap.messageName").isEqualTo("changeComplaintStateRequest");
	
	/**
	 * The Camel route is configured via this method.  The from endpoint is required to be a SwitchYard service.
	 */
	public void configure() {
		
//		errorHandler(loggingErrorHandler().logName(LOG_NAME).level(LoggingLevel.ERROR));
//		errorHandler(deadLetterChannel("jms:queue:dead").allowRedeliveryWhileStopping(false).maximumRedeliveries(2).redeliveryDelay(5000));
        
//		errorHandler(deadLetterChannel("seda:generalError").allowRedeliveryWhileStopping(false).maximumRedeliveries(2).redeliveryDelay(5000));
		
//		.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Exception happened: ${body}")
//		.bean(FaultResponse.class, "getErpUpdateFaultResponse(${body})");
		
//		onException(Throwable.class).continued(true);
		
		from("switchyard://ErpUpdateTransformService")
		.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Camel received message from ErpUpdateTransformService: ${body}")
//		.log(LoggingLevel.INFO, LOG_NAME, "Headers: ${in.headers}")
//		.convertBodyTo(String.class)
//		.removeHeader("Accept-Encoding")
		.choice()
		.when(isUpdateUserRequest)
//		.when(simple("${in.headers[erpUpdateActionName]} == 'updateUser'"))
			.log(LoggingLevel.INFO, LOG_NAME, "transform updateUserRequest")
			.bean(ErpUpdateRequestBuilder.class, "getReceiveDetailCustRequest(${body})")
			.log(LoggingLevel.INFO, LOG_NAME, "ReceiveDetailCust request: ${body}")
//			.setHeader("soapAction", constant("http://ws.mydatart.axapta.datart.cz/IMyDatartAxaptaWS/ReceiveDetailCust"))
			
			.to("switchyard://AxaptaWS?operationName=ReceiveDetailCust")
			.log(LoggingLevel.INFO, LOG_NAME, "Axapta response to the ReceiveDetailCust request: ${body}")
			.to("xslt:xsl/eShopUpdateUserResponse.xsl?saxon=true")
//			.setBody(constant("<bam:updateUserResponse xmlns:bam=\"http://etnetera.com/projects/datart/bambino\"/>"))
			.log(LoggingLevel.INFO, LOG_NAME, "Update customer response to eshop: ${body}")
			
		.when(isUpdateOrderRequest)
			.log(LoggingLevel.INFO, LOG_NAME, "transform updateOrderRequest")
			.bean(ErpUpdateRequestBuilder.class, "getReceiveDetailOrderRequest(${body})")
			.log(LoggingLevel.INFO, LOG_NAME, "ReceiveDetailOrder request: ${body}")
//			.setHeader("soapAction", constant("http://ws.mydatart.axapta.datart.cz/IMyDatartAxaptaWS/ReceiveDetailCust"))
			
			.to("switchyard://AxaptaWS?operationName=ReceiveDetailOrder")
			.log(LoggingLevel.INFO, LOG_NAME, "Axapta response to ReceiveDetailOrder request: ${body}")
			.to("xslt:xsl/eShopUpdateOrderResponse.xsl?saxon=true")
//			.setBody(constant("<bam:updateOrderResponse xmlns:bam=\"http://etnetera.com/projects/datart/bambino\"/>"))
			.log(LoggingLevel.INFO, LOG_NAME, "Update order response to eshop: ${body}")
		
		.when(isChangeOrderOwnerRequest)
			.log(LoggingLevel.INFO, LOG_NAME, "transform ChangeOrderOwnerRequest")
			.bean(ErpUpdateRequestBuilder.class, "getAddUserOrderRequest(${body})")
			.log(LoggingLevel.INFO, LOG_NAME, "AddUserOrder request: ${body}")
//			.setHeader("soapAction", constant("http://ws.mydatart.axapta.datart.cz/IMyDatartAxaptaWS/ReceiveDetailCust"))
			
			.to("switchyard://AxaptaWS?operationName=AddUserOrder")
			.log(LoggingLevel.INFO, LOG_NAME, "Axapta response to AddUserOrder request: ${body}")
			.to("xslt:xsl/eShopChangeOrderOwnerResponse.xsl?saxon=true")
//			.setBody(constant("<bam:changeOrderOwnerResponse xmlns:bam=\"http://etnetera.com/projects/datart/bambino\"/>"))
			.log(LoggingLevel.INFO, LOG_NAME, "ChangeOrderOwner response to eshop: ${body}")
			
			
		.when(isUpdateComplaintRequest)
			.log(LoggingLevel.INFO, LOG_NAME, "transform updateComplaintRequest")
			.bean(ErpUpdateRequestBuilder.class, "getReceiveDetailComplaintRequest(${body})")
			.log(LoggingLevel.INFO, LOG_NAME, "ReceiveDetailComplaint request: ${body}")
//			.setHeader("soapAction", constant("http://ws.mydatart.axapta.datart.cz/IMyDatartAxaptaWS/ReceiveDetailCust"))
			
			.to("switchyard://AxaptaWS?operationName=ReceiveDetailComplaint")
			.log(LoggingLevel.INFO, LOG_NAME, "Axapta response to ReceiveDetailComplaint request: ${body}")
			.to("xslt:xsl/eShopUpdateComplaintResponse.xsl?saxon=true")
//			.setBody(constant("<bam:updateOrderResponse xmlns:bam=\"http://etnetera.com/projects/datart/bambino\"/>"))
			.log(LoggingLevel.INFO, LOG_NAME, "Update complaint response to eshop: ${body}")
		
		.when(isChangeComplaintStateRequest)
			.log(LoggingLevel.INFO, LOG_NAME, "transform ChangeComplaintState request")
			.bean(ErpUpdateRequestBuilder.class, "getChangeComplaintStateRequest(${body})")
			.log(LoggingLevel.INFO, LOG_NAME, "ChangeComplaintState request: ${body}")
//			.setHeader("soapAction", constant("http://ws.mydatart.axapta.datart.cz/IMyDatartAxaptaWS/ReceiveDetailCust"))
			
			.to("switchyard://AxaptaWS?operationName=ChangeComplaintState")
			.log(LoggingLevel.INFO, LOG_NAME, "Axapta response to ChangeComplaintState request: ${body}")
			.to("xslt:xsl/eShopChangeComplaintStateResponse.xsl?saxon=true")
//			.setBody(constant("<bam:changeOrderOwnerResponse xmlns:bam=\"http://etnetera.com/projects/datart/bambino\"/>"))
			.log(LoggingLevel.INFO, LOG_NAME, "ChangeComplaintState response to eshop: ${body}")
			
		.otherwise()
			.setBody(constant("Operation ${in.headers[org.switchyard.soap.messageName]} is unimplemented!"))
			.log(LoggingLevel.WARN, LOG_NAME, "Unimplemented operation: ${body}")
			.bean(FaultResponse.class, "getErpUpdateUnknownOperationFaultResponse(${body})")
			.end();
	}

}
