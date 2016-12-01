package cz.datart.jboss.myDatart.chunks;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.Map;

import org.junit.Test;

import cz.datart.jboss.myDatart.chunks.config.persistence.model.Chunk;
import cz.datart.jboss.myDatart.chunks.config.persistence.model.Group;

public class SortAndMapListTest {

	@Test
	public void test() {
		Group group = new Group();
		Chunk chunk;
		
		group.setId("A");
		
		chunk = new Chunk();
		chunk.setName("c1");
		chunk.setSequence(1);
		
		group.addChunk(chunk);
		
		chunk = new Chunk();
		chunk.setName("c2");
		chunk.setSequence(4);
		
		group.addChunk(chunk);
		
		chunk = new Chunk();
		chunk.setName("c3");
		chunk.setSequence(2);
		
		group.addChunk(chunk);
		
		Map<String, Chunk> result = new ChunkGroupProcessWorker(group, null, null).getChunks();
		
		Iterator<String> iter = result.keySet().iterator();
		
		assertEquals("c1", iter.next());
		assertEquals("c3", iter.next());
		assertEquals("c2", iter.next());
	}

}
