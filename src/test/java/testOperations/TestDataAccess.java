package testOperations;

import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import configuration.ConfigXML;
import domain.Driver;
import domain.Ride;
import domain.Traveler;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;


public class TestDataAccess {
	protected  EntityManager  db;
	protected  EntityManagerFactory emf;

	ConfigXML  c=ConfigXML.getInstance();


	public TestDataAccess()  {

		System.out.println("TestDataAccess created");

		//open();

	}


	public void open(){


		String fileName=c.getDbFilename();

		if (c.isDatabaseLocal()) {
			emf = Persistence.createEntityManagerFactory("objectdb:"+fileName);
			db = emf.createEntityManager();
		} else {
			Map<String, String> properties = new HashMap<String, String>();
			properties.put("javax.persistence.jdbc.user", c.getUser());
			properties.put("javax.persistence.jdbc.password", c.getPassword());

			emf = Persistence.createEntityManagerFactory("objectdb://"+c.getDatabaseNode()+":"+c.getDatabasePort()+"/"+fileName, properties);

			db = emf.createEntityManager();
		}
		System.out.println("TestDataAccess opened");


	}
	public void close(){
		db.close();
		System.out.println("TestDataAccess closed");
	}

	public boolean removeDriver(String name) {
		System.out.println(">> TestDataAccess: removeDriver");
		Driver d = db.find(Driver.class, name);
		if (d!=null) {
			db.getTransaction().begin();
			db.remove(d);
			db.getTransaction().commit();
			return true;
		} else
			return false;
	}
	public Driver createDriver(String name, String pass) {
		System.out.println(">> TestDataAccess: addDriver");
		Driver driver=null;
		db.getTransaction().begin();
		try {
			driver=new Driver(name,pass);
			db.persist(driver);
			db.getTransaction().commit();
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return driver;
	}
	public boolean existDriver(String email) {
		return  db.find(Driver.class, email)!=null;


	}

	public Driver addDriverWithRide(String name, String from, String to,  Date date, int nPlaces, float price) {
		System.out.println(">> TestDataAccess: addDriverWithRide");
		Driver driver=null;
		db.getTransaction().begin();
		try {
			driver = db.find(Driver.class, name);
			if (driver==null) {
				System.out.println("Entra en null");
				driver=new Driver(name,null);
				db.persist(driver);
			}
			driver.addRide(from, to, date, nPlaces, price);
			db.getTransaction().commit();
			System.out.println("Driver created "+driver);

			return driver;

		}
		catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}


	public boolean existRide(String name, String from, String to, Date date) {
		System.out.println(">> TestDataAccess: existRide");
		Driver d = db.find(Driver.class, name);
		if (d!=null) {
			return d.doesRideExists(from, to, date);
		} else
			return false;
	}
	public Ride removeRide(String name, String from, String to, Date date ) {
		System.out.println(">> TestDataAccess: removeRide");
		Driver d = db.find(Driver.class, name);
		if (d!=null) {
			db.getTransaction().begin();
			Ride r= d.removeRide(from, to, date);
			db.getTransaction().commit();
			System.out.println("created rides" +d.getCreatedRides());
			return r;

		} else
			return null;

	}

	public Driver createDriverWithMoney(String name, String pass, double money) {
		System.out.println(">> TestDataAccess: createDriverWithMoney");
		Driver driver=null;
		db.getTransaction().begin();
		try {
			driver=new Driver(name,pass);
			driver.setMoney(money);
			db.persist(driver);
			db.getTransaction().commit();
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return driver;
	}
	public Driver getUser(String username) {
		System.out.println(">> TestDataAccess: getUser");
		Driver d = db.find(Driver.class, username);
		if (d!=null) {
			return d;
		} else
			return null;
	}


	public boolean addTraveler(String username, String password) {
	    db.getTransaction().begin(); // Start the transaction here
	    try {
	        // Retrieve existing driver and traveler within the transaction
	        Driver existingDriver = db.find(Driver.class, username);
	        Traveler existingTraveler = db.find(Traveler.class, username);

	        // If either a driver or traveler with the same username exists, return false
	        if (existingDriver != null || existingTraveler != null) {
	            db.getTransaction().rollback(); // Rollback transaction before returning
	            return false;
	        }

	        // Create a new traveler if no existing user is found
	        Traveler traveler = new Traveler(username, password);
	        db.persist(traveler);
	        db.getTransaction().commit(); // Commit the transaction
	        return true;
	    } catch (Exception e) {
	        e.printStackTrace();
	        db.getTransaction().rollback(); // Ensure rollback in case of any exceptions
	        return false;
	    }
	}




	public Traveler getTraveler(String erab) {
		TypedQuery<Traveler> query = db.createQuery("SELECT t FROM Traveler t WHERE t.username = :username",
				Traveler.class);
		query.setParameter("username", erab);
		List<Traveler> resultList = query.getResultList();
		if (resultList.isEmpty()) {
			return null;
		} else {
			return resultList.get(0);
		}
	}

	public Driver getDriver(String erab) {
		TypedQuery<Driver> query = db.createQuery("SELECT d FROM Driver d WHERE d.username = :username", Driver.class);
		query.setParameter("username", erab);
		List<Driver> resultList = query.getResultList();
		if (resultList.isEmpty()) {
			return null;
		} else {
			return resultList.get(0);
		}
	}

	public boolean removeTraveler(String name) {
		System.out.println(">> TestDataAccess: removeTraveler");
		Driver d = db.find(Driver.class, name);
		if (d!=null) {
			db.getTransaction().begin();
			db.remove(d);
			db.getTransaction().commit();
			return true;
		} else
			return false;
	}

	public Ride createRide(String from, String to, Date date, int nPlaces, float price, String driverName)
	        throws RideAlreadyExistException, RideMustBeLaterThanTodayException {
	    System.out.println(">> DataAccess: createRide=> from= " + from + " to= " + to + " driver=" + driverName + " date " + date);
	    if (driverName == null || from == null) return null;
	    try {
	        if (new Date().compareTo(date) > 0) {
	            throw new RideMustBeLaterThanTodayException("CreateRideGUI.ErrorRideMustBeLaterThanToday");
	        }

	        db.getTransaction().begin();
	        Driver driver = db.find(Driver.class, driverName);
	        if (driver.doesRideExists(from, to, date)) {
	            db.getTransaction().commit(); // Commit and throw exception - potential bug
	            throw new RideAlreadyExistException("DataAccess.RideAlreadyExist");
	        }
	        Ride ride = driver.addRide(from, to, date, nPlaces, price);
	        db.persist(driver);
	        db.getTransaction().commit();

	        return ride;
	    } catch (NullPointerException e) {
	        return null;
	    }
	}
	
	
	public void updateTraveler(Traveler traveler) {
		try {
			db.getTransaction().begin();
			db.merge(traveler);
			db.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			db.getTransaction().rollback();
		}
	}



	}



