package dao;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import dto.ClientEvent;

public class ClientEventDao {

	public EntityManager getEntityManager() {
		return Persistence.createEntityManagerFactory("amit").createEntityManager();
	}

	public ClientEvent saveClientEvent(ClientEvent clientEvent) {

		EntityManager em = getEntityManager();

		em.getTransaction().begin();
		em.persist(clientEvent);
		em.getTransaction().commit();

		return clientEvent;
	}

	public ClientEvent findClientEvent(int clientEventId) {

		EntityManager em = getEntityManager();

		ClientEvent clientEvent = em.find(ClientEvent.class, clientEventId);

		if (clientEvent != null)
			return clientEvent;

		return null;
	}

	public ClientEvent updateClientEvent(ClientEvent clientEvent, int id) {

		EntityManager em = getEntityManager();

		ClientEvent exClientEvent = em.find(ClientEvent.class, id);

		if (exClientEvent != null) {

			clientEvent.setClientEventId(id);

			em.getTransaction().begin();
			em.merge(clientEvent);
			em.getTransaction().commit();

			return clientEvent;
		}
		return null;
	}

	public ClientEvent removeClientEvent(int id) {

		EntityManager em = getEntityManager();

		ClientEvent exClientEvent = em.find(ClientEvent.class, id);

		if (exClientEvent != null) {

			em.getTransaction().begin();
			em.remove(exClientEvent);
			em.getTransaction().commit();

			return exClientEvent;
		}
		return null;
	}
}
