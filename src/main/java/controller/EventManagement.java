package controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import dao.AdminDao;
import dao.ClientDao;
import dao.ClientEventDao;
import dao.ClientServiceDao;
import dao.ServiceDao;
import dto.Admin;
import dto.Client;
import dto.ClientEvent;
import dto.ClientService;
import dto.EventType;
import dto.Service;

public class EventManagement {

	AdminDao adao = new AdminDao();
	ServiceDao sdao = new ServiceDao();
	ClientDao cdao = new ClientDao();
	ClientEventDao cedao = new ClientEventDao();
	ClientServiceDao csdao = new ClientServiceDao();

	public static void main(String[] args) {
		
	}

	public Admin saveAdmin() {

		Admin admin = new Admin();
		Scanner sc = new Scanner(System.in);

		System.out.println("Enter Admin Name :");
		admin.setAdminName(sc.next());
		System.out.println("Enter Admin Email :");
		admin.setAdminMail(sc.next());
		System.out.println("Enter Admin Password :");
		admin.setAdminPassword(sc.next());
		System.out.println("Enter Admin Contact :");
		admin.setAdminContact(sc.nextLong());

		return adao.saveAdmin(admin);
	}

	public Admin adminLogin() {

		Scanner sc = new Scanner(System.in);

		System.out.println("Enter Admin Email : ");
		String adminEmail = sc.next();
		System.out.println("Enter Admin Password : ");
		String adminPassword = sc.next();

		Query query = Persistence.createEntityManagerFactory("amit").createEntityManager()
				.createQuery("select a from Admin a where a.adminMail=?1");
		query.setParameter(1, adminEmail);

		Admin exAdmin = null;

		try {
			exAdmin = (Admin) query.getSingleResult();

			if (exAdmin.getAdminPassword().equals(adminPassword))
				return exAdmin;

			return null;

		} catch (NoResultException e) {

			System.out.println("Wrong Email Or Password...!");

			return null;
		}
	}

	public Service saveService() {

		Admin exAdmin = adminLogin();

		if (exAdmin != null) {

			Service service = new Service();
			Scanner sc = new Scanner(System.in);

			System.out.println("Enter Service Name : ");
			service.setServiceName(sc.next());
			System.out.println("Enter Service Cost Per Person :");
			service.setServiceCostPerPerson(sc.nextDouble());
			System.out.println("Enter Service Cost Per Day : ");
			service.setServiceCostPerDay(sc.nextDouble());

			Service savedService = sdao.saveService(service);
			exAdmin.getServices().add(savedService);
			adao.updateAdmin(exAdmin, exAdmin.getAdminId());

			return service;
		}
		return null;
	}

	public List<Service> getAllServices() {

		System.out.println("Enter Admin Credentials to Proceed..");
		Admin exAdmin = adminLogin();

		if (exAdmin != null) {
			Query query = Persistence.createEntityManagerFactory("amit").createEntityManager()
					.createQuery("select s from Service s");
			List<Service> services = query.getResultList();

			return services;
		}
		return null;
	}

	public String updateService() {

		Scanner sc = new Scanner(System.in);

		List<Service> services = getAllServices();

		if (services != null) {

			for (Service s : services) {

				System.out.println("ServiceID     : " + s.getServiceId());
				System.out.println("ServiceName   : " + s.getServiceName());
				System.out.println("CostPerPerson : " + s.getServiceCostPerPerson());
				System.out.println("CostPerDay    : " + s.getServiceCostPerDay() + "\n");
			}

			System.out.print("Enter ServiceID Which You Want To Update : ");

			byte id = sc.nextByte();
			Service tobeUpdated = sdao.findService(id);

			System.out.print("Enter CostPerPerson : ");
			tobeUpdated.setServiceCostPerPerson(sc.nextDouble());
			System.out.print("Enter CostPerDay : ");
			tobeUpdated.setServiceCostPerPerson(sc.nextDouble());

			if (sdao.updateService(tobeUpdated, id) != null)
				return "Updated Successfully";
			return "Invalid Service Id";
		} else
			return "No Services Found";
	}

