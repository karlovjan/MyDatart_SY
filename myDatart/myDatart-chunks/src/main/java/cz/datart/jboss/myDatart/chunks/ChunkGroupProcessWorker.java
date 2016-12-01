package cz.datart.jboss.myDatart.chunks;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.concurrent.ManagedThreadFactory;
import org.apache.log4j.Logger;
import cz.datart.jboss.myDatart.chunks.config.persistence.model.Chunk;
import cz.datart.jboss.myDatart.chunks.config.persistence.model.Group;

//@Stateful
//@Local
public class ChunkGroupProcessWorker implements IChunkProcessWorker, Runnable {

	private final Logger log;
	
//	@Resource(name = "DefaultManagedThreadFactory")
//	@Resource
    private ManagedThreadFactory threadFactory;
	
//	@Inject
//	@Reference("ChunkCreation")
	private ChunkCreation chunkProcessor;
	
	private String mChunkGroupName;
	
	private Thread workerThread;
	
	private Map<String, Chunk> chunks;

	
	public ChunkGroupProcessWorker(Group chunkGroup, ManagedThreadFactory threadFactoryNew, ChunkCreation chunkProcessorNew ) {
		
		List<Chunk> tmpGroupChunks = null;
		try {
			tmpGroupChunks = chunkGroup.getChunks();
		} catch (Exception e) {
			Logger.getLogger("ChunkGroupProcessWorker-NULL").error("Get chunks error", e);
		}
		
		if(chunkGroup == null || tmpGroupChunks == null){
			log = Logger.getLogger("ChunkGroupProcessWorker-NULL");
			log.warn("A chunk group is not created because it is null or a chunk group hasn't got any chunks");
			return;
		}
		
		this.threadFactory = threadFactoryNew;
		this.chunkProcessor = chunkProcessorNew;
		
		this.mChunkGroupName = chunkGroup.getId();
		
		log = Logger.getLogger("ChunkGroupProcessWorker-"+mChunkGroupName);
		
		chunks = getSortedChunkMap(tmpGroupChunks);
	}
	
	protected Map<String, Chunk> getSortedChunkMap(List<Chunk> chunksUnsorted) {
		
//		return chunksUnsorted.stream().sorted(new Comparator<Chunk>() {
//			@Override
//			public int compare(Chunk c1, Chunk c2) {
//				return (c1.getSequence()<c2.getSequence() ? -1 : (c1.getSequence()==c2.getSequence() ? 0 : 1));
//			};
//		}).collect(Collectors.toMap(Chunk::getName, c -> c));
		
		//toConcurrentMap
		
		return chunksUnsorted.stream()
				.sorted(
					Comparator.comparing(Chunk::getSequence))
				.collect(
						Collectors.toMap(
								Chunk::getName, 
								c -> c,
								(a,b) -> a,
								LinkedHashMap::new));
		
		
		//Comparator.comparing(Building::getName)
		//.collect(Collectors.toMap(c -> c, c -> c));
		
		//.collect(Collectors.groupingBy(Chunk::getName));
		
//		return null;
	}

	@Override
	public synchronized void start() {
		
		log.info("Start a new thread for a chunk group process: " + this.mChunkGroupName);
		
		if(chunkProcessor == null){
			log.warn("A Chunk processor is not set");
			return;
		}
		
		if(threadFactory == null){
			
			log.warn("Thread factory is null. Chunk processor is " + (chunkProcessor != null));
			return;
		}
		workerThread = threadFactory.newThread(this);

		workerThread.setName(this.mChunkGroupName);

        workerThread.setPriority(Thread.MAX_PRIORITY); //v puvodnim myDatartu byla priorita pro chunky Max

        workerThread.start();
        
	}

	@Override
	public synchronized void stop() {
		log.info("Stop a thread for a chunk group process: " + this.mChunkGroupName);
		
		if(this.workerThread != null){
			this.workerThread.interrupt();
			
			try {
				this.workerThread.join(500);
			} catch (InterruptedException e) {
				log.warn("ERROR", e);
			}
			
			this.workerThread = null;
		}
	}

	@Override
	public synchronized boolean isAlive() {
		return workerThread != null ? !workerThread.isInterrupted() && workerThread.isAlive() : false;
	}


	@Override
	public void run() {
		
//		for (String key : linkedHashMap.keySet()) {
//			System.out.println(key + ":\t" + linkedHashMap.get(key));
//		}
		
		
//		while(!Thread.interrupted()){
			//go through all chunks and creating chunks
			getChunks().forEach((chunkName, chunkObject)->{
				//TODO call chunkcreate service and wait for result until chunk is empty
				
				boolean chunkCreated = false;
				
				//vytvarej chunky 
				do {
					try {
						chunkCreated = chunkProcessor.create(chunkObject);
					} catch (Exception e) {
						log.error(e.getMessage(), e);
						chunkCreated = false;
					}
		        } while (chunkCreated);
				
				
				log.info("Process finished for a chunk: " + chunkName);
				
			});
		
			//vsechny chunky byly vytvoreny
			
//		}
		
		log.info("Process finished for a chunk group: " + mChunkGroupName);
	}

	public Map<String, Chunk> getChunks() {
		return chunks;
	}

	@Override
	public void updateConfiguration(Group chunkGroup) {
		
		if(chunkGroup  != null){
			try {
				List<Chunk> chunksUnsorted = chunkGroup.getChunks();
				
				if(chunksUnsorted != null ){
					
					if(!chunksUnsorted.isEmpty()){
						chunks = getSortedChunkMap(chunksUnsorted);
					} 
				
				}
			} catch (Exception e) {
				log.error("Get chunks error", e);
			}
			
		}
	}
}
