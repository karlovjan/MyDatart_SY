package cz.datart.jboss.deployer;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ExtractJavaPackageFilesTest {

	private static final String WORKING_DIR = "d:\\Tmp\\Wildfly\\myDatartDist\\testEnvPackage";
	private static final String MYDATART_TEMPLATE_EAR_NAME = "myDatart.ear";
	private static final String MYDATART_EAR = WORKING_DIR + "\\" + MYDATART_TEMPLATE_EAR_NAME;
//	private static final String CONFIG_FILE_PATH = WORKING_DIR + "\\config";
//	private static final String CONFIG_FILE_NAME = "application.properties";
	
	protected String environment = "test3";
	protected String segment = "CZ";
	
//	@Test
	public void copyEARTemplateTest() throws IOException {
		
		String newEarName = "myDatart" + "-" + environment+segment + ".ear";
		
//		Files.copy(Paths.get(MYDATART_EAR), Paths.get(WORKING_DIR + "\\" + newEarName), StandardCopyOption.REPLACE_EXISTING); 
		
		Path source = Paths.get(MYDATART_EAR);
		
		Files.move(source, source.resolveSibling(newEarName), StandardCopyOption.REPLACE_EXISTING);
	}
	
//	@Test
	public void zipFileTest() {
		
		
		String earFile = "/home/mbaros/Projects/Datart/Backup/myDatart.ear";
		
		String confTmpJarFile = "/home/mbaros/Projects/Datart/Backup/Tmp/myDatart_Conf_"+environment+segment+".jar";
		String confAppTmpFile = "/home/mbaros/Projects/Datart/Backup/Tmp/application.properties";
		
		File confTmpFile = new File(confTmpJarFile);
		confTmpFile.mkdirs();
		
		File confAppTmpPropertiesFile = new File(confAppTmpFile);
		
		try (ZipFile zipF = new ZipFile(earFile)){
			
			///lib/myDatart-configuration-1.0.jar
			
			ZipEntry confEntry = zipF.getEntry("lib/myDatart-configuration-1.0.jar");//zipF.getEntry("application.properties");//zipF.getEntry("lib/myDatart-configuration-1.0.jar");
			
			System.out.println(confEntry.getName());
			
			InputStream confStream = zipF.getInputStream(confEntry);
			
			//FileInputStream in = new FileInputStream("myTestUnzipConfigFile.jar");
			
			
			
			Files.copy(confStream, confTmpFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//extract application.properties file
			try (ZipFile confJarFile = new ZipFile(confTmpFile)) {
				ZipEntry appConEntry = confJarFile.getEntry("application.properties");
				InputStream appConfStream = confJarFile.getInputStream(appConEntry);
				
				Files.copy(appConfStream, confAppTmpPropertiesFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
				
			} catch (Exception ex){
				ex.printStackTrace();
			}
			
			String oldtext = "";
			try (BufferedReader confReader = new BufferedReader(new FileReader(confAppTmpPropertiesFile))) {
				
				String line = "";
				while ((line = confReader.readLine())!= null)  {
					oldtext += line + System.lineSeparator();
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			
			String updatedText = oldtext.replaceAll("test", environment + segment);
			
			try (FileWriter confWriter = new FileWriter(confAppTmpPropertiesFile)){
			
				confWriter.write(updatedText);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
//			while (zipF.entries().hasMoreElements()) {
//				ZipEntry type = (ZipEntry) zipF.entries().nextElement();
//				
//				System.out.println(type.getName());
//			}
		
		
		
	}
	
//	@Test
	public void jarFile_extractJavaPackageFiles_test() {

		File tmpEAR = new File(MYDATART_EAR);
		try (JarFile myDatartEAR = new JarFile(tmpEAR)){
			
			String jarEntry = "myDatart-chunks-1.0.jar";
			
			String libEntry = "lib";
			
			ZipEntry libFolder = myDatartEAR.getEntry(libEntry);
			System.out.println(libFolder.getName());
			
			JarEntry subModulJar = myDatartEAR.getJarEntry(jarEntry);
			System.out.println(subModulJar.getName());
			
			jarEntry = "lib/myDatart-configuration-1.0.jar";
			
			JarEntry subModulConfigJar = myDatartEAR.getJarEntry(jarEntry);
			System.out.println(subModulConfigJar.getName());
			
//			Enumeration<JarEntry> e = myDatartEAR.entries();
//		    while (e.hasMoreElements()) {
//		      JarEntry je = e.nextElement();
//		      System.out.println(je.getName());
//		      
//		    }
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void nonExitingPackageFile(){
	
		RepackageData data = new RepackageData();
		
		try( FileSystem fs = FileSystems.newFileSystem(Paths.get(getTestWorkingDir(data), MYDATART_TEMPLATE_EAR_NAME), null) ){
			
			Path failedPath = fs.getPath("wrongFileName.no");
			
			failedPath.toFile().exists(); //trows UnsupportedOperationException
			
		} catch (IOException e) {
			
			e.printStackTrace();
		} 
	}
	
//	@Test
	public void renameEarModule(){
		

		String environment = "test5";
		String segment = "CZ";
		
		String jarEntryName = "myDatart-chunks-1.0.jar";
		String earFile = "/home/mbaros/Projects/Datart/Backup/myDatart.ear";
		
		String newJarMOdulName = "myDatart-chunks-" + environment + segment + ".jar";
		
		    Path earFilePath = Paths.get(earFile);
		    
		    try( FileSystem fs = FileSystems.newFileSystem(earFilePath, null) ){
		    	
		    	Path oldJarPath = fs.getPath(jarEntryName);
		    	Path newJarPath = fs.getPath(newJarMOdulName);
		    	
//		    	Files.move(newJarPath, oldJarPath);
//		    	Files.move(oldJarPath, newJarPath);
		    	
		    	String oldSubModulName = "myDatart-configuration-1.0.jar";
		    	String newSubModulName = "myDatart-configuration-" + environment + segment + ".jar";
		    	
		    	Path oldSubModulPath = fs.getPath("lib/"+oldSubModulName);
		    	Path newSubModulPath = fs.getPath("lib/"+newSubModulName);
		    	
//		    	Files.move(oldSubModulPath, newSubModulPath);
		    	Files.move(newSubModulPath, oldSubModulPath);
		    	
		    } catch (Exception e) {
		        
		        e.printStackTrace();
		    }
		    
	}
	
	@Test(expected=FileNotFoundException.class)
	public void repackageFullProcessTest_ConfigFileNotFound() throws IOException{
		
		
		RepackageData data = new RepackageData();
		
		String workingdir = new StringBuilder(data.getWorkingDir()).append(File.separator).append("noexistingfolder").toString();
		data.setWorkingDir(workingdir); //default is .
		
		Repackage repackage = new Repackage();
		repackage.setDatamodel(data);
		
		assertEquals(workingdir, data.getWorkingDir());
		
		repackage.loadPackageEnvironmentData();
		
	}
	
	@Test
	public void repackageFullProcessTest() throws Throwable{
		
		
		RepackageData data = new RepackageData();
		
		String workingdir = getTestWorkingDir(data);
		
		data.setWorkingDir(workingdir); //default is .
		
		Repackage repackage = new Repackage();
		repackage.setDatamodel(data);
		
		assertEquals(workingdir, data.getWorkingDir());
		
		repackage.loadPackageEnvironmentData();
		
		assertEquals("test1", data.getEnvironment());
		assertEquals("cz", data.getSegment());
		
		
		assertEquals("test1czTmp", new File(repackage.getTemporallyFolderName()).getName());
		
		String packageName = repackage.getPackageName();
		
		assertEquals("myDatart.ear", packageName);
		
		repackage.copyPackageToTempDir();
		
		File tmpPackageFile = new File(Utils.getFilePathAsString(new String[] {data.getWorkingDir(), repackage.getTemporallyFolderName(), repackage.getPackageName()}));
		assertTrue(tmpPackageFile.exists());
		
		repackage.repackage();
		
		String targetPackageName = "myDatart-test1cz.ear";
		File targetPackageFile = new File(Utils.getFilePathAsString(new String[] {data.getWorkingDir(), data.getTargetFolderName(), targetPackageName}));
		assertTrue(targetPackageFile.exists());
		
		repackage.removeTemporaryFolder();
		
		File tmpPackageFolder = new File(Utils.getFilePathAsString(new String[] {data.getWorkingDir(), repackage.getTemporallyFolderName()}));
		assertFalse(tmpPackageFolder.exists());
		
		repackage.removeTargetFolder();
		
		File tmpTargetFolder = new File(Utils.getFilePathAsString(new String[] {data.getWorkingDir(), data.getTargetFolderName()}));
		assertFalse(tmpTargetFolder.exists());
	}

	private String getTestWorkingDir(RepackageData data) {
		return String.join(File.separator, data.getWorkingDir(), "src", "test", "resources");
	}
	
	
//	@Test
	public void listAllEarFiles(){
		
//		List<String>  
		File tmpEAR = new File(MYDATART_EAR);
		try (JarFile myDatartEAR = new JarFile(tmpEAR)){
			
			Enumeration<JarEntry> entries = myDatartEAR.entries();
			
			while (entries.hasMoreElements()) {
				JarEntry entry = (JarEntry) entries.nextElement();
				
//				if(!entry.isDirectory()){
					System.out.println(entry.getName());
//				}
			}
			
		} catch (Exception e) {
	        
	        e.printStackTrace();
	    }
    
	}
	
//	@Test
	public void modifySwitchyardDescriptor() throws IOException, TransformerException{
	
		String tmpWorkingDir = WORKING_DIR + File.separator + environment+segment + File.separator;
				
		String switchyardXmlFileName = "switchyard.xml";
		String switchyardXmlPathinJar = "META-INF" + File.separator + "switchyard.xml";
		
		String switchyardJarNameInEar = "myDatart-chunks-configuration-1.0.jar";
		
		File switchyardJarTmpFile = Paths.get(tmpWorkingDir + switchyardJarNameInEar).toFile();
		File switchyardXmlTmpFile = Paths.get(tmpWorkingDir + switchyardXmlFileName).toFile();
    	
		switchyardXmlTmpFile.mkdirs();
    	
		File tmpEAR = new File(MYDATART_EAR);

		try( FileSystem fs = FileSystems.newFileSystem(tmpEAR.toPath(), null) ){
	    	
			
	    	Path switchyardJarPath = fs.getPath(switchyardJarNameInEar);

	    	//copy switchyard xml file to tmp folder
			Files.copy(switchyardJarPath, switchyardJarTmpFile.toPath(), StandardCopyOption.REPLACE_EXISTING );
			
	    	try( FileSystem syJarfs = FileSystems.newFileSystem(switchyardJarTmpFile.toPath(), null) ){
		    	
				
		    	Path switchyardXmlPath = syJarfs.getPath(switchyardXmlPathinJar);
	    	
				//copy switchyard xml file to tmp folder
				Files.copy(switchyardXmlPath, switchyardXmlTmpFile.toPath(), StandardCopyOption.REPLACE_EXISTING );
		    	
		    	Document xmlDoc = getXmlDocument(switchyardXmlTmpFile);
		    	
		    	if(xmlDoc == null){
		    		return;
		    	}
		    	
		    	updateSwitchyardXml(xmlDoc, environment+segment);
		    	
		    	saveXmlFile(xmlDoc, switchyardXmlTmpFile);
		    	
		    	//move switchyard xml file back into jar
		    	Files.move(switchyardXmlTmpFile.toPath(), switchyardXmlPath, StandardCopyOption.REPLACE_EXISTING );
	    	
	    	} catch (Exception e) {
		        
		        e.printStackTrace();
		    } 
	    	
	    	//move switchyard jar file back into ear
	    	Files.move(switchyardJarTmpFile.toPath(), switchyardJarPath, StandardCopyOption.REPLACE_EXISTING );
	    	
		} catch (Exception e) {
	        
	        e.printStackTrace();
	    } finally {
	    	//TODO delete tmp dir
	    }
	}


	private void updateSwitchyardXml(Document xmlDoc, String suffix) {
		
		String syAppName = xmlDoc.getDocumentElement().getAttribute("name");
		String syTargetNamspace = xmlDoc.getDocumentElement().getAttribute("targetNamespace");
		
		String newSyAppName = syAppName + "-" + suffix;
		String newSyTargetNamspace = syTargetNamspace.replaceFirst(syAppName, syAppName + "-" + suffix);
		
		
		xmlDoc.getDocumentElement().setAttribute("name", newSyAppName);
		xmlDoc.getDocumentElement().setAttribute("targetNamespace", newSyTargetNamspace);
		
		
		
		Element compositeElement = (Element) xmlDoc.getElementsByTagName("sca:composite").item(0);
		//Element compositeNameNode = (Element) compositeNode.getAttributes().getNamedItem("name");//setAttribute("name", newSyAppName);
		compositeElement.setAttribute("name", newSyAppName);
		//Element compositeTargetNamespaceNode = (Element) compositeNode.getAttributes().getNamedItem("targetNamespace");//setAttribute("name", newSyAppName);
		compositeElement.setAttribute("targetNamespace", newSyTargetNamspace);
		
//		xmlDoc.getDocumentElement().setAttribute("targetNamespace", newSyTargetNamspace);
		
		return;
	}

//	@Test
	public void modifyEarDescriptor(){
	
		String tmpWorkingDir = WORKING_DIR + File.separator + environment+segment + File.separator;
				
		String earDescriptor = "META-INF" + File.separator + "application.xml";
		
		File tmpEAR = new File(MYDATART_EAR);
		
		try( FileSystem fs = FileSystems.newFileSystem(tmpEAR.toPath(), null) ){
	    	
			
	    	Path earConfPath = fs.getPath(earDescriptor);
	    	
	    	File earConfFile = Paths.get(tmpWorkingDir + "earConfTmp.xml").toFile();
	    	
	    	earConfFile.mkdirs();
	    	
	    	Files.copy(earConfPath, earConfFile.toPath(), StandardCopyOption.REPLACE_EXISTING );
	    	
	    	Document xmlDoc = getXmlDocument(earConfFile);
	    	
	    	if(xmlDoc == null){
	    		return;
	    	}
	    	
	    	updateEarDescription(xmlDoc, environment+segment);
	    	
	    	saveXmlFile(xmlDoc, earConfFile);
	    	
	    	Files.move(earConfFile.toPath(), earConfPath, StandardCopyOption.REPLACE_EXISTING );
	    	
		} catch (Exception e) {
	        
	        e.printStackTrace();
	    } finally {
	    	//TODO delete tmp dir
	    }
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

	private void updateEarDescription(Document xmlDoc, String suffix) {
		
		NodeList dispalyNameTags = xmlDoc.getElementsByTagName("display-name");
		Node displayNameNode = dispalyNameTags.item(0).getFirstChild();
		String dispplayNameValue = displayNameNode.getNodeValue();
		displayNameNode.setNodeValue(dispplayNameValue + "-" + environment+segment);
		
		NodeList modules = xmlDoc.getElementsByTagName("java");//xmlDoc.getElementsByTagName("module");
		
		final String fullSuffix = "-"+suffix + ".jar";
		String jarName;
		String newJarName;
		
		
		//loop for each employee
        for(int i=0; i < modules.getLength(); i++){
        	
        	Node name = modules.item(i).getFirstChild();
             
        	jarName = name.getNodeValue();
        	
        	if(jarName != null){
        		newJarName = jarName.replaceFirst("\\.jar", fullSuffix);
        		
        		name.setNodeValue(newJarName);
        		
        	}
        }
        
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
	
//	@Test
	public void readEarDescriptor(){
		
		String earDescriptor = "META-INF/application.xml";
		
		File tmpEAR = new File(MYDATART_EAR);
		//Path earFilePath = Paths.get(earFile);
	    
 	    try( FileSystem fs = FileSystems.newFileSystem(tmpEAR.toPath(), null) ){
	    	
	    	Path earConfPath = fs.getPath(earDescriptor);
//	    	Files.copy(earConfPath, Paths.get(first, more) new FileOutputStream("earConfTmp.xml"), StandardCopyOption.REPLACE_EXISTING );
	    	File earConfFile = Paths.get(WORKING_DIR + "\\earConfTmp.xml").toFile();
	    	
	    	Files.copy(earConfPath, earConfFile.toPath(), StandardCopyOption.REPLACE_EXISTING );
	    	
			// create a new DocumentBuilderFactory
		      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		      try {
		         // use the factory to create a documentbuilder
		         DocumentBuilder builder = factory.newDocumentBuilder();

		         // create a new document from input stream
//		         FileInputStream fis = new FileInputStream("Student.xml");
		         Document doc = builder.parse(earConfFile);
		         
		         // get the first element
		         Element element = doc.getDocumentElement();
		         element.normalize();

		         // get all child nodes
		         NodeList nodes = element.getChildNodes();

		         // print the text content of each child
		         for (int i = 0; i < nodes.getLength(); i++) {
		            System.out.println("" + nodes.item(i).getTextContent());
		            
		         }
		         
		          //updateElementValue(doc, "java", environment+segment);
		          
		          String elementName = "java";
		          String suffix = environment+segment;
		          
				NodeList javaModules = doc.getElementsByTagName(elementName );
		          //Element emp = null;
		          //loop for each employee
		          for(int i=0; i < javaModules.getLength(); i++){
		          	Node name = javaModules.item(0).getFirstChild();
		               
		          	String jarName = name.getNodeValue();
		          	if(jarName != null){
		          		
						name.setNodeValue(jarName.replaceFirst("1.0", suffix));
		          	} else {
		          		System.out.println("jarName is null");
		          	}
		          }
		          
		         
		        //write the updated document to file or console
	            doc.getDocumentElement().normalize();
	            TransformerFactory transformerFactory = TransformerFactory.newInstance();
	            Transformer transformer = transformerFactory.newTransformer();
	            DOMSource source = new DOMSource(doc);
	            
	            StreamResult result = new StreamResult(earConfFile);
	            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	            transformer.transform(source, result);
	            System.out.println("XML file updated successfully");
	            
		      } catch (Exception ex) {
		         ex.printStackTrace();
		      }

		      
			
		} catch (Exception e) {
	        
	        e.printStackTrace();
	    }
    
	}
	
	private void updateElementValue(Document doc, String elementName, String suffix) {
        NodeList javaModules = doc.getElementsByTagName(elementName);
        //Element emp = null;
        //loop for each employee
        for(int i=0; i < javaModules.getLength(); i++){
        	Node name = javaModules.item(0).getFirstChild();
             
        	String jarName = name.getNodeValue();
        	if(jarName != null){
        		name.setNodeValue(jarName.replaceFirst("1.0", suffix));
        	} else {
        		System.out.println("jarName is null");
        	}
        }
    }
	
	public void commandJarExtractEar(){
		
		ProcessBuilder pb =
				   new ProcessBuilder("jar", "xf", MYDATART_EAR);
		
	}
}
