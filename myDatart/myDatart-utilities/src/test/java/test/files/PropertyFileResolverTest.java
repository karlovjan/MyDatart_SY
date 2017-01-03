package test.files;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import cz.datart.jboss.myDatart.utils.PropertyFileResolver;

public class PropertyFileResolverTest {

	@Test
	public void loadPropertiesFiletest() throws IOException {
		
		PropertyFileResolver fResolver = new PropertyFileResolver();
		
		fResolver.setConfigFileName("test.properties");
		fResolver.loadPropertyFile();
		
		assertEquals("test1", fResolver.getProperty("test.property"));
		
		
	}

}
