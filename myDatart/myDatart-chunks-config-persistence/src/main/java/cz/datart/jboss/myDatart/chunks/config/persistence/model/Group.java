package cz.datart.jboss.myDatart.chunks.config.persistence.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="chunk_groups")
public class Group implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1303884377580134191L;
	
	@Id
	private String id;
	@Column
	private String cronExpression;
	@Column
	private int resendCount; //počet odeslání chunku na eshop po prvním neúspěšném odeslání chunku
	@Column
	@OneToMany(mappedBy="group", cascade = CascadeType.ALL, fetch = FetchType.EAGER,  orphanRemoval = true)
	@OrderBy("sequence")
	private List<Chunk> chunks = new ArrayList<>();
	
	@Column
	@Enumerated(EnumType.STRING)
	private ChunkStatus status; 
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar startExecutionTime;
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar endExecutionTime;
	
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar modifiedOn;
	
	public Group() {
		// It must have a no-argument constructor in at least the protected scope
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCronExpression() {
		return cronExpression;
	}
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
	public List<Chunk> getChunks() {
		return chunks;
	}
	
	public void addChunk(Chunk chunk) {
		chunks.add( chunk );
        chunk.setGroup( this );
    }

    public void removeChunk(Chunk chunk) {
    	chunks.remove( chunk );
    	chunk.setGroup( null );
    }
	public ChunkStatus getStatus() {
		return status;
	}
	public void setStatus(ChunkStatus status) {
		this.status = status;
	}
	public int getResendCount() {
		return resendCount;
	}
	public void setResendCount(int resendCount) {
		this.resendCount = resendCount;
	}
	public Calendar getStartExecutionTime() {
		return startExecutionTime;
	}
	public void setStartExecutionTime(Calendar startExecutionTime) {
		this.startExecutionTime = startExecutionTime;
	}
	public Calendar getEndExecutionTime() {
		return endExecutionTime;
	}
	public void setEndExecutionTime(Calendar endExecutionTime) {
		this.endExecutionTime = endExecutionTime;
	}
	public Calendar getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(Calendar modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
}
