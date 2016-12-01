package cz.datart.jboss.myDatart.chunks;

import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class NotifyAxaptaPredicate {

	@EJB
	private ChunkGroupConfigurationStorage chunkGroupConfiguration;
	
	public boolean notify(String chunkName){
		if(chunkGroupConfiguration != null){
			return chunkGroupConfiguration.notifyAxapta(chunkName);
		}
		
		return false;
	}
}
