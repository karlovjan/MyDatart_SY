package cz.datart.jboss.myDatart.eshop.notification;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import net.sf.saxon.xpath.XPathFactoryImpl;


public class EnvelopeEshopUpdateResult {

	private final Logger log = Logger.getLogger(EnvelopeEshopUpdateResult.class);
	
//	private final TransformerFactory fact = new net.sf.saxon.TransformerFactoryImpl();
			
	public String getResult(String body){
		
		try{
	        Document dom = convertStringToDocument(body);

	        XPathFactoryImpl factory = new XPathFactoryImpl();
	      
	        XPath xpath = factory.newXPath();
	        XPathExpression expr = xpath.compile("//*[matches(local-name(), 'SendDetail[a-zA-Z]+Result')][1]/text()");

	        Object result = expr.evaluate(dom, XPathConstants.STRING);
//	        NodeList nodes = (NodeList) result;
//	        Nodes sharped = new Nodes(nodes);
//
//	        for (Node n:sharped){
//	            System.out.println(n.toString());
//	        }
	        
	        return (String) result;
	    }
	    catch(Exception e){
	        log.error(e.getMessage(), e);
	    }
		
		return null;
	}
	
	private Document convertStringToDocument(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
        DocumentBuilder builder;  
        try 
        {  
            builder = factory.newDocumentBuilder();  
            Document doc = builder.parse( new InputSource( new StringReader( xmlStr ) ) ); 
            return doc;
        } catch (Exception e) {  
            e.printStackTrace();  
        } 
        return null;
    }
}
