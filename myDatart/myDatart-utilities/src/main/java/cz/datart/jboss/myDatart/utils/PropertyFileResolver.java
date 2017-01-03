package cz.datart.jboss.myDatart.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;

@Singleton
public class PropertyFileResolver {
	
	private String configFileName;
//	private Map<String, String> properties = new HashMap<>();
	private Properties properties = new Properties();
	
    public String getConfigFileName() {
		return configFileName;
	}

	public void setConfigFileName(String configFileName) {
		this.configFileName = configFileName;
	}

	@PostConstruct
    private void init() throws IOException {
          
        //matches the property name as defined in the system-properties element in WildFly
//        String propertyFile = System.getProperty("application.properties");
//        File file = new File(propertyFile);
    	

//        loadPropertyFile(); 
//        catch (Exception e) {
//            log.error("Unable to load properties file", e);
//        }
          
//        HashMap<?, ?> hashMap = new HashMap<>(properties);
//        this.properties.putAll(hashMap);
    }

	public void loadPropertyFile() throws IOException {
		try (InputStream fileInStream = PropertyFileResolver.class.getClassLoader().getResourceAsStream(getConfigFileName())) {
//            properties.load(new FileInputStream(file));
        	properties.load(fileInStream);
        }
	}
  
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    
}
