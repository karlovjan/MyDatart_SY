package cz.datart.jboss.myDatart.erpUpdate;

public class FaultResponse {

	public String getErpUpdateUnknownOperationFaultResponse(String faultMessage) {
		
		StringBuffer sb = new StringBuffer();

		sb.append("<soapenv:Fault xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">");
		sb.append("<faultcode>");
		sb.append("soapenv:UnknownRequest");
		sb.append("</faultcode>");
		sb.append("<faultstring>");
		sb.append(faultMessage);
		sb.append("</faultstring>");
		sb.append("</soapenv:Fault>");

		return sb.toString();
		
	}
	
	public String getErpUpdateFaultResponse(String faultMessage) {
		
		StringBuffer sb = new StringBuffer();

		sb.append("<soapenv:Fault xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">");
		sb.append("<faultcode>");
		sb.append("soapenv:GeneralError");
		sb.append("</faultcode>");
		sb.append("<faultstring>");
		sb.append(faultMessage);
		sb.append("</faultstring>");
		sb.append("</soapenv:Fault>");

		return sb.toString();
		
	}

}
