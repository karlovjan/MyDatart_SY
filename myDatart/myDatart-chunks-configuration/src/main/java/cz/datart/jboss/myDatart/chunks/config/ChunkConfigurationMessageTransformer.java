package cz.datart.jboss.myDatart.chunks.config;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.switchyard.annotations.Transformer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import cz.datart.jboss.myDatart.chunks.config.persistence.model.Chunk;
import cz.datart.jboss.myDatart.chunks.config.persistence.model.ChunkStatus;
import cz.datart.jboss.myDatart.chunks.config.persistence.model.Group;

import org.w3c.dom.Node;

@Dependent
public final class ChunkConfigurationMessageTransformer {
	
//	private static final Logger LOG = Logger.getLogger(ChunkConfigurationMessageTransformer.class);
	@Inject
	private Logger LOG;

	@Transformer(to = "{urn:cz.datart.jboss.myDatart.chunks.config:myDatart-chunks-configuration:1.0}getResponse")
	public Element transformGroupToGetResponse(Group from) {
		
		StringBuffer xml = new StringBuffer()
	            .append("<urn:getResponse xmlns:urn=\"cz.datart.jboss.myDatart.chunks.config:myDatart-chunks-configuration:1.0\">");
	            
		if(from != null){
			
			//not found any group in db
			
			xml.append(getGroupXmlElement(from));
		}
		
		xml.append("</urn:getResponse>");
		
		return toElement( xml.toString() );
	   
	}

	private String getGroupXmlElement(Group from) {
		StringBuffer xml = new StringBuffer()
	            .append("<group>")
	            .append(createXMLElement("id", from.getId()))
	            .append(createXMLElement("cronExpression", from.getCronExpression()))
	            .append(createXMLElement("resendCount", from.getResendCount()))
	            .append(createXMLElement("status", from.getStatus()));
		
		if(from.getStartExecutionTime() != null){
			xml.append(createXMLElement("startExecutionTime", from.getStartExecutionTime()));
		}
		
		if(from.getEndExecutionTime() != null){
			xml.append(createXMLElement("endExecutionTime", from.getEndExecutionTime()));
		}
		
		if(from.getModifiedOn() != null){
			xml.append(createXMLElement("modifiedOn", from.getModifiedOn()));
		}
	            
		if(from.getChunks() != null){
			xml.append("<chunks>");
			
			for (Chunk chunk : from.getChunks()) {
				
				xml.append("<chunk>")
					.append(createXMLElement("name", chunk.getName()))
					.append(createXMLElement("count", chunk.getCount()))
					.append(createXMLElement("confirmation", chunk.isConfirmation()))
					.append(createXMLElement("sequence", chunk.getSequence()))
					.append("</chunk>");
			}
			
			
			xml.append("</chunks>");
		}
		
		xml.append("</group>");
	    

	    return xml.toString();
	}
	
	protected String createXMLElement(String xmlElementName, int intValue) {
		StringBuffer xmlSB = new StringBuffer();
		xmlSB.append("<").append(xmlElementName).append(">");
		xmlSB.append(intValue);
		xmlSB.append("</").append(xmlElementName).append(">");
		
		return xmlSB.toString();
	}
	
	protected String createXMLElement(String xmlElementName, boolean booleanValue) {
		StringBuffer xmlSB = new StringBuffer();
		xmlSB.append("<").append(xmlElementName).append(">");
		xmlSB.append(booleanValue);
		xmlSB.append("</").append(xmlElementName).append(">");
		
		return xmlSB.toString();
	}

	protected String createXMLElement(String xmlElementName, String textValue){
		StringBuffer xmlSB = new StringBuffer();
		xmlSB.append("<").append(xmlElementName).append(">");
		xmlSB.append(textValue != null ? textValue : "");
		xmlSB.append("</").append(xmlElementName).append(">");
		
		return xmlSB.toString();
	}
	
	protected String createXMLElement(String xmlElementName, ChunkStatus enumValue) {
		StringBuffer xmlSB = new StringBuffer();
		xmlSB.append("<").append(xmlElementName).append(">");
		xmlSB.append(enumValue != null ? enumValue : "");
		xmlSB.append("</").append(xmlElementName).append(">");
		
		return xmlSB.toString();
	}
	
	protected String createXMLElement(String xmlElementName, Calendar dateTimaValue) {
		//sdf.format(from.getModifiedOn().getTime())
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String dt = dateTimaValue == null ? "" : sdf.format(dateTimaValue.getTime());
		
		StringBuffer xmlSB = new StringBuffer();
		xmlSB.append("<").append(xmlElementName).append(">");
		xmlSB.append(dt);
		xmlSB.append("</").append(xmlElementName).append(">");
		
		return xmlSB.toString();
	}

	@Transformer(to = "{urn:cz.datart.jboss.myDatart.chunks.config:myDatart-chunks-configuration:1.0}getAllResponse")
	public Element transformListToGetAllResponse(List<Group> from) {
		
		StringBuffer xml = new StringBuffer()
	            .append("<urn:getAllResponse xmlns:urn=\"cz.datart.jboss.myDatart.chunks.config:myDatart-chunks-configuration:1.0\"><list>");
	            
		if(from != null){
			LOG.info(String.format("Group Count: %d", from.size()));
			//not found any group in db
			for (Group lgroup : from) {
				LOG.info(String.format("Group name: %s ", lgroup.getId()));
				try{
					xml.append(getGroupXmlElement(lgroup));
				} catch (Exception ex){
					LOG.error("Parsing list of groups failed", ex);
				}
			}
			
		}
		
		xml.append("</list></urn:getAllResponse>");
		
		LOG.info(xml.toString());
		
		Element result =  toElement( xml.toString() );
		
		
		return result;
	}

