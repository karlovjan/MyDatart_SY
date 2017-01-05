package cz.datart.jboss.myDatart.chunks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.log4j.Logger;

import cz.datart.jboss.myDatart.chunks.config.persistence.model.Chunk;
import cz.datart.jboss.myDatart.chunks.config.persistence.model.ChunkStatus;
import cz.datart.jboss.myDatart.chunks.config.persistence.model.Group;


//@Startup
//@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
//@AccessTimeout(value = 5, unit = TimeUnit.MINUTES)
@Singleton
public class ChunkGroupConfigurationStorage {

	@Inject
	private Logger log;
	
	private final Map<String, Group> mChunkGroups;
	
	public ChunkGroupConfigurationStorage() {
		this.mChunkGroups = new ConcurrentHashMap<>();
	}
	
	@PostConstruct
    private void startup() {
        log.info("Chunk group configuration storage created!");
    }

	@PreDestroy
	public void destroy(){
		log.info("Destroying ChunkGroupConfigurationStorage...");
		
		mChunkGroups.clear();
		
	}

	public void setNewChunkGroup(final Group newChunkGroup) {
		
		if(newChunkGroup == null){
			log.warn("A new chunk group is null");
			return;
		}
		
		log.info(String.format("Store a new chunk group %s", newChunkGroup.getId()));
		
		//set initial status
		newChunkGroup.setStatus(ChunkStatus.FINISHED);
		
		this.mChunkGroups.put(newChunkGroup.getId(), newChunkGroup);
	
	}
	
	public void setChunkGroup(final Group updatedChunkGroup) {
		
		if(updatedChunkGroup == null){
			log.warn("A modified chunk group is null");
			return;
		}
		
		log.info(String.format("Store a modified chunk group %s", updatedChunkGroup.getId()));
		
		final Group existingChunkGroup = getChunkGroup(updatedChunkGroup.getId());
		//exist a chunk group?
		if(existingChunkGroup != null){
			//group already exist
			//use existing group status
			updatedChunkGroup.setStatus(existingChunkGroup.getStatus());
			//use the existing start date
			updatedChunkGroup.setStartExecutionTime(existingChunkGroup.getStartExecutionTime());
			//use the existing finished date
			updatedChunkGroup.setEndExecutionTime(existingChunkGroup.getEndExecutionTime());
			
			
			this.mChunkGroups.put(existingChunkGroup.getId(), updatedChunkGroup);
			
			
		} else {
			//a group don't exist
			
			log.info(String.format("A chunk group %s doesn't exist in the storage", updatedChunkGroup.getId()));
			setNewChunkGroup(updatedChunkGroup);
		}
		
	}
	
//	public Map<String, Group> getChunkGroups() {
//		return mChunkGroups;
//	}
	
	public int getChunkGroupCount() {
		return mChunkGroups.size();
	}
	
	public String[] getChunkGroupIds() {
		return mChunkGroups.keySet().toArray(new String[0]);
	}

	public Group getChunkGroup(String groupId) {
		
		return this.mChunkGroups.get(groupId);
	}

	public boolean existChunkGroup(String groupId) {
		
		return this.mChunkGroups.containsKey(groupId);
	}

	public boolean notifyAxapta(String chunkName) {
		
			List<Chunk> chunks = new ArrayList<>();
					
			for (Group group : this.mChunkGroups.values()) {
				
				try {
					chunks = group.getChunks();
				} catch (Exception e) {
					log.error("Get chunks error", e);
				}
				
				for (Chunk chunk : chunks) {
					
					if(chunk.getName().equals(chunkName)){
						return chunk.isConfirmation();
					}
				}
				
				
			}
		
		return false;
	}
}
