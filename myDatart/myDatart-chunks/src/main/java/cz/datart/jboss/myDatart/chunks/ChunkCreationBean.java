package cz.datart.jboss.myDatart.chunks;

import java.io.ByteArrayInputStream;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.log4j.Logger;
import org.switchyard.component.bean.Property;
import org.switchyard.component.bean.Reference;
import org.switchyard.component.bean.Service;

import cz.datart.jboss.myDatart.chunks.config.persistence.model.Chunk;


@Service(ChunkCreation.class)
public class ChunkCreationBean implements ChunkCreation {

//	private static final String CHUNK_QUEUE_NAME_SUFFIX = "Queue";
//
//	private static final String CHUNK_QUEUE_NAME_PREFIX = "ChunkUpdate";

	private static final String VERSION_PREFIX = "ver";

//	@Property(name="eshopWSNamespace")
	private final String wsNamespace = "http://etnetera.com/projects/datart/bambino";
	
	@Inject
	private Logger log;
	
	//SendChunkToEshopService
	@Inject
	@Reference("PrepareChunkSendingService")
	private SendChunkToEshopService chunkSender;
	
	@Inject
	@Reference("UpdateChunkVersionService")
	private UpdateChunkVersionService versionUpdater;

	@EJB
	private IJMSChunkLoader jmsClient;
	
	@EJB
	private ChunkVersionStorage versionStorage;

	@Property(name="segment")
//	@ApplicationProperty(name="segment")
	private String scopeSegment; //CZ, SK
	

	@Property(name="environment")
	private String environment;
	
	private XMLInputFactory inputFactory = XMLInputFactory.newInstance();
	
	@Override
	public boolean create(Chunk chunkParameters) {
		
		if(chunkParameters == null){
			log.info("Chunk parameter is null");
			return false;
		}
		
		
		String chunkName = chunkParameters.getName();
		String chunkGroup = chunkParameters.getGroup().getId();
		
		log.info(String.format("Create chunk %s from the chunk  group %s, environment: %s, segment: %s", chunkName, chunkGroup, environment, scopeSegment));
		//ChunkUpdateAttributesQueue
		String queueName = ChunkUtils.getChunkQueueName(chunkName, environment, scopeSegment);
		//load chunks
		List<String> items = null;
		try {
			items = jmsClient.loadMessages(chunkParameters.getCount(), chunkGroup, queueName);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		
		if(items == null || items.isEmpty()){
			return false;
		}
		
		ChunkUtils utils = new ChunkUtils();
		String listXML = utils.createListXML(items);
		
		
		
		String oldVersion = getChunkVersion(chunkName);
		
		String newVersion = utils.getNewVersion(oldVersion, items.size());
		
		if(newVersion == null){
			log.info(String.format("New version of the chunk %s is not counted. Stop sending a chunk.", chunkName));
			return false; //stop processing of that chunk
		}
		//update version by new one
		versionStorage.put(chunkName, newVersion);
		
		String soapMessage = utils.createSoapMessage(chunkName, listXML, scopeSegment, wsNamespace, oldVersion, newVersion);
		log.info("Length of chunk message: " + soapMessage.length());//test message
		
		try{
			chunkSender.send(soapMessage);
			
			log.info(String.format("Chunk %s was sent", chunkName));
			
		} catch (Throwable ex){
			log.error(ex.getMessage(), ex);
		}
		
		
		
		return true;
	}

	public String getChunkVersion(String chunkName) {
		
		String oldVersion = versionStorage.get(chunkName);
		
		if(oldVersion == null){
			log.info(String.format("No version for chunk %s", chunkName));
			log.info(String.format("Version of the chunk %s will be updated", chunkName));
			//update chunk versions
			updateChunkVersions(scopeSegment);
			
			oldVersion = versionStorage.get(chunkName);
			
			if(oldVersion == null){
				log.info(String.format("Version of the chunk %s will not be updated. Stop sending a chunk.", chunkName));
				return null; //stop processing of that chunk
			}
		}
		
		
		return oldVersion;
	}

	private void updateChunkVersions(String segment) {
		
//		String getVersionSoapRequest = new ChunkUtils().getVersionSoapRequest(scopeSegment);
		String newVersions = null;
		try {
			newVersions = versionUpdater.update(segment);
						
		} catch (Exception e) {
			log.error("Get Chunk versions from eshop has failed", e);
		}
		
		log.info(String.format("GetVersions response: %s", newVersions));
		
		if(newVersions != null && !newVersions.isEmpty()){
			try {
				parseChunkVersions_EventReader(newVersions);
							
			} catch (Exception e) {
				log.error("Storing chunk versions has failed", e);
			}
		}
		
	}

	protected void parseChunkVersions_EventReader(String xml) throws XMLStreamException {

		
		XMLEventReader xmlEventReader = inputFactory.createXMLEventReader(new ByteArrayInputStream(xml.getBytes()));

		
		String flowName = null;
		String version = null;
        while (xmlEventReader.hasNext()) {
            XMLEvent xmlEvent = xmlEventReader.nextEvent();

            switch (xmlEvent.getEventType()) {
                case XMLEvent.START_ELEMENT:
                    flowName = getFlowNameFromStartElement(xmlEvent.asStartElement());
                    break;

                case XMLEvent.CHARACTERS:
                    if (flowName != null)
						version = xmlEvent.asCharacters().getData();
                    break;

                case XMLEvent.END_ELEMENT:
                    if (flowName != null) {
                        try {
                            long vers = Long.parseUnsignedLong(version.trim());
                            String sVersion = Long.toString(vers);
                            versionStorage.put(flowName, sVersion);
                            
                            log.info("Loading version " + sVersion + " for flow '" + flowName + "'");
                        } catch (NumberFormatException e) {
                            log.warn("Got invalid version for flow '" + flowName + "'", e);
                        }

                        flowName = null;
                    }
                    break;
            }
        }
        
		xmlEventReader.close();
		
	}

	private String getFlowNameFromStartElement(StartElement startElement) {
		String localPart = startElement.getName().getLocalPart();
		if (localPart.startsWith(VERSION_PREFIX)) {
			return localPart.substring(VERSION_PREFIX.length());
		}
		return null;
	}
}
