package cz.datart.jboss.myDatart.chunks.queueing;

import java.util.List;

import javax.inject.Inject;
import org.apache.log4j.Logger;
import org.switchyard.component.bean.Property;
import org.switchyard.component.bean.Reference;
import org.switchyard.component.bean.Service;

import cz.datart.jboss.myDatart.utils.ChunkUtils;

@Service(value = DissassembledItemJMSService.class, name = "DissassembleProducersService")
public class DissassembleProducersServiceBean extends ChunkDissassemblyBean implements DissassembledItemJMSService {
	
	@Inject
	private Logger log;
		
	
//	@Reference("StoreDissassembledProducersRef")
//	private DissassembledItemJMSService jms;
	
	
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
		
		log.info("Dissassemble Producer xml items and send items to the chunk queue");
		List<String> disassembleItems = disassemble(jms, xml, ChunkItems.Producer);
		
		String queueName = ChunkUtils.getChunkQueueName("Producers", environment, scopeSegment);
		
		log.info(String.format("Send items to the chunk queue: %s", queueName));
		
		storeItemsToJms(jms, disassembleItems, queueName);
		
		
	}

}
