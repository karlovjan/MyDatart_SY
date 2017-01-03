package cz.datart.jboss.myDatart.utils;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlFileResolver {

	private Logger log = Logger.getLogger(XmlFileResolver.class);
	
	private String xmlFileName;
	private Document xmlDoc = null;

	public XmlFileResolver(String fileName) {
		this.xmlFileName = fileName;
	}
	
	public String getXmlFileName() {
		return xmlFileName;
	}
	
	private Document parseXmlDocument(InputStream xmlFile) throws ParserConfigurationException, SAXException, IOException{
		
		// create a new DocumentBuilderFactory
	      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

	         // use the factory to create a documentbuilder
	         DocumentBuilder builder = factory.newDocumentBuilder();

	         // create a new document from input stream
//	         FileInputStream fis = new FileInputStream("Student.xml");
	         Document doc = builder.parse(xmlFile);
	         
	         return doc;
	     
	}

	private Document getXmlDocument() throws IOException, ParserConfigurationException, SAXException{
		
		if(this.xmlDoc != null){
			return this.xmlDoc;
		}
		
		return createXmlDocument();
	}
	
	private Document createXmlDocument() throws IOException, ParserConfigurationException, SAXException {
		
		try (InputStream fileInStream = XmlFileResolver.class.getClassLoader().getResourceAsStream(getXmlFileName())) {
//          properties.load(new FileInputStream(file));
			return parseXmlDocument(fileInStream);
		}
	}
	
	public String getAttribute(String element, String attributeName, String attributeValueStartsWith){
		
		try {
			Document doc = getXmlDocument();
			Element puElement;
			
			if(attributeValueStartsWith == null){
				puElement = (Element) doc.getElementsByTagName(element).item(0);
				return puElement.getAttribute("name");
			} else {
				//find element by attribute start value
				return findElementAttributeValue(element, attributeValueStartsWith, doc);
			}
			
		} catch (Exception e) {
			log.error(String.format("We cannot find attribute %s for element %s", attributeName, element), e);
			
		} 
		
		return null;
	}

	private String findElementAttributeValue(String element, String attributeValueStartsWith, Document doc) {
		Element puElement;
		NodeList persistenceUnits = doc.getElementsByTagName(element);
		String puName;
		for (int i = 0; i < persistenceUnits.getLength(); i++) {
			puElement = (Element) doc.getElementsByTagName(element).item(i);
			puName = puElement.getAttribute("name");
			
			if(puName != null){
				if(puName.startsWith(attributeValueStartsWith)){
					return puName;
				}
			}
		}
		
		return null;
	}
}
