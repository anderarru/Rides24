import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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

public class GauzatuEragiketaMockBlackTest {
	
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
	// Test 1: username != null, amount > 0, deposit != null, user != null, currentMoney >= 0
	public void test1() {
		String username = "user1";
		double amount = 100.0;
		boolean deposit = true;

		User user = new Traveler(username,"a");
		user.setMoney(100);
		Mockito.when(db.find(User.class, username)).thenReturn(user);

		sut.open();
		boolean result = sut.gauzatuEragiketa(username, amount, deposit);
		sut.close();

		assertEquals(true, result);
		assertEquals(200, user.getMoney(), 0);
	}
	
	@Test
	// Test 2: username != null, amount > 0, deposit != null, user != null, currentMoney >= 0
	public void test2() {
		String username = "user2";
		double amount = 50.0;
		boolean deposit = false;

		User user = new Traveler(username,"a");
		user.setMoney(100);
		Mockito.when(db.find(User.class, username)).thenReturn(user);

		sut.open();
		boolean result = sut.gauzatuEragiketa(username, amount, deposit);
		sut.close();

		assertEquals(true, result);
		assertEquals(50, user.getMoney(), 0);
	}
	
	@Test
	// Test 3: username == null
	public void test3() {
		String username = null;
		double amount = 100.0;
		boolean deposit = true;

		sut.open();
		boolean result = sut.gauzatuEragiketa(username, amount, deposit);
		sut.close();

		assertEquals(false, result);
	}
	
	@Test
	// Test 4: amount <= 0
	public void test4() {
		String username = "user3";
		double amount = -50.0;
		boolean deposit = true;

		User user = new Traveler(username,"a");
		user.setMoney(100);
		Mockito.when(db.find(User.class, username)).thenReturn(user);

		sut.open();
		boolean result = sut.gauzatuEragiketa(username, amount, deposit);
		sut.close();

		assertEquals(false, result);
	}
	
	@Test
	// Test 5: deposit == null
	public void test5() {
		String username = "user4";
		double amount = 50.0;
		Boolean deposit = null;

		User user = new Traveler(username,"a");
		user.setMoney(100);
		Mockito.when(db.find(User.class, username)).thenReturn(user);

		sut.open();
		boolean result = sut.gauzatuEragiketa(username, amount, deposit);
		sut.close();

		assertEquals(false, result);
	}
	
	@Test
	// Test 6: user == null
	public void test6() {
		String username = "user5";
		double amount = 100.0;
		boolean deposit = true;

		Mockito.when(db.find(User.class, username)).thenReturn(null);

		sut.open();
		boolean result = sut.gauzatuEragiketa(username, amount, deposit);
		sut.close();

		assertEquals(false, result);
	}
	
	@Test
	// Test 7: currentMoney < 0
	public void test7() {
		String username = "user6";
		double amount = 100.0;
		boolean deposit = true;

		User user = new Traveler(username,"a");
		user.setMoney(-50);
		Mockito.when(db.find(User.class, username)).thenReturn(user);

		sut.open();
		boolean result = sut.gauzatuEragiketa(username, amount, deposit);
		sut.close();

		assertEquals(false, result);
	}
	
	@Test
	// Test 8: username != null, amount > 0, deposit != null, user != null, currentMoney >= 0, amount > currentMoney
	public void test8() {
	    String username = "user7";
	    double amount = 200.0;
	    boolean deposit = false;

	    User user = new Traveler(username,"a");
	    user.setMoney(100);
	    Mockito.when(db.find(User.class, username)).thenReturn(user);

	    sut.open();
	    boolean result = sut.gauzatuEragiketa(username, amount, deposit);
	    sut.close();

	    assertEquals(true, result); 
	    assertEquals(0, user.getMoney(), 0);
	}
	
	@Test
	// Test 9: username != null, amount > 0, deposit != null, user != null, currentMoney >= 0, amount <= currentMoney
	public void test9() {
		String username = "user8";
		double amount = 50.0;
		boolean deposit = false;

		User user = new Traveler(username,"a");
		user.setMoney(100);
		Mockito.when(db.find(User.class, username)).thenReturn(user);

		sut.open();
		boolean result = sut.gauzatuEragiketa(username, amount, deposit);
		sut.close();

		assertEquals(true, result);
		assertEquals(50, user.getMoney(), 0);
	}
	
	@Test
	// Test 10: username != null, amount > 0, deposit != null, user != null, currentMoney >= 0
	public void test10() {
		String username = "user9";
		double amount = 100.0;
		boolean deposit = true;

		User user = new Traveler(username,"a");
		user.setMoney(100);
		Mockito.when(db.find(User.class, username)).thenReturn(user);

		sut.open();
		boolean result = sut.gauzatuEragiketa(username, amount, deposit);
		sut.close();

		assertEquals(true, result);
		assertEquals(200, user.getMoney(), 0);
	}
}
