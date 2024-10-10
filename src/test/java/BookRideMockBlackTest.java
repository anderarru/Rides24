

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import dataAccess.DataAccess;
import domain.Booking;
import domain.Driver;
import domain.Ride;
import domain.Traveler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookRideMockBlackTest {
	/*
    static DataAccess sut;

    protected MockedStatic<Persistence> persistenceMock;
    @Mock protected EntityManager db;
    @Mock protected EntityTransaction et;
    @Mock protected EntityManagerFactory entityManagerFactory;
    @Mock TypedQuery<Traveler> typedQuery;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
        persistenceMock = Mockito.mockStatic(Persistence.class);
        persistenceMock.when(() -> Persistence.createEntityManagerFactory(Mockito.any())).thenReturn(entityManagerFactory);

        when(entityManagerFactory.createEntityManager()).thenReturn(db);
        when(db.getTransaction()).thenReturn(et);
        sut = new DataAccess(db);
    }

    @After
    public void tearDown() {
        persistenceMock.close();
    }

    @Test
    public void testTravelerNull() {
        String username = "nonexistentUser";
        Ride ride = new Ride("Start", "End", new Date(), 2, 5.0, new Driver("driver1", "password"));

        when(db.find(Traveler.class, username)).thenReturn(null);

        sut.open();
        boolean result = sut.bookRide(username, ride, 2, 5.0);
        sut.close();

        assertEquals(false, result);
    }

    @Test
    public void testInsufficientSeats() {
        String username = "traveler1";
        Traveler traveler = new Traveler(username, "password");
        Ride ride = new Ride("Start", "End", new Date(), 1, 5.0, new Driver("driver1", "password"));  // Only 1 seat available

        when(db.find(Traveler.class, username)).thenReturn(traveler);

        sut.open();
        boolean result = sut.bookRide(username, ride, 2, 5.0);  // Trying to book 2 seats
        sut.close();

        assertEquals(false, result);
    }

    @Test
    public void testInsufficientBalance() {
        String username = "traveler1";
        Traveler traveler = new Traveler(username, "password");
        traveler.setMoney(0);  // No balance
        Ride ride = new Ride("Start", "End", new Date(), 3, 10.0, new Driver("driver1", "password"));

        when(db.find(Traveler.class, username)).thenReturn(traveler);

        sut.open();
        boolean result = sut.bookRide(username, ride, 3, 0.0);  // Attempting to book 3 seats
        sut.close();

        assertEquals(false, result);
    }

    @Test
    public void successfulBooking() {
        try {
            // Setup traveler and ride details
            Driver driver = new Driver("Driver Test", "");
            Ride ride = new Ride("Donostia", "Zarautz", new SimpleDateFormat("dd/MM/yyyy").parse("05/10/2026"), 100, 20.5, driver);
            Traveler traveler = new Traveler("Julen", "0000");
            traveler.setMoney(50);

            List<Traveler> travelers = new ArrayList<>();
            travelers.add(traveler);

            // Mock database interactions
            when(db.createQuery(anyString(), any(Class.class))).thenReturn(typedQuery);
            when(typedQuery.getResultList()).thenReturn(travelers);

            sut.open();
            boolean result = sut.bookRide("Julen", ride, 3, 5.0);  // Attempting to book 3 seats
            sut.close();

            assertTrue(result);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testRideNull() {
        String username = "traveler1";
        Traveler traveler = new Traveler(username, "password");

        when(db.find(Traveler.class, username)).thenReturn(traveler);

        sut.open();
        boolean result = sut.bookRide(username, null, 2, 5.0);  // Ride is null
        sut.close();

        assertEquals(false, result);
    }

    @Test
    public void testTransactionErrorRollback() {
        String username = "traveler1";
        Traveler traveler = new Traveler(username, "password");
        traveler.setMoney(50.0);
        Ride ride = new Ride("Start", "End", new Date(), 5, 10.0, new Driver("driver1", "password"));

        when(db.find(Traveler.class, username)).thenReturn(traveler);
        doThrow(new RuntimeException()).when(db).persist(any(Booking.class));

        sut.open();
        boolean result = sut.bookRide(username, ride, 2, 5.0);  // Should trigger rollback
        sut.close();

        verify(et).rollback();  // Ensure rollback is called
        assertEquals(false, result);
    }

    @Test
    public void testCommitExceptionHandling() {
        String username = "traveler1";
        Traveler traveler = new Traveler(username, "password");
        traveler.setMoney(100.0);
        Ride ride = new Ride("Start", "End", new Date(), 5, 10.0, new Driver("driver1", "password"));

        when(db.find(Traveler.class, username)).thenReturn(traveler);
        doNothing().when(db).persist(any(Booking.class));
        doThrow(new RuntimeException()).when(et).commit();

        sut.open();
        boolean result = sut.bookRide(username, ride, 2, 5.0);  // Commit should fail
        sut.close();

        assertEquals(false, result);
    }
    */
}
