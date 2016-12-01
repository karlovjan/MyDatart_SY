package cz.datart.jboss.myDatart.eshop.notification;

public class NotificationResponseBuilder {


	public String getNotifyOrderResponse() {
		
		StringBuffer sb = new StringBuffer();

		sb.append("<myd:notifyOrderResponse xmlns:myd=\"http://myDatart.doxologic.com/\">");
		sb.append("<code>ok</code>");
		sb.append("</myd:notifyOrderResponse>");

		return sb.toString();
		
	}
	
	public String getNotifyUserResponse() {
		
		StringBuffer sb = new StringBuffer();

		sb.append("<myd:notifyUserResponse xmlns:myd=\"http://myDatart.doxologic.com/\">");
		sb.append("<code>ok</code>");
		sb.append("</myd:notifyUserResponse>");

		return sb.toString();
		
	}
	
	public String getNotifyManualResponse() {
		
		StringBuffer sb = new StringBuffer();

		sb.append("<myd:notifyManualResponse xmlns:myd=\"http://myDatart.doxologic.com/\">");
		sb.append("<code>ok</code>");
		sb.append("</myd:notifyManualResponse>");

		return sb.toString();
		
	}

	public String getNotifyOfferResponse() {
	
		StringBuffer sb = new StringBuffer();
	
		sb.append("<myd:notifyOfferResponse xmlns:myd=\"http://myDatart.doxologic.com/\">");
		sb.append("<code>ok</code>");
		sb.append("</myd:notifyOfferResponse>");
	
		return sb.toString();
		
	}

	public String getNotifyComplaintResponse() {
		
		StringBuffer sb = new StringBuffer();

		sb.append("<myd:updateComplaintResponse xmlns:myd=\"http://myDatart.doxologic.com/\">");
		sb.append("<code>ok</code>");
		sb.append("</myd:updateComplaintResponse>");

		return sb.toString();
		
	}
	
	public String getNotifyUnknownOperationFaultResponse(String faultMessage) {
		
		StringBuffer sb = new StringBuffer();

		sb.append("<soapenv:Fault xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">");
		sb.append("<faultcode>");
		sb.append("soapenv:UnimplementedRequest");
		sb.append("</faultcode>");
		sb.append("<faultstring>");
		sb.append(faultMessage);
		sb.append("</faultstring>");
		sb.append("<detail>");
		sb.append("<myd:WSDeliveryException xmlns:myd=\"http://myDatart.doxologic.com/\">");
		sb.append("<message>");
		sb.append(faultMessage);
		sb.append("</message>");
		sb.append("</myd:WSDeliveryException>");
		sb.append("</detail>");
		sb.append("</soapenv:Fault>");

		return sb.toString();
		
	}
}
