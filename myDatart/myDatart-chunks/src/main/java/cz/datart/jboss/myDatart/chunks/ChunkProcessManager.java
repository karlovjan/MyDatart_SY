package cz.datart.jboss.myDatart.chunks;

public interface ChunkProcessManager {

	public void startProcessingChunkGroup(String chunkGroupId);
	public void startProcessingAllChunkGroups();
	
	public void stopAllChunkGroupProcesses();
	public void stopProcessingChunkGroup(String chunkGroupId);
	
	public void restartAllChunkGroupProcesses();

	public void restartChunkGroupProcess(String chunkGroupId);
}
