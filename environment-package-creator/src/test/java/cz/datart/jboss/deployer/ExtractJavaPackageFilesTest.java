package cz.datart.jboss.deployer;

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.junit.Test;

public class ExtractJavaPackageFilesTest {

	private static final String WORKING_DIR = "d:\\Tmp\\Wildfly\\myDatartDist\\testEnvPackage";
	private static final String MYDATART_TEMPLATE_EAR_NAME = "myDatart.ear";
	private static final String MYDATART_EAR = WORKING_DIR + "\\" + MYDATART_TEMPLATE_EAR_NAME;
	private static final String CONFIG_FILE_PATH = WORKING_DIR + "\\config";
	private static final String CONFIG_FILE_NAME = "application.properties";
	
	protected String environment = "test3";
	protected String segment = "CZ";
	
	@Test
	public void copyEARTemplateTest() throws IOException {
		
		String newEarName = "myDatart" + "-" + environment+segment + ".ear";
		
//		Files.copy(Paths.get(MYDATART_EAR), Paths.get(WORKING_DIR + "\\" + newEarName), StandardCopyOption.REPLACE_EXISTING); 
		
		Path source = Paths.get(MYDATART_EAR);
		
		Files.move(source, source.resolveSibling(newEarName), StandardCopyOption.REPLACE_EXISTING);
	}
	
	@Test
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
	
	@Test
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
	
	@Test
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
		        // TODO Auto-generated catch block
		        e.printStackTrace();
		    }
		    
	}
	
	@Test
	public void createEarModule(){
		
    
	}
	
	
	public void commandJarExtractEar(){
		
		ProcessBuilder pb =
				   new ProcessBuilder("jar", "xf", MYDATART_EAR);
		
	}
}
