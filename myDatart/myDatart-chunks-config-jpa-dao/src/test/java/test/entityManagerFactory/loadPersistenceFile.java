package test.entityManagerFactory;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import cz.datart.jboss.myDatart.chunks.config.persistence.file.PersistenceFileResolver;

public class loadPersistenceFile {

	@Test
	public void readPersistenceUnitNameTest() throws IOException {
		
		PersistenceFileResolver fResolver = new PersistenceFileResolver();
		
		
		assertEquals("myDatart-ChunkConfigurationPU", fResolver.getPersistenceUnitName());
		
	}

}
