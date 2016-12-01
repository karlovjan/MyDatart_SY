package cz.datart.jboss.myDatart.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;

@Singleton
public class PropertyFileResolver {
	
//	private Map<String, String> properties = new HashMap<>();
	private Properties properties = new Properties();
	
    @PostConstruct
    private void init() throws IOException {
          
        //matches the property name as defined in the system-properties element in WildFly
//        String propertyFile = System.getProperty("application.properties");
//        File file = new File(propertyFile);
    	

        try (InputStream fileInStream = PropertyFileResolver.class.getClassLoader().getResourceAsStream("application.properties")) {
//            properties.load(new FileInputStream(file));
        	properties.load(fileInStream);
        } 
//        catch (Exception e) {
//            log.error("Unable to load properties file", e);
//        }
          
//        HashMap<?, ?> hashMap = new HashMap<>(properties);
//        this.properties.putAll(hashMap);
    }
  
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    
}
