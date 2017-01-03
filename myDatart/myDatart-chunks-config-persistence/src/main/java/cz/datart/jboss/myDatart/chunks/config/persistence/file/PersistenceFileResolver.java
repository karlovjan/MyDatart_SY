package cz.datart.jboss.myDatart.chunks.config.persistence.file;

import java.io.File;

import cz.datart.jboss.myDatart.utils.XmlFileResolver;

public class PersistenceFileResolver {

	public String getPersistenceUnitName() throws NullPointerException {
		
		return getPersistenceUnitNameStartWith(null);
	}
	
	public String getPersistenceUnitNameStartWith(String startsWith) throws NullPointerException{
		
		XmlFileResolver resolver = new XmlFileResolver("META-INF" + File.separator + "persistence.xml");
		
		String puName = resolver.getAttribute("persistence-unit", "name", startsWith);
		
		if(puName == null){
			throw new NullPointerException("Chunk configuration persitence unit name is null");
		}
		
		return puName;
		
	}
	
}
