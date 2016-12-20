package cz.datart.jboss.myDatart.chunks;

import java.util.List;
import java.util.stream.Collectors;
import cz.datart.jboss.myDatart.utils.BodyTransformer;

public class ChunkUtils {

	/**
	 // using bean binding we can bind the information from the exchange to the types we have in our method signature
    public boolean retry(@Header(Exchange.REDELIVERY_COUNTER) Integer counter, @Body String body, @ExchangeException Exception causedBy) {
        // NOTE: counter is the redelivery attempt, will start from 1
        invoked++;
 
        assertEquals("Hello World", body);
        assertTrue(causedBy instanceof MyFunctionalException);
 
        // we can of course do what ever we want to determine the result but this is a unit test so we end after 3 attempts
        return counter < 3;
    }
    
    */
    
	public String createListXML(final List<String> items) {
		
		
		return items.stream().collect(Collectors.joining("", "<list>", "</list>"));	
	}
	

	public String createSoapMessage(String chunkName, String listXML, String scopeSegment, String wsNamespace, String oldVersion, String newVersion) {
		
		StringBuffer soapMsg = new StringBuffer();
		
		soapMsg.append("<bam:update").append(chunkName).append(" xmlns:bam=\"" + wsNamespace + "\">");
		soapMsg.append("<scope>");
		soapMsg.append("<segment>").append(scopeSegment).append("</segment>");
		soapMsg.append("<oldVersion>").append(oldVersion).append("</oldVersion>");
		soapMsg.append("<newVersion>").append(newVersion).append("</newVersion>");
		soapMsg.append("</scope>");
		soapMsg.append(listXML);
		soapMsg.append("</bam:update").append(chunkName).append(">");
		
		return soapMsg.toString();
	}


	public String getNewVersion(final String oldVersion, final int chunkSize) {
		
		if(oldVersion == null || oldVersion.trim().isEmpty() || chunkSize < 1){
			return null;
		}
		
		String newVersion;
		long oldVersionInt;
		
		try {
			oldVersionInt = Long.parseUnsignedLong(oldVersion);
			
			newVersion = new Long(oldVersionInt + chunkSize).toString();
			
		} catch (Exception e) {
			newVersion = null;
		}
		
		return newVersion;
	}


	public String getVersionSoapRequest(final String scopeSegment) {
		
		final String eshopWSNamespace = "http://etnetera.com/projects/datart/bambino";
		
		StringBuffer soap = new StringBuffer();
		
		soap
		.append("<bam:getVersions").append(" xmlns:bam=\"" + eshopWSNamespace + "\">")
		.append("<segment>")
		.append(scopeSegment)
		.append("</segment>")
		.append("</bam:getVersions>");
		
		return soap.toString();
	}
	
	public String getChunkNameFromRequestMessage(final String body){
		
		if(body != null){
			
			if(body.length() > "<bam:update".length()){
				
				return body.substring("<bam:update".length(), body.indexOf(" "));
			}
			
			return "NOMethodName";
		}
		
		return null;
	}
	
	private String createChunkNotificationRequest(final String flowName, final String newVersion, final String notifications) {
        
        StringBuilder sb = new StringBuilder();
        sb.append("<notifications>");
        sb.append("<entity>");
        sb.append(flowName);
        sb.append("</entity>");
        sb.append("<newVersion>");
        sb.append(newVersion);
        sb.append("</newVersion>");
        sb.append(notifications);
        sb.append("</notifications>");

        return sb.toString();
    }
	
	public String convertXMLBodyIntoEntityString(final String body) {
        
        StringBuilder sb = new StringBuilder();
        
        sb.append("<![CDATA[");
        sb.append(body);
        sb.append("]]>");
        //nahrazeno, problem s transformaci
//        sb.append(body.replace("<", "&lt;").replace(">", "&gt;").replace("\n", "").replaceAll(" +", " "));

        return sb.toString();
    }
	
	public String createConfirmTransAxaptaRequest(final String flowName, final String newVersion, final String notifications){
		
		StringBuffer soap = new StringBuffer();
		
		soap
		.append("<vid:ConfirmTrans").append(" xmlns:vid=\"http://ws.mydatart.axapta.datart.cz\">")
		.append("<vid:xmlInput>")
		.append(new BodyTransformer().embodyXmlWithCDATA(createChunkNotificationRequest(flowName, newVersion, notifications)))
		.append("</vid:xmlInput>")
		.append("</vid:ConfirmTrans>");
		
		return soap.toString();
	}
	
	public static String getChunkQueueName(final String chunkName, final String environment, final String scopeSegment){
		
		return new StringBuilder()
				.append("myDatart")
				.append("-")
				.append("ChunkUpdate")
				.append(chunkName)
				.append("Queue")
				.append("-")
				.append(environment)
				.append(scopeSegment).toString();
	}
	
	
}
