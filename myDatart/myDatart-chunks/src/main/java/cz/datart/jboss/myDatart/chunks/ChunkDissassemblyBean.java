package cz.datart.jboss.myDatart.chunks;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ChunkDissassemblyBean implements ChunkDissassembly {

	private final Logger log = Logger.getLogger(ChunkDissassemblyBean.class);
	
	private static final String ENTITY_PREFIX = "e";
	
	private XMLInputFactory inputFactory = XMLInputFactory.newInstance();
	
	private DocumentBuilder documentBuilder;
	
	public ChunkDissassemblyBean() {
		try {
			documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			log.error(e.getMessage(), e);
		}
	}
	
	@Override
	public synchronized List<String> dissassemble(String xml, String entityName) throws Throwable {
		
		
		return dissassamblyXMLItems(xml, entityName);
	}
	
	public synchronized void dissassemble(DissassembledItemJMSService jms, String xml, ChunkItems entity) {
		
		log.info("start dissassembling of the entity " + entity.toString());
		
		final String entityName = entity.toString();
		
		List<String> dissassembledItems;
		try {
			dissassembledItems = dissassemble(xml, ENTITY_PREFIX + entityName);
		} catch (Throwable e) {
			log.error("Dissassemble " + entityName + " failed", e);
			dissassembledItems = new ArrayList<String>(0);
		}
		
		for (String item : dissassembledItems) {
			try{
//				log.info("store to the queue");
				jms.process(item);
			} catch (Exception ex){
				log.error(item);
				log.error("Store to the " + entityName + " queue Error", ex);
			}
		}
	}

	public synchronized List<String> disassemble(StoreDisassembledItemsJMSService jms, String xml, ChunkItems entity){

		log.info("start dissassembling of the entity " + entity.toString());
		
		final String entityName = entity.toString();
		
		List<String> disassembledItems;
		try {
			disassembledItems = dissassemble(xml, ENTITY_PREFIX + entityName);
		} catch (Throwable e) {
			log.error("Dissassemble " + entityName + " failed", e);
			disassembledItems = new ArrayList<String>(0);
		}
		
		return disassembledItems;
	}
	
//	protected String getXMLItems(String soapMessage){
		
//		DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//
//		Document document = documentBuilder.parse(soapMessage);
//
//
//		if (document.hasChildNodes()) {
//
//			document.getElementsByTagName("")
//
//		}
//	}
	
	protected List<String> dissassamblyXMLItems(String xml, String elementName) throws ParserConfigurationException, SAXException, IOException, XMLStreamException {
		
//		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		InputSource src = new InputSource();
		src.setCharacterStream(new StringReader(xml));

		Document doc = documentBuilder.parse(src);
		
		//doc.getElementsByTagName(elementName).item(0).getTextContent();
		NodeList eElements = doc.getElementsByTagName("xml");
		
		String xmlElementText = eElements.item(0).getTextContent();
		
//		List<String> chunks = new ArrayList<String>();
//		
//		
//		src.setCharacterStream(new StringReader(xmlElementText));
//
//		Document docEnities = builder.parse(src);
//		
//		
//		eElements = doc.getElementsByTagName("producers");
//		
//		
//		for (int i = 0; i != eElements.getLength(); i++) {
//		
//			chunks.add(eElements.item(i).getTextContent());
//			
//		}
		
		return dissassamblyXMLItems_EventReader(xmlElementText);
	}
	
	protected List<String> dissassamblyXMLItems_EventReader(String xml) throws XMLStreamException {

		
		XMLEventReader xmlEventReader = inputFactory.createXMLEventReader(new ByteArrayInputStream(xml.getBytes()));

		List<String> chunks = new ArrayList<String>();
		int level = 0;
		StringWriter writer = null;
		while (xmlEventReader.hasNext()) {
		    XMLEvent xmlEvent = xmlEventReader.nextEvent();
		
		    switch (xmlEvent.getEventType()) {
		        case XMLEvent.START_ELEMENT :
		            level++;
		        
		            if (level == 2) {
		                if (writer != null) {
		                    if (log.isDebugEnabled()) {
		                        log.debug("writer is not null and has content: " + writer.toString());
		                    }
		
		                    try {
		                        writer.close();
		                    } catch (IOException e) {
		                        log.error("close writer", e);
		                    }
		                }
		                
		                writer = new StringWriter();
		            }
		
		            if (level > 1) {
		                xmlEvent.writeAsEncodedUnicode(writer);
		            }
		
		            break;
		
		        case XMLEvent.END_ELEMENT :
		            level--;
		            
		            if (level > 0) {
		                xmlEvent.writeAsEncodedUnicode(writer);
		            }
		            
		            if (level == 1) {
		                if (writer != null) {
		                    String chunk = writer.toString();
		
		                    chunks.add(chunk);
		                }
		                
		                else {
		                    log.error("writer has been null!");
		                }
		            }
		
		            break;
		        
		        default:
		            if (writer != null) {
		                xmlEvent.writeAsEncodedUnicode(writer);
		            }
		
		            break;
		    }
		}
		
		xmlEventReader.close();
		
		return chunks;
	}
	
	protected synchronized String getElemetText(String xml, String elementName) throws XMLStreamException, ParserConfigurationException, SAXException, IOException {

		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		InputSource src = new InputSource();
		src.setCharacterStream(new StringReader(xml));

		Document doc = builder.parse(src);
		
		return doc.getElementsByTagName(elementName).item(0).getTextContent();
		
		
	}

	
	protected synchronized void storeItemsToJms(StoreDisassembledItemsJMSService jmsService, final List<String> disassembleItems, final String queueName) {
		for (String item : disassembleItems) {
			try{
//				log.info("store to the queue");
				jmsService.storeItem(new QueueItem(queueName, item));
			} catch (Exception ex){
				log.error(item);
				log.error("Store to the " + queueName + " failed", ex);
			}
		}
	}
}
