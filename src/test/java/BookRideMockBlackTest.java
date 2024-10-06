import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import dataAccess.DataAccess;
import domain.Booking;
import domain.Driver;
import domain.Ride;
import domain.Traveler;

import java.util.Date;

public class BookRideMockBlackTest {

    static DataAccess sut;

    private EntityManager db;
    private EntityTransaction et;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        db = mock(EntityManager.class);
        et = mock(EntityTransaction.class);
        when(db.getTransaction()).thenReturn(et);

        sut = new DataAccess(db);
    }

    @After
    public void tearDown() {
        db = null;
        et = null;
        sut = null;
    }

    /**
     * 1. Test Scenario: traveler == null
     * If the traveler is not found, the method should return false.
     */
    @Test
    public void testTravelerNull() {
        String username = "nonexistentUser";
        Ride ride = new Ride("Start", "End", new Date(), 2, 5.0, new Driver("driver1", "password"));
        int seats = 2;
        double discount = 5.0;

        when(db.find(Traveler.class, username)).thenReturn(null);

        sut.open();
        boolean result = sut.bookRide(username, ride, seats, discount);
        sut.close();

        assertEquals(false, result);
    }

    /**
     * 2. Test Scenario: ride.getnPlaces() < seats
     * Traveler is found, but ride has insufficient seats.
     */
    @Test
    public void testInsufficientSeats() {
        String username = "traveler1";
        Traveler traveler = new Traveler(username, "password");
        Ride ride = new Ride("Start", "End", new Date(), 1, 5.0, new Driver("driver1", "password"));  // Only 1 seat available
        int seats = 2;
        double discount = 5.0;

        when(db.find(Traveler.class, username)).thenReturn(traveler);

        sut.open();
        boolean result = sut.bookRide(username, ride, seats, discount);
        sut.close();

        assertEquals(false, result);
    }

    /**
     * 3. Test Scenario: availableBalance < ridePriceDesk
     * Traveler does not have enough balance to cover the ride cost.
     */
    @Test
    public void testInsufficientBalance() {
        String username = "traveler1";
        Traveler traveler = new Traveler(username, "password");
        traveler.setMoney(0);  // Traveler has no balance
        Ride ride = new Ride("Start", "End", new Date(), 3, 10.0, new Driver("driver1", "password"));  // Price is 10.0 per seat
        int seats = 3;
        double discount = 0.0;  // No discount

        when(db.find(Traveler.class, username)).thenReturn(traveler);

        sut.open();
        boolean result = sut.bookRide(username, ride, seats, discount);
        sut.close();

        assertEquals(false, result);
    }

    /**
     * 4. Test Scenario: Successful booking
     * Traveler and ride data should be correctly updated.
     */
    @Test
    public void testSuccessfulBooking() {
        String username = "traveler1";
        Traveler traveler = new Traveler(username, "password");
        traveler.setMoney(100.0);  // Sufficient balance
        Ride ride = new Ride("Start", "End", new Date(), 5, 10.0, new Driver("driver1", "password"));
        int seats = 2;
        double discount = 5.0;  // Discount of 5.0 per seat

        when(db.find(Traveler.class, username)).thenReturn(traveler);

        sut.open();
        boolean result = sut.bookRide(username, ride, seats, discount);
        sut.close();

        assertEquals(true, result);
        assertEquals(3, ride.getnPlaces());  // 2 seats should be booked, leaving 3
        assertEquals(90.0, traveler.getMoney(), 0.01);  // Traveler's balance should be reduced to 90.0
    }

    /**
     * 5. Test Scenario: ride == null
     * NullPointerException should be handled, and method should return false.
     */
    @Test
    public void testRideNull() {
        String username = "traveler1";
        Traveler traveler = new Traveler(username, "password");
        int seats = 2;
        double discount = 5.0;

        when(db.find(Traveler.class, username)).thenReturn(traveler);

        sut.open();
        boolean result = sut.bookRide(username, null, seats, discount);
        sut.close();

        assertEquals(false, result);
    }

    /**
     * 6. Test Scenario: Transaction error (Rollback)
     * If an error occurs during db.persist or db.merge, the method should rollback.
     */
    @Test
    public void testTransactionErrorRollback() {
        String username = "traveler1";
        Traveler traveler = new Traveler(username, "password");
        traveler.setMoney(50.0);
        Ride ride = new Ride("Start", "End", new Date(), 5, 10.0, new Driver("driver1", "password"));
        int seats = 2;
        double discount = 5.0;

        when(db.find(Traveler.class, username)).thenReturn(traveler);
        doThrow(new RuntimeException()).when(db).persist(any(Booking.class));

        sut.open();
        boolean result = sut.bookRide(username, ride, seats, discount);
        sut.close();

        verify(et).rollback();  // Ensure rollback is called
        assertEquals(false, result);
    }

    /**
     * 7. Test Scenario: Exception in Transaction Commit
     * If an exception occurs during transaction commit, the method should return false.
     */
    @Test
    public void testCommitExceptionHandling() {
        String username = "traveler1";
        Traveler traveler = new Traveler(username, "password");
        traveler.setMoney(100.0);
        Ride ride = new Ride("Start", "End", new Date(), 5, 10.0, new Driver("driver1", "password"));
        int seats = 2;
        double discount = 5.0;

        when(db.find(Traveler.class, username)).thenReturn(traveler);
        doNothing().when(db).persist(any(Booking.class));
        doThrow(new RuntimeException()).when(et).commit();

        sut.open();
        boolean result = sut.bookRide(username, ride, seats, discount);
        sut.close();

        assertEquals(false, result);
    }
}
