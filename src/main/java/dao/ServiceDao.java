package dao;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import dto.Service;

public class ServiceDao {
	
	public EntityManager getEntityManager() {
		return Persistence.createEntityManagerFactory("amit").createEntityManager();
	}
	
	public Service saveService(Service service) {
		
		EntityManager em = getEntityManager();
		
		em.getTransaction().begin();
		em.persist(service);
		em.getTransaction().commit();
		
		return service;
	}
	
	public Service findService(int serviceId) {
		
		EntityManager em = getEntityManager();

		Service service = em.find(Service.class, serviceId);
		
		if(service != null)
			return service;
		
		return null;
	}
	
	public Service updateService(Service service , int serviceId) {
		
		EntityManager em = getEntityManager();
		
		Service exService = em.find(Service.class, serviceId);
		
		if(exService != null) {
			
			service.setServiceId(serviceId);
			
			em.getTransaction().begin();
			em.merge(service);
			em.getTransaction().commit();
			
			return service;
		}
		return null;
	}
	
	public Service removeService(int serviceId) {
		
		EntityManager em = getEntityManager();
		
		Service service = em.find(Service.class, serviceId);
		
		if(service != null) {
			
			em.getTransaction().begin();
			em.remove(service);
			em.getTransaction().commit();
			
			return service;
		}
		return null;
	}
}