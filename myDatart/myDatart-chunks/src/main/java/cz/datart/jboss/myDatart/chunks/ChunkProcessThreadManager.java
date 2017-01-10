package cz.datart.jboss.myDatart.chunks;

import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.log4j.Logger;

//@Singleton
//@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
//@AccessTimeout(value = 5, unit = TimeUnit.MINUTES)
@Singleton
public class ChunkProcessThreadManager extends ConcurrentHashMap<String, IChunkProcessWorker> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1972869227744393564L;
	
	@Inject
	private Logger log;
	
	@PostConstruct
    public void init() {
        log.info("ChunkProcessThreadManager created!");
        
    }
	
	
	@PreDestroy
	public void destroy(){
		log.info("Destroying ChunkProcessThreadManager...");
		
		clear();
			
	}

//	public IChunkProcessWorker get(String chunkGroupId) {
//		
//		return mThreads.get(chunkGroupId);
//	}
//
//
//	public void put(String chunkGroupId, IChunkProcessWorker worker) {
//		
//		mThreads.put(chunkGroupId, worker);
//	}
	
	
	
}
