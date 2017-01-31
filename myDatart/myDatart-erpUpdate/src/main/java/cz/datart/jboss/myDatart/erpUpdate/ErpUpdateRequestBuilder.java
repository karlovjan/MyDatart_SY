package cz.datart.jboss.myDatart.erpUpdate;

import cz.datart.jboss.myDatart.utils.BodyTransformer;

public class ErpUpdateRequestBuilder {

	public String getReceiveDetailCustRequest(String body) {
		
		StringBuffer sb = new StringBuffer();

		createStartSoapEnvelope(sb);
		sb.append("<vid:ReceiveDetailCust xmlns:vid=\"http://ws.mydatart.axapta.datart.cz\">");
		sb.append("<vid:xmlInput>");
		sb.append(new BodyTransformer().embodyXmlWithCDATA(body));
		sb.append("</vid:xmlInput>");
		sb.append("</vid:ReceiveDetailCust>");
		createEndSoapEnvelope(sb);

		return sb.toString();
		
	}

	public String getReceiveDetailOrderRequest(String body) {
		
		StringBuffer sb = new StringBuffer();

		createStartSoapEnvelope(sb);
		sb.append("<vid:ReceiveDetailOrder xmlns:vid=\"http://ws.mydatart.axapta.datart.cz\">");
		sb.append("<vid:xmlInput>");
		sb.append(new BodyTransformer().embodyXmlWithCDATA(body));
		sb.append("</vid:xmlInput>");
		sb.append("</vid:ReceiveDetailOrder>");
		createEndSoapEnvelope(sb);

		return sb.toString();
	}

	private void createEndSoapEnvelope(StringBuffer sb) {
		sb.append("</soapenv:Body></soapenv:Envelope>");
	}

	private void createStartSoapEnvelope(StringBuffer sb) {
		sb.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">");
		sb.append("<soapenv:Header/><soapenv:Body>");
	}
	
	public String getAddUserOrderRequest(String body) {
		
		StringBuffer sb = new StringBuffer();

		createStartSoapEnvelope(sb);
		sb.append("<vid:AddUserOrder xmlns:vid=\"http://ws.mydatart.axapta.datart.cz\">");
		sb.append("<vid:id>");
		sb.append(new BodyTransformer().embodyXmlWithCDATA(body));
		sb.append("</vid:id>");
		sb.append("</vid:AddUserOrder>");
		createEndSoapEnvelope(sb);

		return sb.toString();
		
	}

	public String getReceiveDetailComplaintRequest(String body) {
		
		StringBuffer sb = new StringBuffer();

		createStartSoapEnvelope(sb);
		sb.append("<vid:ReceiveDetailComplaint xmlns:vid=\"http://ws.mydatart.axapta.datart.cz\">");
		sb.append("<vid:xmlInput>");
		sb.append(new BodyTransformer().embodyXmlWithCDATA(body));
		sb.append("</vid:xmlInput>");
		sb.append("</vid:ReceiveDetailComplaint>");
		createEndSoapEnvelope(sb);

		return sb.toString();
		
	}
	
	public String getChangeComplaintStateRequest(String body) {
		
		StringBuffer sb = new StringBuffer();

		createStartSoapEnvelope(sb);
		sb.append("<vid:ChangeComplaintState xmlns:vid=\"http://ws.mydatart.axapta.datart.cz\">");
		sb.append("<vid:xmlInput>");
		sb.append(new BodyTransformer().embodyXmlWithCDATA(body));
		sb.append("</vid:xmlInput>");
		sb.append("</vid:ChangeComplaintState>");
		createEndSoapEnvelope(sb);

		return sb.toString();
		
	}
}
