package cz.datart.jboss.myDatart.eshop.notification;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;


public class AxaptaNotificationServiceRoute extends RouteBuilder {

	private static final String LOG_NAME = "AxaNotifyServiceRoute";
	
	/**
	 * The Camel route is configured via this method.  The from endpoint is required to be a SwitchYard service.
	 */
	public void configure() {
		
		from("switchyard://AxaNotificationServiceEndpoint")
			.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Message received from AxaNotificationServiceEndpoint: ${body}")
			.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | Headers: ${in.headers}")
			.choice()
				.when(simple("${in.headers[org.switchyard.soap.messageName]} == 'AxaAdviceEndpoint_notifyOrder'"))
					.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | route notifyOrderRequest")
					.inOnly("switchyard://OrderQueueRef")
					.bean(NotificationResponseBuilder.class, "getNotifyOrderResponse")
				.when(simple("${in.headers[org.switchyard.soap.messageName]} == 'AxaAdviceEndpoint_notifyUser'"))
					.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | route notifyUserRequest")
					.inOnly("switchyard://UserQueueRef")
					.bean(NotificationResponseBuilder.class, "getNotifyUserResponse")
				.when(simple("${in.headers[org.switchyard.soap.messageName]} == 'AxaAdviceEndpoint_notifyManual'"))
					.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | route notifyManualRequest")
					.inOnly("switchyard://ManualQueueRef")
					.bean(NotificationResponseBuilder.class, "getNotifyManualResponse")
				.when(simple("${in.headers[org.switchyard.soap.messageName]} == 'AxaAdviceEndpoint_notifyOffer'"))
					.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | route notifyOfferRequest")
					.inOnly("switchyard://OfferQueueRef")
					.bean(NotificationResponseBuilder.class, "getNotifyOfferResponse")
				.when(simple("${in.headers[org.switchyard.soap.messageName]} == 'AxaAdviceEndpoint_updateComplaint'"))
					.log(LoggingLevel.INFO, LOG_NAME, "ID: ${id} | route notifyComplaintRequest")
					.inOnly("switchyard://ComplaintQueueRef")
					.bean(NotificationResponseBuilder.class, "getNotifyComplaintResponse")
				.otherwise()
					.setBody(constant("Operation ${in.headers[org.switchyard.soap.messageName]} is unimplemented!"))
					.log(LoggingLevel.WARN, LOG_NAME, "ID: ${id} | Unimplemented request: ${body}")
					.bean(NotificationResponseBuilder.class, "getNotifyUnknownOperationFaultResponse(${body})");
					;
	}

}
