package dao;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import dto.ClientService;

public class ClientServiceDao {

	public EntityManager getEntityManager() {
		return Persistence.createEntityManagerFactory("amit").createEntityManager();
	}
	
public ClientService saveClientService(ClientService clientService) {
		
		EntityManager em = getEntityManager();
		
		em.getTransaction().begin();
		em.persist(clientService);
		em.getTransaction().commit();
		
		return clientService;
	}
	
	public ClientService findClientService(int clientServiceId) {
		
		EntityManager em = getEntityManager();

		ClientService clientService = em.find(ClientService.class, clientServiceId);
		
		if(clientService != null)
			return clientService;
		
		return null;
	}
	
	public ClientService updateClientService(ClientService clientService, int id) {
		
		EntityManager em = getEntityManager();

		ClientService exClientService = em.find(ClientService.class, id);
		
		if(exClientService != null) {
			
			clientService.setClientServiceId(id);
			
			em.getTransaction().begin();
			em.merge(clientService);
			em.getTransaction().commit();
			
			return clientService;
		}
		return null;
	}
	
	public ClientService removeClientService(int id) {
		
		EntityManager em = getEntityManager();

		ClientService exClientService = em.find(ClientService.class, id);

		if(exClientService != null) {
			
			em.getTransaction().begin();
			em.remove(exClientService);
			em.getTransaction().commit();
			
			return exClientService;
		}
		return null;
	}
}
