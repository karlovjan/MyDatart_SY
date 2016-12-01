package cz.datart.jboss.myDatart.chunks.config;

import java.util.List;

import javax.inject.Inject;
import org.apache.log4j.Logger;
import org.switchyard.component.bean.Service;

import cz.datart.jboss.myDatart.chunks.config.jpa.dao.GroupDAOImpl;
import cz.datart.jboss.myDatart.chunks.config.persistence.model.Group;


@Service(value = ChunkGroupConfigurator.class, name = "ChunkGroupConfiguratorService")
public class ChunkGroupConfiguratorServiceBean implements ChunkGroupConfigurator {

//	private static final Logger LOG = Logger.getLogger(ChunkGroupConfiguratorServiceBean.class);
	@Inject
	private Logger LOG;
	
	@Inject
	private GroupDAOImpl groupDao;
	
//	@PostConstruct
//	public void testInit () {
//	    return;
//	}
	
	@Override
	public void create(Group chunkGroup) {
		
		if(chunkGroup == null){
			LOG.warn("chunk group is null");
			return;
		}
		
		LOG.info(String.format("Create chunk group %s", chunkGroup.getId()));
		
		
		try {
			groupDao.persist(chunkGroup);
			
		} catch (Exception e) {
			LOG.error(String.format("Save chunk group %s config error", chunkGroup.getId()), e);
			
			throw e;
		}
	}

	@Override
	public void update(Group chunkGroup) {
		
		if(chunkGroup == null){
			LOG.warn("chunk group is null");
			return;
		}
		
		LOG.info(String.format("Update chunk group %s", chunkGroup.getId()));
		
		try {
			Group group = (Group)groupDao.find(chunkGroup.getId());
			
			
			//do update only if group exist
			if(group != null){
//				LOG.info("Chunk group does not exist for id: " + chunkGroup.getId());
//				group.setCronExpression(chunkGroup.getCronExpression());
////				group.setId(chunkGroup.getId());
//				group.setResendCount(chunkGroup.getResendCount());
//				group.setStatus(chunkGroup.getStatus());
//				group.setModifiedOn(chunkGroup.getModifiedOn());
//				
//				group.getChunks().clear();
//				
//				group.getChunks().addAll(chunkGroup.getChunks());
				
				groupDao.update(chunkGroup);
				
			} else {
				LOG.warn("Chunk group does not exist for id: " + chunkGroup.getId());
			}
		
			
		} catch (Exception e) {
			String.format("Update chunk group %s config error", chunkGroup.getId());
			
			throw e;
		}
	}

	@Override
	public void delete(String chunkGroupID) {

		LOG.info(String.format("Delete chunk group %s", chunkGroupID));
		
		try {
			groupDao.delete(chunkGroupID);
		} catch (Exception e) {
			String.format("Delete chunk group %s config error", chunkGroupID);
			
			throw e;
		}
	
	}

	@Override
	public List<Group> getAll() {
		
		LOG.info("Get all chunk groups");
		
		try {
			return groupDao.findAll();
		} catch (Exception e) {
			LOG.error("Get all chunk groups error", e);
			
			throw e;
		}
		
//		return new ArrayList<Group>(0);
	}

	@Override
	public Group get(String chunkGroupID) {
		
		LOG.info(String.format("Get chunk group %s", chunkGroupID));
		
		try{
			
			return groupDao.find(chunkGroupID);
		} catch (Exception e){
			LOG.error("Error in finding a chunk group with id: " + chunkGroupID, e);
			
			throw e;
		}
		
	}

}
