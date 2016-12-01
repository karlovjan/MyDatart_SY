package cz.datart.jboss.myDatart.chunks.config;

import java.util.List;

import cz.datart.jboss.myDatart.chunks.config.persistence.model.Group;


public interface ChunkGroupConfigurator {

	public void create(Group chunkGroup);
	
	public void update(Group chunkGroup);
	
	public void delete(String chunkGroupID);
	
	public List<Group> getAll();
	public Group get(String chunkGroupID);
}
