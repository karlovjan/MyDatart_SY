package cz.datart.jboss.myDatart.chunks;

public class ChunkVersion {

	private String oldVersion;
	private String newVersion;
	
	public ChunkVersion() {
		
	}
	
	public ChunkVersion(String pOldVersion, String pNewVersion) {
		this.oldVersion = pOldVersion;
		this.newVersion = pNewVersion;
	}

	public String getOldVersion() {
		return oldVersion;
	}

	public void setOldVersion(String oldVersion) {
		this.oldVersion = oldVersion;
	}

	public String getNewVersion() {
		return newVersion;
	}

	public void setNewVersion(String newVersion) {
		this.newVersion = newVersion;
	}
	
}
