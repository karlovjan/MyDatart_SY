package cz.datart.jboss.myDatart.chunks.config.jpa.dao;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;


//@Dependent reference tohoto beanu je drzena ve tride ktera injectuje EntityManager
@ApplicationScoped
public class TransactedEntityManagerProducer {

	
	@Produces
	@PersistenceContext(unitName="ChunkConfigurationPU", type = PersistenceContextType.TRANSACTION) 
	protected EntityManager entityManager;
}
