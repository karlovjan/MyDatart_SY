package cz.datart.jboss.myDatart.erpUpdate;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

public class BambinoErpUpdateRoute extends RouteBuilder {

	private static final String LOG_NAME = "BambinoErpUpdateRoute";

	/**
	 * The Camel route is configured via this method.  The from endpoint is required to be a SwitchYard service.
	 */
	public void configure() {
		
//		onException(Throwable.class).handled(true).process(new Processor() {
//			
//			@Override
//			public void process(Exchange exchange) throws Exception {
//				
//				FaultResponse faultResponse = new FaultResponse();
//				
//				Exception ex = exchange.getException();
//				String faultMessage = ex != null ? ex.getMessage() : "Unknown error";
//				
//				exchange.getIn().setBody(faultResponse.getErpUpdateFaultResponse(faultMessage));
//				
//			}
//		});
		
		from("switchyard://BambinoErpUpdateService")
		.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Message received from BambinoErpUpdateService:   ${body}")
//		.log(LoggingLevel.INFO, LOG_NAME, "Headers: ${in.headers}")
		.choice()
			.when(simple("${in.headers[org.switchyard.soap.messageName]} == 'updateUserRequest'"))
				.log(LoggingLevel.INFO, LOG_NAME, "route updateUserRequest")
//				.setHeader("erpUpdateActionName", constant("updateUser"))
				.to("switchyard://ErpUpdateTransformService?operationName=updateUser")
			.when(simple("${in.headers[org.switchyard.soap.messageName]} == 'updateOrderRequest'"))
				.log(LoggingLevel.INFO, LOG_NAME, "route updateOrderRequest")
//				.setHeader("erpUpdateActionName", constant("updateOrder"))
				.to("switchyard://ErpUpdateTransformService?operationName=updateOrder")
			.when(simple("${in.headers[org.switchyard.soap.messageName]} == 'orderOwnerChangeRequest'"))
				.log(LoggingLevel.INFO, LOG_NAME, "route changeOrderOwnerRequest")
//				.setHeader("erpUpdateActionName", constant("changeOrderOwner"))
				.to("switchyard://ErpUpdateTransformService?operationName=changeOrderOwner")
			.when(simple("${in.headers[org.switchyard.soap.messageName]} == 'updateComplaintRequest'"))
				.log(LoggingLevel.INFO, LOG_NAME, "route updateComplaintRequest")
//				.setHeader("erpUpdateActionName", constant("updateComplaint"))
				.to("switchyard://ErpUpdateTransformService?operationName=updateComplaint")
			.when(simple("${in.headers[org.switchyard.soap.messageName]} == 'changeComplaintStateRequest'"))
				.log(LoggingLevel.INFO, LOG_NAME, "route changeComplaintStateRequest")
//				.setHeader("erpUpdateActionName", constant("changeComplaintState"))
				.to("switchyard://ErpUpdateTransformService?operationName=changeComplaintState")
			.otherwise()
				.setBody(constant("Operation ${in.headers[org.switchyard.soap.messageName]} is unimplemented!"))
				.log(LoggingLevel.WARN, LOG_NAME, "Unimplemented request: ${body}")
				.bean(FaultResponse.class, "getErpUpdateUnknownOperationFaultResponse(${body})")
				.end();
	}
	
	//.to("switchyard://ErpUpdateTransformationRef?operationName=updateUser")
	
//	public void configure() {
//		// TODO Auto-generated method stub
//		from("switchyard://BambinoErpUpdate").process(new Processor() {
//			
//			@Override
//			public void process(Exchange exchange) throws Exception {
//				
//				System.out.println("Process erp update");
//				Message msg = exchange.getIn();
//				
//				Map<String, Object> headers = msg.getHeaders();
//				
//				@SuppressWarnings("unchecked")
//				String body = (String) msg.getBody();
//				
//				msg.setBody("<string>Mira</string>");
//				
//			}
//		}).to("switchyard://ErpUpdateUserReference?operationName=sayHello");
//	}
	
	/*
	 * .process(new Processor() {
					
						@Override
						public void process(Exchange exchange) throws Exception {
							
							System.out.println("Process erp update");
							Message msg = exchange.getIn();
							
//							Map<String, Object> headers = msg.getHeaders();
							
							@SuppressWarnings("unchecked")
							String body = (String) msg.getBody();
							
							StringBuffer sb = new StringBuffer();
							sb.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:vid=\"http://ws.mydatart.axapta.datart.cz\">");
							sb.append("<soapenv:Header/>");
							sb.append("<soapenv:Body>");
							sb.append("<vid:ReceiveDetailCust>");
							sb.append("<vid:xmlInput>");
							sb.append("<![CDATA[");
							sb.append(body);
							sb.append("]]>");
							sb.append("</vid:xmlInput>");
							sb.append("</vid:ReceiveDetailCust>");
							sb.append("</soapenv:Body>");
							sb.append("</soapenv:Envelope>");
							
							msg.setBody(sb.toString());
							
						}
					})
	 */
}
