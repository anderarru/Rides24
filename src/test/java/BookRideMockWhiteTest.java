/*
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
import domain.Booking;
import domain.Driver;
import domain.Ride;
import domain.Traveler;

import java.util.Date;

public class BookRideMockWhiteTest {

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
    // Test 1: traveler == null, expect False
    public void test1() {
        try {
            // Define parameters
            String username = null;
            Ride ride = new Ride("Start", "End", new Date(), 2, 5.0, new Driver("driver1", "password"));
            int seats = 2;
            double discount = 5.0;

            // Configure state through mocks
            Mockito.when(db.find(Traveler.class, username)).thenReturn(null);

            // Invoke SUT
            sut.open();
            boolean result = sut.bookRide(username, ride, seats, discount);

            // Verify the result
            assertEquals(false, result);
        } catch (Exception e) {
            fail();
        } finally {
            sut.close();
        }
    }

    @Test
    // Test 2: ride.getnPlaces() < seats, expect False
    public void test2() {
        try {
            // Define parameters
            String username = "traveler1";
            Ride ride = new Ride("Start", "End", new Date(), 1, 5.0, new Driver("driver1", "password")); // Only 1 seat available
            int seats = 1000; // Requesting more seats than available
            double discount = 5.0;

            // Traveler exists in DB
            Traveler traveler = new Traveler(username, "password");
            Mockito.when(db.find(Traveler.class, username)).thenReturn(traveler);

            // Invoke SUT
            sut.open();
            boolean result = sut.bookRide(username, ride, seats, discount);

            // Verify the result
            assertEquals(false, result);
        } catch (Exception e) {
            fail();
        } finally {
            sut.close();
        }
    }

    @Test
    // Test 3: availableBalance < ridePriceDesk, expect False
    public void test3() {
        try {
            // Define parameters
            String username = "traveler1";
            Traveler traveler = new Traveler(username, "password");
            traveler.setMoney(0); // Traveler has no balance
            Ride ride = new Ride("Start", "End", new Date(), 3, 10.0, new Driver("driver1", "password")); // Price is 10.0 per seat
            int seats = 3; // Booking 3 seats
            double discount = 0.0; // No discount

            // Traveler exists in DB with insufficient balance
            Mockito.when(db.find(Traveler.class, username)).thenReturn(traveler);

            // Invoke SUT
            sut.open();
            boolean result = sut.bookRide(username, ride, seats, discount);

            // Verify the result
            assertEquals(false, result);
        } catch (Exception e) {
            fail();
        } finally {
            sut.close();
        }
    }

    @Test
    // Test 4: Successful booking, expect True
    public void test4() {
        try {
            // Define parameters
            String username = "traveler1";
            Traveler traveler = new Traveler(username, "password");
            traveler.setMoney(100.0); // Sufficient balance
            Ride ride = new Ride("Start", "End", new Date(), 5, 10.0, new Driver("driver1", "password"));
            int seats = 2;
            double discount = 5.0; // Discount of 5.0 per seat

            // Traveler exists in DB and can book the ride
            Mockito.when(db.find(Traveler.class, username)).thenReturn(traveler);

            // Invoke SUT
            sut.open();
            boolean result = sut.bookRide(username, ride, seats, discount);

            // Verify the results
            assertEquals(true, result);
            assertEquals(3, ride.getnPlaces()); // 2 seats should be booked, leaving 3
            assertEquals(90.0, traveler.getMoney(), 0.01); // Traveler's balance should be reduced to 90.0
        } catch (Exception e) {
            fail();
        } finally {
            sut.close();
        }
    }

    @Test
    // Test 5: Exception handling, traveler does not exist in DB, expect False
    public void test5() {
        try {
            // Define parameters
            String username = "nonexistentUser";
            Ride ride = new Ride("Start", "End", new Date(), 2, 5.0, new Driver("driver1", "password"));
            int seats = 2;
            double discount = 5.0;

            // Configure state through mocks to simulate exception
            Mockito.when(db.find(Traveler.class, username)).thenThrow(new RuntimeException());

            // Invoke SUT
            sut.open();
            boolean result = sut.bookRide(username, ride, seats, discount);

            // Verify the result
            assertEquals(false, result);
        } catch (Exception e) {
            fail();
        } finally {
            sut.close();
        }
    }
}

*/

