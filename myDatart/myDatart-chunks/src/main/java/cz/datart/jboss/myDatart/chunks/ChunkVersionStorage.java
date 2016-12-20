package cz.datart.jboss.myDatart.chunks;

import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.log4j.Logger;


@Singleton
//@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class ChunkVersionStorage extends ConcurrentHashMap<String, String> {

	//pri @ConcurrencyManagement( nesmim pouzit @Lock( protoze je containerem ignorovan, defaultne jsou vsechny metody @LOck(Write), musim 
	
	@Inject
	private Logger log;
	
	@PostConstruct
	public void init() {
	    log.info("Creating ChunkVersionStorage singleton instance.");
	}
	
	private static final long serialVersionUID = 1L;

	//musim zadat synchronized protoze jinak je defaultne kazda metoda @Lock(Write)!
	public void removeVersion(String chunkName){
		if(chunkName != null){
			this.remove(chunkName);
		}
	}
	
	@PreDestroy
	public void dispose() {
		log.info("Desposing ChunkVersionStorage singleton instance.");
		this.clear();
	}
}