	public Service deleteService(int serviceId) {

		Scanner sc = new Scanner(System.in);
		Admin exadmin = adminLogin();

		List<Service> services = getAllServices();

		if (exadmin != null) {

			for (Service s : services) {

				System.out.println("ServiceID     : " + s.getServiceId());
				System.out.println("ServiceName   : " + s.getServiceName());
				System.out.println("CostPerPerson : " + s.getServiceCostPerPerson());
				System.out.println("CostPerDay    : " + s.getServiceCostPerDay() + "\n");
			}

			System.out.print("Enter ServiceID Which You Want To Update : ");

			byte id = sc.nextByte();
			Service tobeRemoved = sdao.findService(id);

			services.remove(tobeRemoved);
			exadmin.setServices(services);
			adao.updateAdmin(exadmin, exadmin.getAdminId());

			return tobeRemoved;
		}
		return null;
	}

	public Client saveClient() {

		Client client = new Client();
		Scanner sc = new Scanner(System.in);

		System.out.println("Enter Client Name :");
		client.setClientName(sc.next());
		System.out.println("Enter Client Contact :");
		client.setClientContact(sc.nextLong());
		System.out.println("Enter Client Email :");
		client.setClientMail(sc.next());

		return cdao.saveClient(client);
	}
	
	public Client clientLogin() {
		
		Scanner sc = new Scanner(System.in);

		System.out.println("Enter Client Email : ");
		String clientEmail = sc.next();
		System.out.println("Enter Client Password : ");
		String clientPassword = sc.next();

		Query query = Persistence.createEntityManagerFactory("amit").createEntityManager()
				.createQuery("select c from Client c where c.clientMail=?1");
		query.setParameter(1, clientEmail);

		Client exClient = null;

		try {
			exClient = (Client) query.getSingleResult();

			if (exClient.getPassword().equals(clientPassword))
				return exClient;

			return null;

		} catch (NoResultException e) {

			System.out.println("Wrong Email Or Password...!");

			return null;
		}
	}
	
