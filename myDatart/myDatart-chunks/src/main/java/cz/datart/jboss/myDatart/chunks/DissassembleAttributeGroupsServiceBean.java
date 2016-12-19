package cz.datart.jboss.myDatart.chunks;

import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.switchyard.component.bean.Property;
import org.switchyard.component.bean.Reference;
import org.switchyard.component.bean.Service;

@Service(value = DissassembledItemJMSService.class, name = "DissassembleAttributeGroupsService")
public class DissassembleAttributeGroupsServiceBean extends ChunkDissassemblyBean implements DissassembledItemJMSService {

//	@Inject
//	@Reference("StoreDissassembledAttributeGroupsRef")
//	private DissassembledItemJMSService jms;
	
	@Inject
	private Logger log;
	
	@Inject
	@Reference("StoreDisassembledItemsJMSService")
	private StoreDisassembledItemsJMSService jms;
	
	@Property(name="segment")
//	@ApplicationProperty(name="segment")
	private String scopeSegment; //CZ, SK
	

	@Property(name="environment")
	private String environment;
	
	@Override
	public void process(String xml) {
//		dissassemble(jms, xml, ChunkItems.AttributeGroup);
		
		log.info("Dissassemble AttributeGroup xml items and send items to the chunk queue");
		List<String> disassembleItems = disassemble(jms, xml, ChunkItems.AttributeGroup);
		
//		String queueName = ChunkUtils.getChunkQueueName("AttributeGroups", scopeSegment);
		String queueName = ChunkUtils.getChunkQueueName("AttributeGroups", environment, scopeSegment);
		
		log.info(String.format("Send items to the chunk queue: %s", queueName));
		
		storeItemsToJms(jms, disassembleItems, queueName);
	}

}
