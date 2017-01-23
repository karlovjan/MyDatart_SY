package cz.datart.jboss.myDatart.utils;

public class BodyTransformer {

	public boolean isEmptyBody(String body){
		
		return body == null || body.trim().isEmpty();
	}
	
	public boolean notEmptyBody(String body){
		
		return body != null && !body.trim().isEmpty();
	}
	
	public String convertXmlStringToXML(String body){
		
		if(body == null || body.isEmpty() ){
			return null;
		}
		
		StringBuffer sb = new StringBuffer();
		//remove all new lines by this replaceAll("(\\r|\\n|\\r\\n)+","")
        sb.append(body.replaceAll("&lt;", "<").replaceAll("&gt;", ">"));
        
        return sb.toString();
	}
	
	public String embodyXmlWithCDATA(String body){
		
		if(body == null ){
			return null;
		}
		
		StringBuffer sb = new StringBuffer();
		
        sb.append("<![CDATA[");
        sb.append(body);
        sb.append("]]>");
        
        return sb.toString();
	}

	/**
	 * 
	 *
	 */
    public String convertXmlBodyToString(String xmlBody) {
       
        StringBuilder sb = new StringBuilder();
        
        //nahrazeno, problem s transformaci
        sb.append(xmlBody.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "").replaceAll(" +", " "));
        
        return sb.toString();
    }
}
