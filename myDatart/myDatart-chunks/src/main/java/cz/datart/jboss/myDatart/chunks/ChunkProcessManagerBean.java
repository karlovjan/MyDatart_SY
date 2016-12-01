package cz.datart.jboss.myDatart.chunks;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.enterprise.concurrent.ManagedThreadFactory;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.switchyard.component.bean.Reference;
import org.switchyard.component.bean.Service;

@Service(ChunkProcessManager.class)
public class ChunkProcessManagerBean implements ChunkProcessManager {

	@Inject
	private Logger log;
	
	@Resource
    private ManagedThreadFactory threadFactory;
	
	@Inject
	@Reference("ChunkCreation")
	private ChunkCreation chunkProcessor;
	
	@EJB
	private ChunkGroupConfigurationStorage chunkGroupConfiguration;
	
	@EJB
	private ChunkProcessThreadManager chunkGroupWorkerStorage;
	
	@Override
	public synchronized void startProcessingChunkGroup(String chunkGroupId) {
		
		log.info(String.format("Start processing a chunk group with id: %s", chunkGroupId));
		
		IChunkProcessWorker worker = chunkGroupWorkerStorage.get(chunkGroupId);
		//is process alive?
		if(worker != null){
			if(worker.isAlive()){
				log.info(String.format("A chunk group with id: %s is alive", chunkGroupId));
				return; //leave starting this chunk group process
			}
			
			//update worker
			worker.updateConfiguration(chunkGroupConfiguration.getChunkGroup(chunkGroupId));
		} else {
			log.info(String.format("Create a new worker for a chunk group with id: %s", chunkGroupId));
			//create new worker
			worker = new ChunkGroupProcessWorker(chunkGroupConfiguration.getChunkGroup(chunkGroupId), threadFactory, chunkProcessor);
			
			chunkGroupWorkerStorage.put(chunkGroupId, worker);
		}
		
		
		
		worker.start();
	}

	@Override
	public synchronized void startProcessingAllChunkGroups() {
		
		//zde vola periodicky planovac chunkovaciho procesu
		
		//konfigurace chunku se nezmenila
		
		log.info("Start processing all chunk groups");
		
		//go throug all chunk groups
		String[] chunkGroupsIDs = chunkGroupConfiguration.getChunkGroupIds();
		
		for (String chunkGroupID : chunkGroupsIDs) {
			startProcessingChunkGroup(chunkGroupID);
		}
	}

	@Override
	public synchronized void stopAllChunkGroupProcesses() {
		
		log.info("Stop processing all chunk groups");
		
		chunkGroupWorkerStorage.destroy();
	}

	@Override
	public synchronized void stopProcessingChunkGroup(String chunkGroupId) {
		
		log.info(String.format("Stop processing a chunk group with id: %s", chunkGroupId));
		
		IChunkProcessWorker worker = chunkGroupWorkerStorage.get(chunkGroupId);
		//is process alive?
		if(worker != null){
			worker.stop();
		}
	}

	@Override
	public synchronized void restartAllChunkGroupProcesses() {
		
		log.info("Restart processing all chunk groups");
		
		//TODO chunkGroupWorkerStorage.destroy();
		
		
	}

	@Override
	public synchronized void restartChunkGroupProcess(String chunkGroupId) {
		
		//doslo ke zmene v databazi chunkovaci grupy
		
		log.info(String.format("Restart processing a chunk group with id: %s", chunkGroupId));
		
		stopProcessingChunkGroup(chunkGroupId);
		
		
		startProcessingChunkGroup(chunkGroupId);
	}


}
