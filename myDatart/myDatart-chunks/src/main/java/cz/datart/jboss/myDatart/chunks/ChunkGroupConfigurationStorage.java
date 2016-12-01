package cz.datart.jboss.myDatart.chunks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.ejb.AccessTimeout;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import cz.datart.jboss.myDatart.chunks.config.persistence.model.Chunk;
import cz.datart.jboss.myDatart.chunks.config.persistence.model.ChunkStatus;
import cz.datart.jboss.myDatart.chunks.config.persistence.model.Group;


@Startup
@Singleton
//@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@AccessTimeout(value = 5, unit = TimeUnit.MINUTES)
public class ChunkGroupConfigurationStorage {

	@Inject
	private Logger log;
	
	private Map<String, Group> mChunkGroups;
	
	@PostConstruct
    private void startup() {
        log.info("Chunk group configuration storage created!");
    }

	

	@Lock(LockType.WRITE)
	public void setNewChunkGroup(Group newChunkGroup) {
		
		if(!isChunkGroupsConfigurationSet()){
			this.mChunkGroups = new HashMap<>();
		} 
		
		if(newChunkGroup == null){
			log.warn("A new chunk group is null");
			return;
		}
		
		log.info(String.format("Store a new chunk group %s", newChunkGroup.getId()));
		
		//set initial status
		newChunkGroup.setStatus(ChunkStatus.FINISHED);
		
		this.mChunkGroups.put(newChunkGroup.getId(), newChunkGroup);
	}
	
	@Lock(LockType.WRITE)
	public void setChunkGroup(Group updatedChunkGroup) {
		
		if(!isChunkGroupsConfigurationSet()){
			this.mChunkGroups = new HashMap<>();
		} 
		
		if(updatedChunkGroup == null){
			log.warn("A modified chunk group is null");
			return;
		}
		
		log.info(String.format("Store a modified chunk group %s", updatedChunkGroup.getId()));
		
		Group existingChunkGroup = getChunkGroup(updatedChunkGroup.getId());
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
	
	@Lock(LockType.READ)
	public Map<String, Group> getChunkGroups() {
		return mChunkGroups;
	}
	
	@Lock(LockType.READ)
	public boolean isChunkGroupsConfigurationSet() {
		if( mChunkGroups == null){
			log.warn("chunk groups storage is not set");
			return false;
		}
		
		return true;
	}
	
	@Lock(LockType.READ)
	public int getChunkGroupCount() {
		return isChunkGroupsConfigurationSet() ? mChunkGroups.size() : 0;
	}
	
	@Lock(LockType.READ)
	public String[] getChunkGroupIds() {
		return isChunkGroupsConfigurationSet() ? mChunkGroups.keySet().toArray(new String[0]) : new String[0];
	}

	@Lock(LockType.READ)
	public Group getChunkGroup(String groupId) {
		
		return isChunkGroupsConfigurationSet() ? this.mChunkGroups.get(groupId) : null;
	}

	@Lock(LockType.READ)
	public boolean existChunkGroup(String groupId) {
		
		return isChunkGroupsConfigurationSet() ? this.mChunkGroups.containsKey(groupId) : false;
	}

	@Lock(LockType.READ)
	public boolean notifyAxapta(String chunkName) {
		
		if(isChunkGroupsConfigurationSet()){
			
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
		}
		return false;
	}
}
