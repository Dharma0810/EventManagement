package dao;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import dto.Client;

public class ClientDao {

	public EntityManager getEntityManager() {
		return Persistence.createEntityManagerFactory("amit").createEntityManager();
	}
	
public Client saveClient(Client client) {
		
		EntityManager em = getEntityManager();
		
		em.getTransaction().begin();
		em.persist(client);
		em.getTransaction().commit();
		
		return client;
	}
	
	public Client findClient(int clientId) {
		
		EntityManager em = getEntityManager();

		Client client = em.find(Client.class, clientId);
		
		if(client != null)
			return client;
		
		return null;
	}
	
	public Client updateClient(Client client, int id) {
		
		EntityManager em = getEntityManager();

		Client exClient = em.find(Client.class, id);
		
		if(exClient != null) {
			
			client.setClientId(id);;
			
			em.getTransaction().begin();
			em.merge(client);
			em.getTransaction().commit();
			
			return client;
		}
		return null;
	}
	
	public Client removeClient(int id) {
		
		EntityManager em = getEntityManager();

		Client exClient = em.find(Client.class, id);

		if(exClient != null) {
			
			em.getTransaction().begin();
			em.remove(exClient);
			em.getTransaction().commit();
			
			return exClient;
		}
		return null;
	}
}