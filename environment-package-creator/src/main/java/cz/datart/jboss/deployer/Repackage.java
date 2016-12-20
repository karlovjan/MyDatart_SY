package cz.datart.jboss.deployer;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Repackage {
	//http://logging.apache.org/log4j/2.x/manual/migration.html
	private static final Logger log = LogManager.getLogger();

	public static void main(String[] args) {
		
		Repackage repackageAction = new Repackage(args);
		
		RepackageData data = new RepackageData();
		
		repackageAction.setDatamodel(data);
		
		try {
			repackageAction.loadPackageEnvironmentData();
			
			repackageAction.copyPackageToTempDir();
			
			repackageAction.repackage();
			
			
		} catch (Exception e) {
			
			log.error("ERROR", e);
			
			e.printStackTrace();
			
			
		} finally {
			repackageAction.removeTemporaryFolder();
		}
		
		/* nemubudu brat ohled na to co se da jako argument, segment a nevironment se musi nastavit v konfiguracnim souboru,
		 *  tak jako ostatni atributy specificke pro vytvareny pro specificky balicek
		if(args == null || args.length == 0){
			//no environments variables
			//load environment and segment from config file
			
//			repackageAction.load
		} else if ( args.length == 2){
			//environment and segment is given
			data.setEnvironment(args[0]);
			data.setSegment(args[1]);
		}
		*/
		
		
	}
	
	public Repackage() {
		
	}
	
	public Repackage(String[] inputArguments) {
		readInputArguments(inputArguments);
	}
	
	protected void readInputArguments(String[] args) {
		
		if(Utils.isArrayEmpty(args)){
			return;
		}

		this.packageName = args[0];
	}

	public static void extractJavaPackageFiles(List<String> packageFilePaths){
		
//		List<String> command = new ArrayList<>();
//		command.add("jar")
//		ProcessBuilder pb = new ProcessBuilder(command)
		 
		
	}

	private RepackageData configData;
	private File inputConfigFile;
	private String packageName;
	private boolean configurationFileModified;

	public void setDatamodel(RepackageData pRepackageConfigData) {
		
		this.configData = pRepackageConfigData;
	}

	/**
	 * Load a package environment data from a package configuration file
	 * @throws IOException 
	 */
	public void loadPackageEnvironmentData() throws IOException {
		
		log.info("Load environmet and segment attributes from config file...");
		
		final Properties prop = new Properties();
		
		File configFile = getInputConfigFile();
		
		log.info("The config file is loading from path: {}", configFile.getAbsolutePath());
		
		try (InputStream inputS = new FileInputStream(configFile)) {

			// load a properties file
			prop.load(inputS);

		} 
		
		// get the property value and print it out
		configData.setEnvironment(prop.getProperty("environment"));
		configData.setSegment(prop.getProperty("segment"));
		
		configData.setDataSourceName(prop.getProperty("jpa.persistence.datasource.name"));
		configData.setSqlDriver(prop.getProperty("jpa.db.driver"));
		configData.setShowSql(Utils.parseBoolean(prop.getProperty("jpa.db.show_sql")));
		
		log.info("Loaded environment: {}, segment: {}", configData.getEnvironment(), configData.getSegment());
		log.info("Loaded datasource: {}, sqlDriver: {}, showSgl: {}", configData.getDataSourceName(), configData.getSqlDriver(), configData.isShowSql());
	}

	private File getInputConfigFile() throws FileNotFoundException {
		
		if(this.inputConfigFile != null){
			return this.inputConfigFile;
		}
		
		final String configFilePath = Utils.getFilePathAsString(
				new String[] {
						configData.getWorkingDir(), 
						configData.getConfigurationFolderName(),
						configData.getPackageConfigurationFileName()});
		
		File configFile = new File(configFilePath);
		
		if(!configFile.exists()){
			throw new FileNotFoundException("The configuration file doesn't exist in conf folder");
		}
		
		return configFile;
	}

	public String getPackageName() {
		
		if(Utils.isStringEmpty(packageName)){
			
			this.packageName = findPackageByExtension(".ear");
			
		}
		
		return this.packageName;
	}

	private String findPackageByExtension(final String extension) {
		
//		PathMatcher matcher =
//		    FileSystems.getDefault().getPathMatcher("glob:*.{java,class}");
		

		File dir = new File(configData.getWorkingDir());
		
		File[] list = dir.listFiles(getFileFilterByExtension(extension));

		if(Utils.isArrayEmpty(list)){
			log.info("Was not found any file with extension .ear");
			return null;
		}
		
		return list[0].getName(); //choose the first found file
	}

	private FileFilter getFileFilterByExtension(final String extension) {
		
		return new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				
				return pathname != null && pathname.isFile() && pathname.getName().endsWith(extension);
			}
		};
	}

	public void copyPackageToTempDir() throws IOException {
		
		log.info("Copy package {} to the temporary folder {} ", getPackageName(), getTemporaryPackageFileName());
		
		if(Utils.isStringEmpty(getPackageName())){
			
			throw new FileNotFoundException("Package file is not found!");
		}

		File sourcePackageFile = new File(Utils.getFilePathAsString(new String[] {configData.getWorkingDir(), getPackageName()}));
		
		File tmpPackageFile = new File(getTemporaryPackageFileName());
		
		//create temporally folder if not exists
		tmpPackageFile.mkdirs();
		
		Files.copy(sourcePackageFile.toPath(), tmpPackageFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		
		log.info("Copying files to the temporary folder finnished.");
	}

	private String getTemporaryPackageFileName() {
		return Utils.getFilePathAsString(new String[] {configData.getWorkingDir(), getTemporallyFolderName(), getPackageName()});
	}
	
	private String getTemporaryFolderName() {
		return Utils.getFilePathAsString(new String[] {configData.getWorkingDir(), getTemporallyFolderName()});
	}
	
	private String getInputConfigPackageFile() {
		return Utils.getFilePathAsString(new String[] {configData.getWorkingDir(), configData.getConfigurationFolderName(), configData.getPackageConfigurationFileName()});
	}

	public void repackage() throws IOException, TransformerException {
		
		log.info("Start repackaging of {}", getPackageName());
		
		if(isEarPackage()){
			repackageEar();
		} else if (isJarPackage()){
			repackageJar(Paths.get(getTemporaryPackageFileName()), Paths.get(renamePackage(getTemporaryPackageFileName(), getRepackagSuffix(), "jar")));
		} else if (isWarPackage()){
			
		}
		
		movePackageToTargetFolder();
		
	}

	private void movePackageToTargetFolder() throws IOException {
		Path tmpFolderPath = Paths.get(getTemporaryPackageFileName());
		Path targetFolderPath = Paths.get(getTargetRepackageFolderName(), renamePackage(getPackageName(), getRepackagSuffix(), "ear"));
		
		targetFolderPath.toFile().mkdirs();
		
		log.info(String.format("Move repacked packgae %s to %s", tmpFolderPath.getFileName(), targetFolderPath.getFileName()));
		
		Files.copy(tmpFolderPath, targetFolderPath, StandardCopyOption.REPLACE_EXISTING);
	}

	private String getTargetRepackageFolderName() {
		
		return Utils.getFilePathAsString(new String[] {configData.getWorkingDir(), configData.getTargetFolderName()});
	}

	private boolean isJarPackage() {
		return Utils.isStringEmpty(getPackageName()) ? false : getPackageName().endsWith(".jar");
	}
	
	private boolean isWarPackage() {
		return Utils.isStringEmpty(getPackageName()) ? false : getPackageName().endsWith(".war");
	}

	private boolean isEarPackage() {
		
		return Utils.isStringEmpty(getPackageName()) ? false : getPackageName().endsWith(".ear");
	}
	
	public List<String> listAllPackageFiles(){
		
		List<String> earEntries = null;
		
		File tmpEAR = new File(getTemporaryPackageFileName());
		try (JarFile myDatartEAR = new JarFile(tmpEAR)){
			
			Enumeration<JarEntry> entries = myDatartEAR.entries();
			
			earEntries = new ArrayList<>();
			
			while (entries.hasMoreElements()) {
				JarEntry entry = (JarEntry) entries.nextElement();
				
				if(!entry.isDirectory()){
//					System.out.println(entry.getName());
					earEntries.add(entry.getName());
				}
			}
			
		} catch (Exception e) {
			earEntries = null;
	        e.printStackTrace();
	    }
    
		return earEntries;
	}

	public void copyConfigToPackage(Path pathToPackageConfig) throws IOException {
		

		Path sourceConfigFilePath = Paths.get(configData.getWorkingDir(), configData.getConfigurationFolderName());
		
		copyFileToPackage(sourceConfigFilePath, pathToPackageConfig);
	}

	public void copyFileToPackage(Path soueceFilePath, Path pathToPackageConfig) throws IOException {
		
		Files.copy(soueceFilePath, pathToPackageConfig, StandardCopyOption.REPLACE_EXISTING);
		
	}

	
	public void repackageEar() throws IOException, TransformerException {
		log.info("Start repackaging ear file");
		
		File tmpEAR = new File(getTemporaryPackageFileName());

		List<String> earEntries = listAllPackageFiles();
		
		if(earEntries == null){
			log.warn(String.format("The package %s has no entries!", getPackageName()));
			return;
		}
		
		try( FileSystem fs = FileSystems.newFileSystem(tmpEAR.toPath(), null) ){
			
			Path earDescriptorPath = fs.getPath(configData.getEarDescriptorPath());
			
			modifyEarDescriptor(earDescriptorPath);
			
			Path jarPath;
			
			for(String earEntryName : earEntries){
				
				if(earEntryName.endsWith(".jar")){
					
					jarPath = fs.getPath(earEntryName);
					
					repackageJar(jarPath, fs.getPath(renamePackage(jarPath.toString(), getRepackagSuffix(), "jar")));
					
					
				} 
			}
			
		} //az ted se provedou vsechny zmeny na tomto filesystemu
		
		
		
	}
	
	private void modifyEarDescriptor(Path earDescriptorPath) throws IOException, TransformerException {
		
		Path tmpEarDescriptorPath = Paths.get(getTemporaryFolderName(), configData.getEarPackageDescriptorName());
		
		//copy ear descriptor xml file to tmp folder
		Files.copy(earDescriptorPath, tmpEarDescriptorPath, StandardCopyOption.REPLACE_EXISTING );
		
    	Document xmlDoc = getXmlDocument(tmpEarDescriptorPath.toFile());
    	
    	if(xmlDoc == null){
    		return;
    	}
    	
    	updateEarDescriptorXml(xmlDoc);
    	
    	saveXmlFile(xmlDoc, tmpEarDescriptorPath.toFile());
    	
    	Files.move(tmpEarDescriptorPath, earDescriptorPath, StandardCopyOption.REPLACE_EXISTING);
    	
	}

	private void updateEarDescriptorXml(Document xmlDoc) {
		
		String suffix = getRepackagSuffix();
		
		NodeList dispalyNameTags = xmlDoc.getElementsByTagName("display-name");
		Node displayNameNode = dispalyNameTags.item(0).getFirstChild();
		String dispplayNameValue = displayNameNode.getNodeValue();
		displayNameNode.setNodeValue(dispplayNameValue + "-" + suffix);
		
		NodeList modules = xmlDoc.getElementsByTagName("java");//xmlDoc.getElementsByTagName("module");
		
		
		String jarName;
		String newJarName;
		
		
		//loop for each employee
        for(int i=0; i < modules.getLength(); i++){
        	
        	Node name = modules.item(i).getFirstChild();
             
        	jarName = name.getNodeValue();
        	
        	if(jarName != null){
        		newJarName = renamePackage(jarName, suffix, "jar"); 
        		
        		name.setNodeValue(newJarName);
        		
        	}
        }
        
	}
	
	private void updatePersistenceXml(Document xmlDoc) {
		
		NodeList elementName;
		Node nodeName;
		
		
		elementName = xmlDoc.getElementsByTagName("jta-data-source");
		nodeName = elementName.item(0).getFirstChild();
		nodeName.setNodeValue(configData.getDataSourceName());
		
		NodeList properties = xmlDoc.getElementsByTagName("property");
		Element propertyElement;
		int j = 0;
		
		for (int i = 0; i != properties.getLength() && j != 3; i++) {
			
			propertyElement = (Element) properties.item(i);
			
			if(propertyElement.getAttribute("hibernate.dialect") != null){
				propertyElement.setAttribute("hibernate.dialect", configData.getSqlDriver());
				++j;
			} else if(propertyElement.getAttribute("hibernate.show_sql") != null){
				propertyElement.setAttribute("hibernate.show_sql", Utils.toString(configData.isShowSql()));
				++j;
			} else if(propertyElement.getAttribute("hibernate.format_sql") != null){
				propertyElement.setAttribute("hibernate.format_sql", Utils.toString(configData.isShowSql()));
				++j;
			}
		}
		
	}

	private void repackageJar(Path jarPath, Path newJarPath) throws IOException, TransformerException {
		
		
		
		final String jarName = jarPath.toString();//Utils.getFileName(jarPath);// String.format("%s%n", jarPath.getFileName());
		
		log.info("Start repackaging jar file: {}", jarName);
		
		//String newJarName = renamePackage(jarName, getRepackagSuffix(), "jar");
		
		Path tmpJarPackage = Paths.get(getTemporaryFolderName(), jarName);
		
		tmpJarPackage.toFile().mkdirs();
		
		//upacke jar and rename it
		Files.copy(jarPath, tmpJarPackage, StandardCopyOption.REPLACE_EXISTING);
		
		final String tmpJarPackageName = tmpJarPackage.toString();
		//unpack jar
		try( FileSystem fs2 = FileSystems.newFileSystem(tmpJarPackage, null) ){
			
			//configuration jar
			modifyConfigurationApplication(fs2.getPath(configData.getPackageConfigurationFileName()), tmpJarPackageName);
			
			//persistance jar
			modifyPersistenceApplication(fs2.getPath(RepackageData.METAINF_FOLDER_NAME, configData.getPersistenceXmlFileName()), tmpJarPackageName);
			
			//switchyard project 
			modifySwitchyardApplication(fs2.getPath(RepackageData.METAINF_FOLDER_NAME, configData.getSwitchyardXmlFileName()), tmpJarPackageName);
			
		} //az ted se provedou vsechny zmeny na tomto filesystemu
		
		//pack unnpacked configuration jar package
		Files.copy( tmpJarPackage, jarPath, StandardCopyOption.REPLACE_EXISTING);
			
		//rename package
		Files.move(jarPath, newJarPath, StandardCopyOption.REPLACE_EXISTING);
	}

	/**
	 * 
	 * @param jarName
	 * @param configFilePath
	 * @return return target path to the unpacked config file
	 * @throws IOException
	 */
	private Path isConfigurationPackage(final String jarName, Path configFilePath) throws IOException {
		
		if( jarName.contains(configData.getConfigurationPackageTag())){
			
			try {
				//copy config file from config package jar to temporary folder
				return Files.copy(configFilePath, Paths.get(getTemporaryFolderName(), configData.getPackageConfigurationFileName()), StandardCopyOption.REPLACE_EXISTING);
			} catch (NoSuchFileException e) {
				log.debug(String.format("file %s not found in a package %s", configFilePath.toString(), jarName));
			}
			
			return null; // configration file is not contained in package
		}
		
		return null; //not contains "configuration" in file name
	}
	
	private Path isSwitchyardPackage(final String jarName, Path syConfigFilePath) throws IOException {
		
		try {
			//copy sy config file from config package jar to temporary folder
			return Files.copy(syConfigFilePath, Paths.get(getTemporaryFolderName(), configData.getSwitchyardXmlFileName()), StandardCopyOption.REPLACE_EXISTING);
		} catch (NoSuchFileException e) {
			log.debug(String.format("file %s not found in a package %s", syConfigFilePath.toString(), jarName));
		}
			
		return null; //not contain "switchyard configuration" in a package
	}
	
	private Path isPersistencePackage(final String jarName, Path persistenceConfigFilePath) throws IOException {
		
		try {
			//copy sy config file from config package jar to temporary folder
			return Files.copy(persistenceConfigFilePath, Paths.get(getTemporaryFolderName(), configData.getPersistenceXmlFileName()), StandardCopyOption.REPLACE_EXISTING);
		} catch (NoSuchFileException e) {
			log.debug(String.format("file %s not found in a package %s", persistenceConfigFilePath.toString(), jarName));
		}
			
		return null; //not contain "persistence.xml" in a package
	}
	

	private String renamePackage(String oldPackageName, String suffix, String packageType) {
		final String fullSuffix = new StringBuilder().append("-").append(suffix).append(".").append(packageType).toString();
		final String pattern = "\\."+packageType;
		return oldPackageName.replaceFirst(pattern, fullSuffix);
	}

//	private boolean containConfigurationFile(Path jarPackage) throws IOException {
//	
//		try (JarFile jar = new JarFile(jarPackage.toFile())){
//			
//			Enumeration<JarEntry> entries = jar.entries();
//			
//			while (entries.hasMoreElements()) {
//				JarEntry entry = (JarEntry) entries.nextElement();
//				
//				if(!entry.isDirectory()){
////					System.out.println(entry.getName());
//					if(configData.getPackageConfigurationFileName().equals(entry.getName())){
//						return true;
//					}
//				}
//			}
//			
//		} 
//
//		return false;
//	}
	
	private void modifyPersistenceApplication(Path configFilePath, final String packageName) throws IOException, TransformerException {
		
		Path unpackedTmpPesistenceFilePath = isPersistencePackage(packageName, configFilePath);
		if(unpackedTmpPesistenceFilePath != null) {
	    	
	    	
			Document xmlDoc = getXmlDocument(unpackedTmpPesistenceFilePath.toFile());
			
			if(xmlDoc == null){
				return;
			}
			
			updatePersistenceXml(xmlDoc);
			
			saveXmlFile(xmlDoc, unpackedTmpPesistenceFilePath.toFile());
			
			//move switchyard xml file back into jar
			Files.move(unpackedTmpPesistenceFilePath, configFilePath, StandardCopyOption.REPLACE_EXISTING );
		}
	}


	

	private void modifyConfigurationApplication(Path configFilePath, final String packageName) throws IOException {
		
		//I can modify only one configuration file; Can be only one configuration file per switchyard application
		if(!isConfigurationFileModified()) {
			//filter, a configuration package should contain the 'configuration' text
			Path unpackedTmpConfigFilePath = isConfigurationPackage(packageName, configFilePath);
			if(unpackedTmpConfigFilePath != null ){
			
				
				this.configurationFileModified = true;
				
				
				//overwrite configuration file with that file from conf folder
				Files.copy(Paths.get(getInputConfigPackageFile()), unpackedTmpConfigFilePath, StandardCopyOption.REPLACE_EXISTING );
				
				//move config from temporary folder back to the configuration jar package
				Files.move(unpackedTmpConfigFilePath, configFilePath, StandardCopyOption.REPLACE_EXISTING);
			}
		}
	}

	private boolean isConfigurationFileModified() {
	
		return this.configurationFileModified;
	}

	private void modifySwitchyardApplication(Path syFilePath, final String packageName) throws IOException, TransformerException {
		
		Path unpackedTmpSyFilePath = isSwitchyardPackage(packageName, syFilePath);
		if(unpackedTmpSyFilePath != null) {
	    	
	    	
			Document xmlDoc = getXmlDocument(unpackedTmpSyFilePath.toFile());
			
			if(xmlDoc == null){
				return;
			}
			
			updateSwitchyardXml(xmlDoc, getRepackagSuffix());
			
			saveXmlFile(xmlDoc, unpackedTmpSyFilePath.toFile());
			
			//move switchyard xml file back into jar
			Files.move(unpackedTmpSyFilePath, syFilePath, StandardCopyOption.REPLACE_EXISTING );
		}
	}


	private String getRepackagSuffix() {
		if( Utils.isStringEmpty(configData.getEnvironment()) && Utils.isStringEmpty(configData.getSegment()) ) {
			throw new NullPointerException("environment and segment attributs are null or empty.");
		}
		
		return new StringBuilder(configData.getEnvironment()).append(configData.getSegment()).toString();
	}
	
	/**
	 * 
	 * @return getEnvironment() + getSegment() + "Tmp"
	 */
	public String getTemporallyFolderName() {
		
		return getRepackagSuffix() + "Tmp";
	}
	
	private Document getXmlDocument(File xmlFile){
		
		// create a new DocumentBuilderFactory
	      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

	      try {
	         // use the factory to create a documentbuilder
	         DocumentBuilder builder = factory.newDocumentBuilder();

	         // create a new document from input stream
//	         FileInputStream fis = new FileInputStream("Student.xml");
	         Document doc = builder.parse(xmlFile);
	         
	         return doc;
	      } catch (Exception e) {
	    	  e.printStackTrace();
	      }
	      
	      return null;
	}

	private void updateSwitchyardXml(Document xmlDoc, String suffix) {
		
		String syAppName = xmlDoc.getDocumentElement().getAttribute(configData.getSwitchyardNameAttributName());
		String syTargetNamspace = xmlDoc.getDocumentElement().getAttribute(configData.getSwitchyardTargetNamespaceAttributName());
		
		String newSyAppName = syAppName + "-" + suffix;
		String newSyTargetNamspace = syTargetNamspace.replaceFirst(syAppName, syAppName + "-" + suffix);
		
		
		xmlDoc.getDocumentElement().setAttribute(configData.getSwitchyardNameAttributName(), newSyAppName);
		xmlDoc.getDocumentElement().setAttribute(configData.getSwitchyardTargetNamespaceAttributName(), newSyTargetNamspace);
		
		
		Element compositeElement = (Element) xmlDoc.getElementsByTagName(configData.getSwitchyardCompositeElementName()).item(0);
		//Element compositeNameNode = (Element) compositeNode.getAttributes().getNamedItem("name");//setAttribute("name", newSyAppName);
		compositeElement.setAttribute(configData.getSwitchyardNameAttributName(), newSyAppName);
		//Element compositeTargetNamespaceNode = (Element) compositeNode.getAttributes().getNamedItem("targetNamespace");//setAttribute("name", newSyAppName);
		compositeElement.setAttribute(configData.getSwitchyardTargetNamespaceAttributName(), newSyTargetNamspace);
		
//		xmlDoc.getDocumentElement().setAttribute("targetNamespace", newSyTargetNamspace);
		
		return;
	}
	
	private void saveXmlFile(Document xmlDoc, File xmlFile) throws TransformerException {
		
		//write the updated document to file or console
		xmlDoc.getDocumentElement().normalize();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(xmlDoc);
        
        StreamResult result = new StreamResult(xmlFile);
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(source, result);
        
        System.out.println("XML file updated successfully");
        
	}

	public void removeTemporaryFolder() {
		
		log.info("Removing the temporary folder");
		
		try {
			Utils.deleteDirectory(new File(getTemporaryFolderName()));
		} catch (Exception e) {
			log.error("Error", e);
		}
	}
	
	public void removeTargetFolder() {
		
		try {
			Utils.deleteDirectory(new File(getTargetRepackageFolderName()));
		} catch (Exception e) {
			log.error("Error", e);
		}
	}

}
