package cz.datart.jboss.deployer;

import java.io.File;

public class RepackageData {

	public final static String METAINF_FOLDER_NAME = "META-INF";
	
	private String workingDir = "."; //actual folder, where is tha program started from.
	private String configurationFolderName = "conf"; //a folder where is palced the configuration file for sy app
	private String targetFolderName = "dist"; //a folder where is palced the configuration file for sy app
	
	private String environment; //environment of the package in which the package is deployed
	private String segment; //CZ or SK
	
	private String switchyardXmlFileName = "switchyard.xml";
	private String switchyardNameAttributName = "name";
	private String switchyardTargetNamespaceAttributName = "targetNamespace";
	
	private String earPackageDescriptorName = "application.xml";
	private String earPackageDescriptorDisplayNameElement = "display-name";
	private String earPackageDescriptorJavaElement = "java";
	
	
	private String packageConfigurationFileName = "application.properties";

	public RepackageData() {
		
	}

	public RepackageData(String pEnvironment, String pSegment) {
		
		setEnvironment(pEnvironment);
		setSegment(pSegment);
	}
	
	public String getWorkingDir() {
		return workingDir;
	}


	public void setWorkingDir(String workingDir) {
		this.workingDir = workingDir;
	}

	/**
	 * 
	 * @return "conf"
	 */
	public String getConfigurationFolderName() {
		return configurationFolderName;
	}


	public void setConfigurationFolderName(String configurationFolderName) {
		this.configurationFolderName = configurationFolderName;
	}

	/**
	 * 
	 * @return "dist"
	 */
	public String getTargetFolderName() {
		return targetFolderName;
	}


	public void setTargetFolderName(String targetFolderName) {
		this.targetFolderName = targetFolderName;
	}


	public String getEnvironment() {
		return environment;
	}


	public void setEnvironment(String environment) {
		this.environment = environment;
	}


	public String getSegment() {
		return segment;
	}


	public void setSegment(String segment) {
		this.segment = segment;
	}

	/**
	 * 
	 * @return switchyard.xml
	 */
	public String getSwitchyardXmlFileName() {
		return switchyardXmlFileName;
	}


	public void setSwitchyardXmlFileName(String switchyardXmlFileName) {
		this.switchyardXmlFileName = switchyardXmlFileName;
	}


	public String getSwitchyardNameAttributName() {
		return switchyardNameAttributName;
	}


	public void setSwitchyardNameAttributName(String switchyardNameAttributName) {
		this.switchyardNameAttributName = switchyardNameAttributName;
	}


	public String getSwitchyardTargetNamespaceAttributName() {
		return switchyardTargetNamespaceAttributName;
	}


	public void setSwitchyardTargetNamespaceAttributName(String switchyardTargetNamespaceAttributName) {
		this.switchyardTargetNamespaceAttributName = switchyardTargetNamespaceAttributName;
	}

	/**
	 * 
	 * @return "application.xml"
	 */
	public String getEarPackageDescriptorName() {
		return earPackageDescriptorName;
	}


	public void setEarPackageDescriptorName(String earPackageDescriptorName) {
		this.earPackageDescriptorName = earPackageDescriptorName;
	}


	public String getEarPackageDescriptorDisplayNameElement() {
		return earPackageDescriptorDisplayNameElement;
	}


	public void setEarPackageDescriptorDisplayNameElement(String earPackageDescriptorDisplayNameElement) {
		this.earPackageDescriptorDisplayNameElement = earPackageDescriptorDisplayNameElement;
	}


	public String getEarPackageDescriptorJavaElement() {
		return earPackageDescriptorJavaElement;
	}


	public void setEarPackageDescriptorJavaElement(String earPackageDescriptorJavaElement) {
		this.earPackageDescriptorJavaElement = earPackageDescriptorJavaElement;
	}

	/**
	 * 
	 * @return "application.properties"
	 */
	public String getPackageConfigurationFileName() {
		return packageConfigurationFileName;
	}


	public void setPackageConfigurationFileName(String packageConfigurationFileName) {
		this.packageConfigurationFileName = packageConfigurationFileName;
	}

	public String getSwitchyardCompositeElementName() {
		
		return "sca:composite";
	}

	/**
	 * 
	 * @return METAINF_FOLDER_NAME, "/", getEarPackageDescriptorName()
	 */
	public String getEarDescriptorPath() {
		
		return Utils.getFilePathAsString(new String[] {METAINF_FOLDER_NAME, "/", getEarPackageDescriptorName()});
	}

	/**
	 * 
	 * @return "configuration"
	 */
	public String getConfigurationPackageTag() {
		
		return "configuration";
	}

	
	
	
}