	public void createClientEvent() {
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Enter Your Credentials to Proceed..!");
		
		Client exClient = clientLogin();
		
		if(exClient != null) {
			
			System.out.println("\n Event List \n");
			
			byte count = 1;
			for (EventType event : EventType.values()) {
				System.out.println(count+". "+event);
				count++;
			}
			
			List<ClientEvent> cevents = new ArrayList<ClientEvent>();
			ClientEvent ce = new ClientEvent();
			
			System.out.print("Enter Event Number : ");
			switch (sc.nextInt()) {
			case 1:{
				System.out.print("Enter No. of People : ");
				byte numberOfPeople = sc.nextByte();
				ce.setClientEventNoOfPeople(numberOfPeople);
				System.out.print("Enter No. of Days : ");
				ce.setClientEventNoOfDays(sc.nextByte());
				System.out.print("Enter Start Date : ");
				ce.setStartDate(LocalDate.parse(sc.next()));
				System.out.print("Enter Event Location : ");
				ce.setClientEventLocation(sc.next());
				ce.setClient(exClient);
				List<ClientService> cservice = listOfService();
				ce.setClientServices(cservice);
				ce.setEventType(EventType.Marriage);
				
				double totalAmount = 0;
				
				for (ClientService cs : cservice) {
					totalAmount += (numberOfPeople*cs.getClientServiceCostPerPerson())+cs.getClientServiceCost();
				}
				
				ce.setClientEventCost(totalAmount);
				
				cedao.saveClientEvent(ce);
				cevents.add(ce);
				exClient.setEvents(cevents);
				cdao.updateClient(exClient, exClient.getClientId());
				break;
			}
			case 2:{
				System.out.print("Enter No. of People : ");
				byte numberOfPeople = sc.nextByte();
				ce.setClientEventNoOfPeople(numberOfPeople);
				System.out.print("Enter No. of Days : ");
				ce.setClientEventNoOfDays(sc.nextByte());
				System.out.print("Enter Start Date : ");
				ce.setStartDate(LocalDate.parse(sc.next()));
				System.out.print("Enter Event Location : ");
				ce.setClientEventLocation(sc.next());
				ce.setClient(exClient);
				List<ClientService> cservice = listOfService();
				ce.setClientServices(cservice);
				ce.setEventType(EventType.Engagement);
				
				double totalAmount = 0;
				
				for (ClientService cs : cservice) {
					totalAmount += (numberOfPeople*cs.getClientServiceCostPerPerson())+cs.getClientServiceCost();
				}
				
				ce.setClientEventCost(totalAmount);
				
				cedao.saveClientEvent(ce);
				cevents.add(ce);
				exClient.setEvents(cevents);
				cdao.updateClient(exClient, exClient.getClientId());
				break;
			}
			case 3:{
				System.out.print("Enter No. of People : ");
				byte numberOfPeople = sc.nextByte();
				ce.setClientEventNoOfPeople(numberOfPeople);
				System.out.print("Enter No. of Days : ");
				ce.setClientEventNoOfDays(sc.nextByte());
				System.out.print("Enter Start Date : ");
				ce.setStartDate(LocalDate.parse(sc.next()));
				System.out.print("Enter Event Location : ");
				ce.setClientEventLocation(sc.next());
				ce.setClient(exClient);
				List<ClientService> cservice = listOfService();
				ce.setClientServices(cservice);
				ce.setEventType(EventType.Birthday);
				
				double totalAmount = 0;
				
				for (ClientService cs : cservice) {
					totalAmount += (numberOfPeople*cs.getClientServiceCostPerPerson())+cs.getClientServiceCost();
				}
				
				ce.setClientEventCost(totalAmount);
				
				cedao.saveClientEvent(ce);
				cevents.add(ce);
				exClient.setEvents(cevents);
				cdao.updateClient(exClient, exClient.getClientId());
				break;
			}
			case 4:{
				System.out.print("Enter No. of People : ");
				byte numberOfPeople = sc.nextByte();
				ce.setClientEventNoOfPeople(numberOfPeople);
				System.out.print("Enter No. of Days : ");
				ce.setClientEventNoOfDays(sc.nextByte());
				System.out.print("Enter Start Date : ");
				ce.setStartDate(LocalDate.parse(sc.next()));
				System.out.print("Enter Event Location : ");
				ce.setClientEventLocation(sc.next());
				ce.setClient(exClient);
				List<ClientService> cservice = listOfService();
				ce.setClientServices(cservice);
				ce.setEventType(EventType.Anniversary);
				
				double totalAmount = 0;
				
				for (ClientService cs : cservice) {
					totalAmount += (numberOfPeople*cs.getClientServiceCostPerPerson())+cs.getClientServiceCost();
				}
				
				ce.setClientEventCost(totalAmount);
				
				cedao.saveClientEvent(ce);
				cevents.add(ce);
				exClient.setEvents(cevents);
				cdao.updateClient(exClient, exClient.getClientId());
				break;
			}
			case 5:{
				System.out.print("Enter No. of People : ");
				byte numberOfPeople = sc.nextByte();
				ce.setClientEventNoOfPeople(numberOfPeople);
				System.out.print("Enter No. of Days : ");
				ce.setClientEventNoOfDays(sc.nextByte());
				System.out.print("Enter Start Date : ");
				ce.setStartDate(LocalDate.parse(sc.next()));
				System.out.print("Enter Event Location : ");
				ce.setClientEventLocation(sc.next());
				ce.setClient(exClient);
				List<ClientService> cservice = listOfService();
				ce.setClientServices(cservice);
				ce.setEventType(EventType.BabyShower);
				
				double totalAmount = 0;
				
				for (ClientService cs : cservice) {
					totalAmount += (numberOfPeople*cs.getClientServiceCostPerPerson())+cs.getClientServiceCost();
				}
				
				ce.setClientEventCost(totalAmount);
				
				cedao.saveClientEvent(ce);
				cevents.add(ce);
				exClient.setEvents(cevents);
				cdao.updateClient(exClient, exClient.getClientId());
				break;
			}
			case 6:{
				System.out.print("Enter No. of People : ");
				byte numberOfPeople = sc.nextByte();
				ce.setClientEventNoOfPeople(numberOfPeople);
				System.out.print("Enter No. of Days : ");
				ce.setClientEventNoOfDays(sc.nextByte());
				System.out.print("Enter Start Date : ");
				ce.setStartDate(LocalDate.parse(sc.next()));
				System.out.print("Enter Event Location : ");
				ce.setClientEventLocation(sc.next());
				ce.setClient(exClient);
				List<ClientService> cservice = listOfService();
				ce.setClientServices(cservice);
				ce.setEventType(EventType.Reunion);
				
				double totalAmount = 0;
				
				for (ClientService cs : cservice) {
					totalAmount += (numberOfPeople*cs.getClientServiceCostPerPerson())+cs.getClientServiceCost();
				}
				
				ce.setClientEventCost(totalAmount);
				
				cedao.saveClientEvent(ce);
				cevents.add(ce);
				exClient.setEvents(cevents);
				cdao.updateClient(exClient, exClient.getClientId());
				break;
			}
			case 7:{
				System.out.print("Enter No. of People : ");
				byte numberOfPeople = sc.nextByte();
				ce.setClientEventNoOfPeople(numberOfPeople);
				System.out.print("Enter No. of Days : ");
				ce.setClientEventNoOfDays(sc.nextByte());
				System.out.print("Enter Start Date : ");
				ce.setStartDate(LocalDate.parse(sc.next()));
				System.out.print("Enter Event Location : ");
				ce.setClientEventLocation(sc.next());
				ce.setClient(exClient);
				List<ClientService> cservice = listOfService();
				ce.setClientServices(cservice);
				ce.setEventType(EventType.NamingCeremony);
				
				double totalAmount = 0;
				
				for (ClientService cs : cservice) {
					totalAmount += (numberOfPeople*cs.getClientServiceCostPerPerson())+cs.getClientServiceCost();
				}
				
				ce.setClientEventCost(totalAmount);
				
				cedao.saveClientEvent(ce);
				cevents.add(ce);
				exClient.setEvents(cevents);
				cdao.updateClient(exClient, exClient.getClientId());
				break;
			}
			case 8:{
				System.out.print("Enter No. of People : ");
				byte numberOfPeople = sc.nextByte();
				ce.setClientEventNoOfPeople(numberOfPeople);
				System.out.print("Enter No. of Days : ");
				ce.setClientEventNoOfDays(sc.nextByte());
				System.out.print("Enter Start Date : ");
				ce.setStartDate(LocalDate.parse(sc.next()));
				System.out.print("Enter Event Location : ");
				ce.setClientEventLocation(sc.next());
				ce.setClient(exClient);
				List<ClientService> cservice = listOfService();
				ce.setClientServices(cservice);
				ce.setEventType(EventType.BachelorParty);
				
				double totalAmount = 0;
				
				for (ClientService cs : cservice) {
					totalAmount += (numberOfPeople*cs.getClientServiceCostPerPerson())+cs.getClientServiceCost();
				}
				
				ce.setClientEventCost(totalAmount);
				
				cedao.saveClientEvent(ce);
				cevents.add(ce);
				exClient.setEvents(cevents);
				cdao.updateClient(exClient, exClient.getClientId());
				break;
			}
			default:
				System.out.println("Enter Correct Event Number...");
				break;
			}
		}
	}
	
