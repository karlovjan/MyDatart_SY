package cz.datart.jboss.myDatart.chunks.config.jpa.dao;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;

@Dependent
@Transactional
public abstract class AbstractDAOImpl<T extends Serializable> implements Serializable, AbstractDAO<T>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4827756917846496269L;

	
	private final Class<T> clazz;
	
	@Inject
	private Logger log;
	
	@Inject
	@ChunkConfigurationDb
	private EntityManager em;
	
	@PostConstruct
	public void testInit () {
		log.info(String.format("%s DAO impl is created..", clazz.getSimpleName()));
	    return;
	}
	
	public AbstractDAOImpl(Class<T> clazz) {
		this.clazz = clazz;
	}

	@Override
	public T find(Object id) {
		return em.find(clazz, id);
	}
	
	@Override
	public void persist(final T entity) {
		em.persist(entity);
		
		//JTA transaction.  When the transaction commits, the persistence context is flushed to the datasource (entity objects are detached but may still be referenced by application code).
		//em.flush(); //propagate changes to the DB
	}
	
	@Override
	public void update(T entity) {
		
		if(entity != null){
			em.merge(entity);
			//JTA transaction.  When the transaction commits, the persistence context is flushed to the datasource (entity objects are detached but may still be referenced by application code).
			//em.flush(); //propagate changes to the DB
		}
	}
	
	@Override
	public List<T> findAll() {
		final CriteriaQuery<T> criteriaQuery =
				em.getCriteriaBuilder().createQuery(clazz);
				
		criteriaQuery.select(criteriaQuery.from(clazz));
				
		return em.createQuery(criteriaQuery).getResultList();
	}
	
	@Override
	public void deleteAll() {
		
		//You should upgrade your hibernate version to a version that implements JPA 2.1 (Hibernate starts JPA 2.1 on version 4.3.11.Final 
				
//		final CriteriaDelete<T> criteriaDelete =
//				em.getCriteriaBuilder().createCriteriaDelete(clazz);
//				
//		criteriaDelete.from(clazz);
//				
//		em.createQuery(criteriaDelete).executeUpdate();

		final CriteriaQuery<T> criteriaDelete =
				em.getCriteriaBuilder().createQuery(clazz);
				
		criteriaDelete.from(clazz);
		
		final String deleteString = "DELETE FROM " + clazz.getSimpleName(); //chunk_groups
		
		 em.createQuery(deleteString).executeUpdate();
	}
	
	@Override
	public void delete(Object id) {
//		//Delete entity using JP QL
//		Query query = manager.createNativeQuery("DELETE FROM DEPARTMENT WHERE ID = " + departmentId);
//		query.executeUpdate();
		
		final T entity = find(id);
		if(entity != null){
			em.remove(entity);
			//JTA transaction.  When the transaction commits, the persistence context is flushed to the datasource (entity objects are detached but may still be referenced by application code).
			//em.flush(); //propagate changes to the DB
		}
	}
	/*
	public Member findByEmail(String email) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Member> criteria = cb.createQuery(Member.class);
        Root<Member> member = criteria.from(Member.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(member).where(cb.equal(member.get(Member_.email), email));
        criteria.select(member).where(cb.equal(member.get("email"), email));
        return em.createQuery(criteria).getSingleResult();
    }

    public List<Member> findAllOrderedByName() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Member> criteria = cb.createQuery(Member.class);
        Root<Member> member = criteria.from(Member.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(member).orderBy(cb.asc(member.get(Member_.name)));
        criteria.select(member).orderBy(cb.asc(member.get("name")));
        return em.createQuery(criteria).getResultList();
    }
    */

}
