package cz.datart.jboss.myDatart.erpUpdate;

import cz.datart.jboss.myDatart.utils.BodyTransformer;

public class ErpUpdateRequestBuilder {

	public String getReceiveDetailCustRequest(String body) {
		
		StringBuffer sb = new StringBuffer();

		sb.append("<vid:ReceiveDetailCust xmlns:vid=\"http://ws.mydatart.axapta.datart.cz\">");
		sb.append("<vid:xmlInput>");
		sb.append(new BodyTransformer().embodyXmlWithCDATA(body));
		sb.append("</vid:xmlInput>");
		sb.append("</vid:ReceiveDetailCust>");

		return sb.toString();
		
	}

	public String getReceiveDetailOrderRequest(String body) {
		
		StringBuffer sb = new StringBuffer();

		sb.append("<vid:ReceiveDetailOrder xmlns:vid=\"http://ws.mydatart.axapta.datart.cz\">");
		sb.append("<vid:xmlInput>");
		sb.append(new BodyTransformer().embodyXmlWithCDATA(body));
		sb.append("</vid:xmlInput>");
		sb.append("</vid:ReceiveDetailOrder>");

		return sb.toString();
	}
	
	public String getAddUserOrderRequest(String body) {
		
		StringBuffer sb = new StringBuffer();

		sb.append("<vid:AddUserOrder xmlns:vid=\"http://ws.mydatart.axapta.datart.cz\">");
		sb.append("<vid:id>");
		sb.append(new BodyTransformer().embodyXmlWithCDATA(body));
		sb.append("</vid:id>");
		sb.append("</vid:AddUserOrder>");

		return sb.toString();
		
	}

	public String getReceiveDetailComplaintRequest(String body) {
		
		StringBuffer sb = new StringBuffer();

		sb.append("<vid:ReceiveDetailComplaint xmlns:vid=\"http://ws.mydatart.axapta.datart.cz\">");
		sb.append("<vid:xmlInput>");
		sb.append(new BodyTransformer().embodyXmlWithCDATA(body));
		sb.append("</vid:xmlInput>");
		sb.append("</vid:ReceiveDetailComplaint>");

		return sb.toString();
		
	}
	
	public String getChangeComplaintStateRequest(String body) {
		
		StringBuffer sb = new StringBuffer();

		sb.append("<vid:ChangeComplaintState xmlns:vid=\"http://ws.mydatart.axapta.datart.cz\">");
		sb.append("<vid:xmlInput>");
		sb.append(new BodyTransformer().embodyXmlWithCDATA(body));
		sb.append("</vid:xmlInput>");
		sb.append("</vid:ChangeComplaintState>");

		return sb.toString();
		
	}
}
