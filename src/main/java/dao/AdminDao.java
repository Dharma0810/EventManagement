package dao;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import dto.Admin;

public class AdminDao {

	public EntityManager getEntityManager() {
		return Persistence.createEntityManagerFactory("amit").createEntityManager();
	}
	
	public Admin saveAdmin(Admin admin) {
		
		EntityManager em = getEntityManager();
		
		em.getTransaction().begin();
		em.persist(admin);
		em.getTransaction().commit();
		
		return admin;
	}
	
	public Admin findAdmin(int adminId) {
		
		EntityManager em = getEntityManager();

		Admin admin = em.find(Admin.class, adminId);
		
		if(admin != null)
			return admin;
		
		return null;
	}
	
	public Admin updateAdmin(Admin admin, int id) {
		
		EntityManager em = getEntityManager();

		Admin exAdmin = em.find(Admin.class, id);
		
		if(exAdmin != null) {
			
			admin.setAdminId(id);
			
			em.getTransaction().begin();
			em.merge(admin);
			em.getTransaction().commit();
			
			return admin;
		}
		return null;
	}
	
	public Admin removeAdmin(int id) {
		
		EntityManager em = getEntityManager();

		Admin exAdmin = em.find(Admin.class, id);

		if(exAdmin != null) {
			
			em.getTransaction().begin();
			em.remove(exAdmin);
			em.getTransaction().commit();
			
			return exAdmin;
		}
		return null;
	}
}
