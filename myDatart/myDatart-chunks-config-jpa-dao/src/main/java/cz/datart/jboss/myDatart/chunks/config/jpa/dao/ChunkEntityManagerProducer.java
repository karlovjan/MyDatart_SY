package cz.datart.jboss.myDatart.chunks.config.jpa.dao;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;


//@Dependent reference tohoto beanu je drzena ve tride ktera injectuje EntityManager
@ApplicationScoped
public class ChunkEntityManagerProducer {

//	@Inject
//	@ChunkConfigurationDb
//	private EntityManagerFactory emFactory_ChunkDB;
	
	@Produces
	@ChunkConfigurationDb
	@PersistenceContext(unitName="myDatart-ChunkConfigurationPU", type = PersistenceContextType.TRANSACTION) 
	protected EntityManager entityManager;
	
//	@Produces
//	@RequestScoped
//	@ChunkConfigurationDb
//	public EntityManager createChunkEntityManager() {
//		return emFactory_ChunkDB.createEntityManager();
//	}
}
