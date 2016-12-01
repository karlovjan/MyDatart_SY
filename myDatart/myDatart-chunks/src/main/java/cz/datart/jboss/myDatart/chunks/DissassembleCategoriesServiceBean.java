package cz.datart.jboss.myDatart.chunks;

import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.switchyard.component.bean.Property;
import org.switchyard.component.bean.Reference;
import org.switchyard.component.bean.Service;

@Service(value = DissassembledItemJMSService.class, name = "DissassembleCategoriesService")
public class DissassembleCategoriesServiceBean extends ChunkDissassemblyBean implements DissassembledItemJMSService {

//	@Inject
//	@Reference("StoreDissassembledCategoriesRef")
//	private DissassembledItemJMSService jms;
	
	@Inject
	private Logger log;
	
	@Inject
	@Reference("StoreDisassembledItemsJMSService")
	private StoreDisassembledItemsJMSService jms;
	
	@Property(name="myDatart.segment")
//	@ApplicationProperty(name="myDatart.segment")
	private String scopeSegment; //CZ, SK
	

	@Property(name="myDatart.environment")
	private String environment;
	
	@Override
	public void process(String xml) {
//		dissassemble(jms, xml, ChunkItems.Category);
		
		log.info("Dissassemble Category xml items and send items to the chunk queue");
		List<String> disassembleItems = disassemble(jms, xml, ChunkItems.Category);
		
		String queueName = ChunkUtils.getChunkQueueName("Categoris", environment, scopeSegment);
		
		log.info(String.format("Send items to the chunk queue: %s", queueName));
		
		storeItemsToJms(jms, disassembleItems, queueName);
	}

}