	public List<ClientService> listOfService() {
		
		Scanner sc = new Scanner(System.in);
		List<ClientService> cslist = new ArrayList<ClientService>();
				
		List<Service> lservices = getAllServices();
		
		int count = 1;
		System.out.println("----- List of Services -----\n");
		for (Service s : lservices) {
			System.out.println(count+". "+s.getServiceName()+"     "+s.getServiceCostPerPerson()+"     "+s.getServiceCostPerDay());
			count++;
		}
		System.out.println(count+". For Exit");
		
		boolean loop = true;
		
		do {
			
			System.out.println("\nEnter Your Choice to Proceed..!");
			byte choice = sc.nextByte();
			
			switch (choice) {
			case 1:{
				
				Service s = sdao.findService(1);
				
				ClientService cs = new ClientService();
				
				cs.setClientServiceName(s.getServiceName());
				System.out.print("Enter No. of Days : ");
				byte days = sc.nextByte();
				cs.setClientServiceNoOfDays(days);
				cs.setClientServiceCostPerPerson(s.getServiceCostPerPerson());
				cs.setClientServiceCost(days*s.getServiceCostPerDay());
				
				csdao.saveClientService(cs);
				cslist.add(cs);
				
				break;
			}
			case 2:{
				
				Service s = sdao.findService(2);
				
				ClientService cs = new ClientService();
				
				cs.setClientServiceName(s.getServiceName());
				System.out.print("Enter No. of Days : ");
				byte days = sc.nextByte();
				cs.setClientServiceNoOfDays(days);
				cs.setClientServiceCostPerPerson(s.getServiceCostPerPerson());
				cs.setClientServiceCost(days*s.getServiceCostPerDay());
				
				csdao.saveClientService(cs);
				cslist.add(cs);
				break;
			}
			case 3:
				loop = false;

			default:
				System.out.println("Enter Correct Choice...");
			}
			
		} while (loop);
		
		return cslist;
	}
}