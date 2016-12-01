package cz.datart.jboss.myDatart.chunks;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.AccessTimeout;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.inject.Inject;

import org.apache.log4j.Logger;

@Singleton
//@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@AccessTimeout(value = 5, unit = TimeUnit.MINUTES)
public class ChunkProcessThreadManager {

	@Inject
	private Logger log;
	
	private Map<String, IChunkProcessWorker> mThreads;
	
	
	@PostConstruct
    public void init() {
        log.info("ChunkProcessThreadManager created!");
        log.info("Init a chunk group thread container...");
        
        mThreads = new HashMap<>();
    }
	
	
	@PreDestroy
	public void destroy(){
		log.info("Destroying ChunkProcessThreadManager...");
		
		if(mThreads != null){
			for (IChunkProcessWorker worker : mThreads.values()) {
				worker.stop();
			}
			
			mThreads.clear();
			
			mThreads = null;
			
		}
	}


	@Lock(LockType.READ)
	public IChunkProcessWorker get(String chunkGroupId) {
		
		return mThreads.get(chunkGroupId);
	}


	@Lock(LockType.WRITE)
	public void put(String chunkGroupId, IChunkProcessWorker worker) {
		
		mThreads.put(chunkGroupId, worker);
	}
	
	
	
}
