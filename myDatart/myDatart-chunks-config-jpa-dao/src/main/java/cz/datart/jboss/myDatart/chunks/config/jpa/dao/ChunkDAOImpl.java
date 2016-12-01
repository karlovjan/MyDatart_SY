package cz.datart.jboss.myDatart.chunks.config.jpa.dao;

import javax.enterprise.context.Dependent;

import cz.datart.jboss.myDatart.chunks.config.persistence.model.Chunk;

@Dependent
public class ChunkDAOImpl extends AbstractDAOImpl<Chunk> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -17597421436467760L;


	public ChunkDAOImpl() {
		super(Chunk.class);
		
	}

}
