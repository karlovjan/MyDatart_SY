package cz.datart.jboss.myDatart.chunks;

import cz.datart.jboss.myDatart.chunks.config.persistence.model.Group;

public interface IChunkProcessWorker {

	public void start();
	
	public void stop();
	
	public boolean isAlive();

	public void updateConfiguration(Group chunkGroup);
}
