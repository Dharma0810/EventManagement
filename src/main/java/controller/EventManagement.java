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

		EventManagement em = new EventManagement();

		em.createClientEvent();
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
		System.out.println("Enter Password :");
		client.setPassword(sc.next());

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

	public Client createClientEvent() {

		Scanner sc = new Scanner(System.in);

		System.out.println("Enter Your Credentials to Proceed..!");

		Client exClient = clientLogin();

		if (exClient != null) {

			System.out.println("\n Event List \n");

			byte count = 1;
			for (EventType ce : EventType.values()) {
				System.out.println(count + ". " + ce);
				count++;
			}

			List<ClientEvent> cces = new ArrayList<ClientEvent>();
			ClientEvent ce = new ClientEvent();

			System.out.print("Enter Event Number : ");
			switch (sc.nextInt()) {
			case 1:
				ce.setEventType(EventType.Anniversary);
				break;
			case 2:
				ce.setEventType(EventType.BabyShower);
				break;
			case 3:
				ce.setEventType(EventType.BachelorParty);
				break;
			case 4:
				ce.setEventType(EventType.Birthday);
				break;
			case 5:
				ce.setEventType(EventType.Engagement);
				break;
			case 6:
				ce.setEventType(EventType.Marriage);
				break;
			case 7:
				ce.setEventType(EventType.NamingCeremony);
				break;
			case 8:
				ce.setEventType(EventType.Reunion);
				break;
			default:
				ce.setEventType(EventType.Birthday);
				break;			
			}
			System.out.println("enter the ce location");
			String ceLocation = sc.next();
			ce.setClientEventLocation(ceLocation);

			System.out.println("enter the " + ce.getEventType() + " year");
			int year = sc.nextInt();
			System.out.println("enter the " + ce.getEventType() + " month");
			int month = sc.nextInt();
			System.out.println("enter the " + ce.getEventType() + " date");
			int day = sc.nextInt();
			ce.setStartDate(LocalDate.of(year, month, day));

			System.out.println("enter number of days for the ce");
			ce.setClientEventNoOfDays(sc.nextInt());

			System.out.println("enter the number of people that will be attending the ce");
			ce.setClientEventNoOfPeople(sc.nextInt());

			ClientEvent savedEvent = cedao.saveClientEvent(ce);

			savedEvent.setClient(exClient);
			exClient.getEvents().add(savedEvent);

			return cdao.updateClient(exClient, exClient.getClientId());
		}
		return null;
	}

	public void addEventServices() {
		
		Scanner sc = new Scanner(System.in);
		
		Client client = clientLogin();
		if (client != null) {
			for (ClientEvent event : client.getEvents()) {
				System.out.println("Event Id   " + "Event type   " + "Event location");
				System.out.println(
						event.getClientEventId() + " " + event.getEventType() + " " + event.getClientEventLocation());
			}

			System.out.println("Enter the event id you want to add services to");
			int eventid = sc.nextInt();
			ClientEvent event = cedao.findClientEvent(eventid);
			if (event != null) {
				Admin admin = adminLogin();
				if (admin != null) {
					System.out.println("ener the number of services you want to add to the event");
					int count = sc.nextInt();
					while (count > 0) {

						for (Service cs : admin.getServices()) {
							System.out.println(cs.getServiceId() + " " + cs.getServiceName());
						}
						System.out.println("enter the service id you want to choose");
						int serviceId = sc.nextInt();

						Service exService = sdao.findService(serviceId);
						ClientService cs = new ClientService();
//						cs.setClientServiceCost(exService.getServiceCostPerDay() * exService.getServiceCostPerPerson() * event.getClientEventNoOfDays());
						cs.setClientServiceName(exService.getServiceName());
						cs.setClientServiceNoOfDays(event.getClientEventNoOfDays());
						cs.setClientServiceCostPerPerson(exService.getServiceCostPerPerson());

						if (exService.getServiceCostPerPerson() == 0) {
							cs.setClientServiceCost(exService.getServiceCostPerDay() * event.getClientEventNoOfDays());
						} else {
							cs.setClientServiceCost(exService.getServiceCostPerDay() * event.getClientEventNoOfDays()
									* exService.getServiceCostPerPerson());

						}
						event.setClientEventCost(event.getClientEventCost() + cs.getClientServiceCost());
						event.getClientServices().add(cs);
						cedao.updateClientEvent(event, event.getClientEventId());
						count--;

					}
				} // admin credentials are wrong to get all the services
			} // event does not exist with given event id

		} // client login failed
	}

	public ClientEvent deleteEventService() {
		
		Scanner sc = new Scanner(System.in);
		
		Client client = clientLogin();
		if (client != null) {
			for (ClientEvent event : client.getEvents()) {
				System.out.println(event.getClientEventId()+" "+event.getEventType());
			}
			
			
			System.out.println("choose the  event from which you want to remove a service");
			int eventId = sc.nextInt();
			ClientEvent event = cedao.findClientEvent(eventId);
			List<ClientService> services = event.getClientServices();
			for(ClientService service: services) {
				System.out.println(service.getClientServiceId()+" "+service.getClientServiceName());
			}
			System.out.println("enter the service id you want to delete from event");
			int sid = sc.nextInt();
			for(ClientService service: services) {
				if(service.getClientServiceId()==sid) {
					services.remove(service);
				}
			}
			event.setClientServices(services);
			
			cedao.updateClientEvent(event,event.getClientEventId());
			return event;
		}
		return null; // wrong client credentials
	}
}