	@Transformer(from = "{urn:cz.datart.jboss.myDatart.chunks.config:myDatart-chunks-configuration:1.0}create")
	public Group transformCreateToGroup(Element from) {
		Group chunk_group = new Group();

		chunk_group.setCronExpression(getElementValue(from, "cronExpression"));
		chunk_group.setId(getElementValue(from, "id"));
		chunk_group.setResendCount(Integer.valueOf(getElementValue(from, "resendCount")));

//		chunk_group.setStatus(ChunkStatus.valueOf(getElementValue(from, "status")));
		
		chunk_group.setStatus(ChunkStatus.NEW);
		
		chunk_group.setModifiedOn(Calendar.getInstance());
		
		try {
			setChunks(from, chunk_group);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			chunk_group = null;
		}
		
		
		
		//Datumy se neposilaji!!!
		
		
//		
//		cal.setTime(sdf.parse(getElementValue(from, "endExecutionTime")));
//		chunk_group.setEndExecutionTime(cal);
		
        return chunk_group;
	}

	@Transformer(from = "{urn:cz.datart.jboss.myDatart.chunks.config:myDatart-chunks-configuration:1.0}delete")
	public String transformDeleteToString(Element from) {
		
		return getElementValue(from, "chunkGroupID");
	}

	@Transformer(from = "{urn:cz.datart.jboss.myDatart.chunks.config:myDatart-chunks-configuration:1.0}get")
	public String transformGetToString(Element from) {
		
		return getElementValue(from, "chunkGroupID");
	}

	@Transformer(from = "{urn:cz.datart.jboss.myDatart.chunks.config:myDatart-chunks-configuration:1.0}update")
	public Group transformUpdateToGroup(Element from) {
		
		LOG.info("--- Update chunk group ---" );
		Group chunk_group = new Group();

		chunk_group.setCronExpression(getElementValue(from, "cronExpression"));
		chunk_group.setId(getElementValue(from, "id"));
		chunk_group.setResendCount(Integer.valueOf(getElementValue(from, "resendCount")));

//		chunk_group.setStatus(ChunkStatus.valueOf(getElementValue(from, "status")));
		
		chunk_group.setStatus(ChunkStatus.UPDATED);
		
		chunk_group.setModifiedOn(Calendar.getInstance());
		
		try {
			setChunks(from, chunk_group);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			chunk_group = null;
		}
		
        return chunk_group;
	}

	
	private void setChunks(Element parent, Group chunkGroup) throws Exception {
        
        NodeList nodes = parent.getElementsByTagName("chunk");
        
        
        if(nodes != null){
        	
        	Chunk chunk;
        	NodeList chunks;
        	Node tmpNode;
        	for(int i = 0; i != nodes.getLength(); i++){
        		
//        		chunk.setName(parent.getElementsByTagName("name").item(0).getChildNodes().item(0).getNodeValue());
        		
        		chunks = nodes.item(i).getChildNodes();
        		chunk = new Chunk();
        		
        		for(int j = 0; j != chunks.getLength(); j++){
        			
        			tmpNode = chunks.item(j);
        			
        			if("name".equalsIgnoreCase(tmpNode.getLocalName())) {
						chunk.setName(tmpNode.getChildNodes().item(0).getNodeValue());
        			} else if ("count".equalsIgnoreCase(tmpNode.getLocalName())) {
        				chunk.setCount(Integer.parseInt(tmpNode.getChildNodes().item(0).getNodeValue()));
					} else if ("sequence".equalsIgnoreCase(tmpNode.getLocalName())) {
        				chunk.setSequence(Integer.parseInt(tmpNode.getChildNodes().item(0).getNodeValue()));
					} else if ("confirmation".equalsIgnoreCase(tmpNode.getLocalName())) {
        				chunk.setConfirmation(Boolean.parseBoolean(tmpNode.getChildNodes().item(0).getNodeValue()));
					}
        		}
        		
        		chunkGroup.addChunk(chunk);
        	}
//        	LOG.info(String.format("length of chunks array: %s: %s", nodes.getLength(), nodes.item(0).getChildNodes().getLength()));
//        	
//        	LOG.info(nodes);
//        	
//        	Chunk chunk = new Chunk();
//        	chunk.setConfirmation(true);
//        	chunk.setCount(10);
//        	chunk.setName("Chunk1");
//        	chunk.setSequence(1);
        	
        	
        	
        }
//        if (nodes.getLength() > 0) {
//            value = nodes.item(0).getChildNodes().item(0).getNodeValue();
//        }
       
    }
	
	/**
     * Returns the text value of a child node of parent.
     */
    private String getElementValue(Element parent, String elementName) {
        String value = null;
        NodeList nodes = parent.getElementsByTagName(elementName);
        if (nodes.getLength() > 0) {
            value = nodes.item(0).getChildNodes().item(0).getNodeValue();
        }
        return value;
    }

    private Element toElement(String xml) {
        DOMResult dom = new DOMResult();
        try {
            TransformerFactory.newInstance().newTransformer().transform(
                new StreamSource(new StringReader(xml)), dom);
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }

        return ((Document) dom.getNode()).getDocumentElement();
    }
}
