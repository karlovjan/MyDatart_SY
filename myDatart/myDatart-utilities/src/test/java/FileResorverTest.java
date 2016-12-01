import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.Properties;

import org.junit.Test;

import cz.datart.jboss.myDatart.utils.PropertyFileResolver;

public class FileResorverTest {

	@Test
	public void test() {
		
		  Properties properties = new Properties();
			
		  try (InputStream fileInStream = PropertyFileResolver.class.getClassLoader().getResourceAsStream("application.properties")) {
	//          properties.load(new FileInputStream(file));
	      	properties.load(fileInStream);
	      } catch (Exception e) {
	          System.out.println("Unable to load properties file" + e);
	      } 
			
		  
		  String result = properties.getProperty("myDatart.segment");
		
		assertEquals("CZ", result);
	}

}
