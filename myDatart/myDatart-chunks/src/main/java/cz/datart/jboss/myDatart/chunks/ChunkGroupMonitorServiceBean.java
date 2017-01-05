package cz.datart.jboss.myDatart.chunks;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.switchyard.component.bean.Reference;
import org.switchyard.component.bean.Service;

import cz.datart.jboss.myDatart.chunks.jpa.ChunkGroupMonitor;
import cz.datart.jboss.myDatart.chunks.config.persistence.model.Group;

@Service(value = ChunkGroupMonitor.class, name = "ChunkGroupMonitorService")
public class ChunkGroupMonitorServiceBean implements ChunkGroupMonitor {

	@Inject
	private Logger log;
	
	@Inject
	@Reference("ChunkProcessManager")
	private ChunkProcessManager chunkProcessor;
	
	@Inject
	private ChunkGroupConfigurationStorage chunkGroupConfiguration;
	
	@Override
	public void getChunkGroup(Group updatedGroup) {
		
		if(updatedGroup == null){
			log.warn("Updating group is null");
			return;
		}
		
		log.info(String.format("Update group configuration for group Id: %s", updatedGroup.getId()));
		
		//exist a chunk group?
		if(chunkGroupConfiguration.existChunkGroup(updatedGroup.getId())){
			//group already exist
			
			//update existing group
			chunkGroupConfiguration.setChunkGroup(updatedGroup);
			
//			if(updatedGroup.getStatus() == ChunkGroupStatus.UPDATED){
//				
//			}
			
//			chunkProcessor.restartChunkGroupProcess(updatedGroup.getId());
			
			//aktualizace configurace se overuje periodicky jendou za 30s,
			//tento periodicky dotaz nemuze zastavovat bezici chunkovaci procesy, kazdych 30s
			//data ve storage se zmeni a pri dotazu na parametry se v procesu pouzijou nove data
		} else {
			//a group don't exist
			
			//save a new group
			chunkGroupConfiguration.setNewChunkGroup(updatedGroup);
			
			chunkProcessor.startProcessingChunkGroup(updatedGroup.getId());
		}
		
	}

}
