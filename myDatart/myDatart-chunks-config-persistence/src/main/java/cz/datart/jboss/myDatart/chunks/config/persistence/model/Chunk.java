package cz.datart.jboss.myDatart.chunks.config.persistence.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="chunks")
public class Chunk implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2024855160588549526L;

	
	@Id
	private String name; //název chunku = id
	@Column
	private int count; //pocet polozek v chunku
	@Column
	private boolean confirmation; //poslat potvrzeni o odeslani chunku do axapty
	@Column
	private int sequence; //pořadí ve kterém se budou odesílat chunky, nejnišší jde první
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Group group;
	
	public Chunk() {
		// It must have a no-argument constructor in at least the protected scope
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public boolean isConfirmation() {
		return confirmation;
	}
	public void setConfirmation(boolean confirmation) {
		this.confirmation = confirmation;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	public Group getGroup() {
		return group;
	}
	public void setGroup(Group group) {
		this.group = group;
	}
	
	@Override
    public boolean equals(Object o) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }
        Chunk chunk = (Chunk) o;
        return Objects.equals( name, chunk.name );
    }

    @Override
    public int hashCode() {
        return Objects.hash( name );
    }
}
