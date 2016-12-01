package cz.datart.jboss.myDatart.chunks;

import java.util.concurrent.ConcurrentHashMap;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;

@Singleton
//@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class ChunkVersionStorage extends ConcurrentHashMap<String, String> {

	//pri @ConcurrencyManagement( nesmim pouzit @Lock( protoze je containerem ignorovan, defaultne jsou vsechny metody @LOck(Write), musim 
	
	
	
	private static final long serialVersionUID = 1L;

	//musim zadat synchronized protoze jinak je defaultne kazda metoda @Lock(Write)!
	public void removeVersion(String chunkName){
		if(chunkName != null){
			this.remove(chunkName);
		}
	}
}
