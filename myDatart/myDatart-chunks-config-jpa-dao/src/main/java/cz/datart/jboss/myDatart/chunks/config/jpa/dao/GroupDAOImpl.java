package cz.datart.jboss.myDatart.chunks.config.jpa.dao;

import javax.enterprise.context.Dependent;

import cz.datart.jboss.myDatart.chunks.config.persistence.model.Group;

@Dependent
public class GroupDAOImpl extends AbstractDAOImpl<Group> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -58569002132480601L;

	public GroupDAOImpl() {
		super(Group.class);
	}
}
