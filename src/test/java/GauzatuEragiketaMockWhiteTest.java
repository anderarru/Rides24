
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import dataAccess.DataAccess;
import domain.Traveler;
import domain.User;

public class GauzatuEragiketaMockWhiteTest {
	
	static DataAccess sut;
	
	protected MockedStatic<Persistence> persistenceMock;

	@Mock
	protected EntityManagerFactory entityManagerFactory;
	@Mock
	protected EntityManager db;
	@Mock
    protected EntityTransaction et;
	

	@Before
    public void init() {
        MockitoAnnotations.openMocks(this);
        persistenceMock = Mockito.mockStatic(Persistence.class);
		persistenceMock.when(() -> Persistence.createEntityManagerFactory(Mockito.any()))
        .thenReturn(entityManagerFactory);
        
        Mockito.doReturn(db).when(entityManagerFactory).createEntityManager();
		Mockito.doReturn(et).when(db).getTransaction();
	    sut = new DataAccess(db);
    }
	
	@After
    public void tearDown() {
		persistenceMock.close();
    }
	
	@Test
	// Test 1: The User is null. The test must return false.
	public void test1() {
		try {
			// Define parameters
			String username = null;
			double amount = 3.4;
			boolean deposit = true;

			// Configure the state through mocks
			Mockito.when(db.find(User.class, username)).thenReturn(null);

			// Invoke System Under Test (sut)
			sut.open();
			boolean result = sut.gauzatuEragiketa(username, amount, deposit);

			// Verify the results
			assertEquals(false, result);
		} catch (Exception e) {
			fail();
		} finally {
			sut.close();
		}
	}
	
	@Test
	// Test 2: The User does not exist in the DB. The test must return false.
	public void test2() {
		try {
			// Define parameters
			String username = "EzNago";
			double amount = 40;
			boolean deposit = true;

			// Configure the state through mocks
			Mockito.when(db.find(User.class, username)).thenReturn(null);

			// Invoke System Under Test (sut)
			sut.open();
			boolean result = sut.gauzatuEragiketa(username, amount, deposit);

			// Verify the results
			assertEquals(false, result);
		} catch (Exception e) {
			fail();
		} finally {
			sut.close();
		}
	}
	
	@Test
	public void test3() {
	    try {
	        // Define parameters
	        String username = "proba";
	        double amount = 40;
	        boolean deposit = true;

	        // Configure the state through mocks
	        User user = new Traveler(username, "a");
	        user.setMoney(100);
	        Mockito.when(db.find(User.class, username)).thenReturn(user);

	        // Invoke System Under Test (sut)
	        sut.open();
	        boolean result = sut.gauzatuEragiketa(username, amount, deposit);

	        // Verify the results
	        assertEquals(true, result);
	        assertEquals(140, user.getMoney(), 0); // Verifica que el dinero se ha incrementado correctamente
	    } catch (Exception e) {
	        fail();
	    } finally {
	        sut.close();
	    }
	}

	@Test
	// Test 4: The User exists in the DB and amount > currentMoney. The test must return true.
	public void test4() {
		try {
			// Define parameters
			String username = "proba";
			double amount = 40;
			boolean deposit = false;

			// Configure the state through mocks
			User user = new Traveler(username,"a");
			user.setMoney(30);
			Mockito.when(db.find(User.class, username)).thenReturn(user);

			// Invoke System Under Test (sut)
			sut.open();
			boolean result = sut.gauzatuEragiketa(username, amount, deposit);

			// Verify the results
			assertEquals(true, result);
			assertEquals(0, user.getMoney(), 0);
		} catch (Exception e) {
			fail();
		} finally {
			sut.close();
		}
	}
	
	@Test
	// Test 5: The User exists in the DB and amount < currentMoney. The test must return true.
	public void test5() {
		try {
			// Define parameters
			String username = "proba";
			double amount = 40;
			boolean deposit = false;

			// Configure the state through mocks
			User user = new Traveler(username,"a");
			user.setMoney(100);
			Mockito.when(db.find(User.class, username)).thenReturn(user);

			// Invoke System Under Test (sut)
			sut.open();
			boolean result = sut.gauzatuEragiketa(username, amount, deposit);

			// Verify the results
			assertEquals(true, result);
			assertEquals(60, user.getMoney(), 0);
		} catch (Exception e) {
			fail();
		} finally {
			sut.close();
		}
	}
}
